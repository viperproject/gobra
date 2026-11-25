// Copyright 2016 ETH Zurich
// Copyright 2018 ETH Zurich, Anapaya Systems
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// +gobra

package addr

import (
	"encoding"
	"flag"
	"fmt"
	"strconv"
	"strings"

	"github.com/scionproto/scion/pkg/private/serrors"
)

const (
	IABytes       = 8
	ISDBits       = 16
	ASBits        = 48
	BGPASBits     = 32
	MaxISD    ISD = (1 << ISDBits) - 1
	MaxAS     AS  = (1 << ASBits) - 1
	MaxBGPAS  AS  = (1 << BGPASBits) - 1

	asPartBits = 16
	asPartBase = 16
	asPartMask = (1 << asPartBits) - 1
	asParts    = ASBits / asPartBits
)

// ISD is the ISolation Domain identifier. See formatting and allocations here:
// https://github.com/scionproto/scion/wiki/ISD-and-AS-numbering#isd-numbers
type ISD uint16

// ParseISD parses an ISD from a decimal string. Note that ISD 0 is parsed
// without any errors.
// @ decreases
func ParseISD(s string) (ISD, error) {
	isd, err := strconv.ParseUint(s, 10, ISDBits)
	if err != nil {
		return 0, serrors.WrapStr("parsing ISD", err)
	}
	return ISD(isd), nil
}

// @ requires isd >= 0
// @ decreases
func (isd ISD) String() string {
	return strconv.FormatUint(uint64(isd), 10)
}

var _ encoding.TextUnmarshaler = (*AS)(nil)

// AS is the Autonomous System identifier. See formatting and allocations here:
// https://github.com/scionproto/scion/wiki/ISD-and-AS-numbering#as-numbers
type AS uint64

// ParseAS parses an AS from a decimal (in the case of the 32bit BGP AS number
// space) or ipv6-style hex (in the case of SCION-only AS numbers) string.
// @ ensures retErr == nil ==> retAs.InRange()
// @ decreases
func ParseAS(_as string) (retAs AS, retErr error) {
	return parseAS(_as, ":")
}

// @ ensures retErr == nil ==> retAs.InRange()
// @ decreases
func parseAS(_as string, sep string) (retAs AS, retErr error) {
	parts := strings.Split(_as, sep)
	if len(parts) == 1 {
		// Must be a BGP AS, parse as 32-bit decimal number
		return asParseBGP(_as)
	}

	if len(parts) != asParts {
		return 0, serrors.New("wrong number of separators", "sep", sep, "value", _as)
	}
	var parsed AS
	//@ invariant 0 <= i && i <= asParts
	//@ invariant acc(parts)
	//@ decreases asParts - i
	for i := 0; i < asParts; i++ {
		parsed <<= asPartBits
		v, err := strconv.ParseUint(parts[i], asPartBase, asPartBits)
		if err != nil {
			return 0, serrors.WrapStr("parsing AS part", err, "index", i, "value", _as)
		}
		parsed |= AS(v)
	}
	// This should not be reachable. However, we leave it here to protect
	// against future refactor mistakes.
	if !parsed.inRange() {
		// (VerifiedSCION) Added cast around MaxAS to be able to call serrors.New
		return 0, serrors.New("AS out of range", "max", uint64(MaxAS), "value", _as)
	}
	return parsed, nil
}

// @ ensures retErr == nil ==> retAs.InRange()
// @ decreases
func asParseBGP(s string) (retAs AS, retErr error) {
	_as, err := strconv.ParseUint(s, 10, BGPASBits)
	if err != nil {
		return 0, serrors.WrapStr("parsing BGP AS", err)
	}
	// (VerifiedSCION)
	// The following assertions are needed to prove retAs.InRange().
	// Gobra is not able to infer this automatically from the definition
	// of strconv.Exp, unless we put a postcondition saying that the
	// result is equal to the body.
	// @ assert strconv.Exp(2, BGPASBits) == 2 * strconv.Exp(2, 31)
	// @ assert strconv.Exp(2, BGPASBits) == 4 * strconv.Exp(2, 30)
	// @ strconv.Exp2to10(30)
	// @ strconv.Exp2to10(20)
	// @ strconv.Exp2to10(10)
	// @ assert _as < uint64(strconv.Exp(2, BGPASBits))
	return AS(_as), nil
}

// @ requires _as.InRange()
// @ decreases
func (_as AS) String() string {
	return fmtAS(_as, ":")
}

// @ decreases
// @ pure
func (_as AS) inRange() bool {
	return _as <= MaxAS
}

// @ decreases
func (_as AS) MarshalText() ([]byte, error) {
	if !_as.inRange() {
		// (VerifiedSCION) Added cast around MaxAS and as to be able to call serrors.New
		return nil, serrors.New("AS out of range", "max", uint64(MaxAS), "value", uint64(_as))
	}
	return []byte(_as.String()), nil
}

// @ preserves acc(_as)
// @ preserves forall i int :: { &text[i] } 0 <= i && i < len(text) ==> acc(&text[i])
// @ decreases
func (_as *AS) UnmarshalText(text []byte) error {
	parsed, err := ParseAS(string(text))
	if err != nil {
		return err
	}
	*_as = parsed
	return nil
}

var _ fmt.Stringer = IA(0)
var _ encoding.TextUnmarshaler = (*IA)(nil)
var _ flag.Value = (*IA)(nil)

// IA represents the ISD (ISolation Domain) and AS (Autonomous System) Id of a given SCION AS.
// The highest 16 bit form the ISD number and the lower 48 bits form the AS number.
type IA uint64

// MustIAFrom creates an IA from the ISD and AS number. It panics if any error
// is encountered. Callers must ensure that the values passed to this function
// are valid.
// @ requires _as.InRange()
// @ decreases
func MustIAFrom(isd ISD, _as AS) IA {
	ia, err := IAFrom(isd, _as)
	if err != nil {
		panic(fmt.Sprintf("parsing ISD-AS: %s", err))
	}
	return ia
}

// IAFrom creates an IA from the ISD and AS number.
// @ requires _as.InRange()
// @ ensures err == nil
// @ decreases
func IAFrom(isd ISD, _as AS) (ia IA, err error) {
	if !_as.inRange() {
		return 0, serrors.New("AS out of range", "max", MaxAS, "value", _as)
	}
	return IA(isd)<<ASBits | IA(_as&MaxAS), nil
}

// ParseIA parses an IA from a string of the format 'isd-as'.
// @ decreases
func ParseIA(ia string) (IA, error) {
	parts := strings.Split(ia, "-")
	if len(parts) != 2 {
		return 0, serrors.New("invalid ISD-AS", "value", ia)
	}
	isd, err := ParseISD(parts[0])
	if err != nil {
		return 0, err
	}
	_as, err := ParseAS(parts[1])
	if err != nil {
		return 0, err
	}
	return MustIAFrom(isd, _as), nil
}

// @ decreases
func (ia IA) ISD() ISD {
	return ISD(ia >> ASBits)
}

// @ decreases
func (ia IA) AS() AS {
	return AS(ia) & MaxAS
}

// @ decreases
func (ia IA) MarshalText() ([]byte, error) {
	return []byte(ia.String()), nil
}

// @ preserves acc(ia)
// @ preserves forall i int :: { &b[i] } 0 <= i && i < len(b) ==> acc(&b[i])
// @ decreases
func (ia *IA) UnmarshalText(b []byte) error {
	parsed, err := ParseIA(string(b))
	if err != nil {
		return err
	}
	*ia = parsed
	return nil
}

// @ decreases
// @ pure
func (ia IA) IsZero() bool {
	return ia == 0
}

// @ decreases
func (ia IA) Equal(other IA) bool {
	return ia == other
}

// IsWildcard returns whether the ia has a wildcard part (isd or as).
// @ decreases
func (ia IA) IsWildcard() bool {
	return ia.ISD() == 0 || ia.AS() == 0
}

// @ decreases
func (ia IA) String() string {
	// (VerifiedSCION) Added casts around ia.ISD() and ia.AS() to be able to pass them to 'fmt.Sprintf'
	return fmt.Sprintf("%d-%s", ia.ISD(), ia.AS())
}

// Set implements flag.Value interface
// @ preserves acc(ia)
// @ decreases
func (ia *IA) Set(s string) error {
	pIA, err := ParseIA(s)
	if err != nil {
		return err
	}
	*ia = pIA
	return nil
}
