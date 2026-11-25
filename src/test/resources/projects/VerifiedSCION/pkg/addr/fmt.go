// Copyright 2022 Anapaya Systems
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
	"fmt"
	"strconv"
	"strings"

	"github.com/scionproto/scion/pkg/private/serrors"
)

// ParseFormattedIA parses an IA that was formatted with the FormatIA function.
// The same options must be provided to successfully parse.
// @ trusted
// @ requires false
func ParseFormattedIA(ia string, opts ...FormatOption) (IA, error) {
	parts := strings.Split(ia, "-")
	if len(parts) != 2 {
		return 0, serrors.New("invalid ISD-AS", "value", ia)
	}
	isd, err := ParseFormattedISD(parts[0], opts...)
	if err != nil {
		return 0, serrors.WrapStr("parsing ISD part", err, "value", ia)
	}
	as_, err := ParseFormattedAS(parts[1], opts...)
	if err != nil {
		return 0, serrors.WrapStr("parsing AS part", err, "value", ia)
	}
	return MustIAFrom(isd, as_), nil
}

// ParseFormattedISD parses an ISD number that was formatted with the FormatISD
// function. The same options must be provided to successfully parse.
// @ trusted
// @ requires false
func ParseFormattedISD(isd string, opts ...FormatOption) (ISD, error) {
	o := applyFormatOptions(opts)
	if o.defaultPrefix {
		trimmed := strings.TrimPrefix(isd, "ISD")
		if trimmed == isd {
			return 0, serrors.New("prefix is missing", "prefix", "ISD", "value", isd)
		}
		isd = trimmed
	}
	return ParseISD(isd)
}

// ParseFormattedAS parses an AS number that was formatted with the FormatAS
// function. The same options must be provided to successfully parse.
// @ trusted
// @ requires false
func ParseFormattedAS(as_ string, opts ...FormatOption) (AS, error) {
	o := applyFormatOptions(opts)
	if o.defaultPrefix {
		trimmed := strings.TrimPrefix(as_, "AS")
		if trimmed == as_ {
			return 0, serrors.New("prefix is missing", "prefix", "AS", "value", as_)
		}
		as_ = trimmed
	}
	return parseAS(as_, o.separator)
}

// FormatIA formats the ISD-AS.
// @ trusted
// @ requires false
func FormatIA(ia IA, opts ...FormatOption) string {
	o := applyFormatOptions(opts)
	as_ := fmtAS(ia.AS(), o.separator)
	if o.defaultPrefix {
		return fmt.Sprintf("ISD%d-AS%s", ia.ISD(), as_)
	}
	return fmt.Sprintf("%d-%s", ia.ISD(), as_)
}

// FormatISD formats the ISD number.
// @ trusted
// @ requires false
func FormatISD(isd ISD, opts ...FormatOption) string {
	o := applyFormatOptions(opts)
	if o.defaultPrefix {
		return fmt.Sprintf("ISD%d", isd)
	}
	return strconv.Itoa(int(isd))
}

// FormatAS formats the AS number.
// @ trusted
// @ requires false
func FormatAS(as_ AS, opts ...FormatOption) string {
	o := applyFormatOptions(opts)
	s := fmtAS(as_, o.separator)
	if o.defaultPrefix {
		return "AS" + s
	}
	return s
}

// @ requires as_.InRange()
// @ decreases
func fmtAS(as_ AS, sep string) string {
	if !as_.inRange() {
		return fmt.Sprintf("%d [Illegal AS: larger than %d]", as_, MaxAS)
	}
	// Format BGP ASes as decimal
	if as_ <= MaxBGPAS {
		// (VerifiedSCION) the following property is guaranteed by the type system,
		// but Gobra cannot infer it yet
		// @ assume 0 <= as_
		return strconv.FormatUint(uint64(as_), 10)
	}
	// Format all other ASes as 'sep'-separated hex.
	// (VerifiedSCION) revert this change when Gobra is fixed.
	// const maxLen = len("ffff:ffff:ffff")
	var maxLen = len("ffff:ffff:ffff")
	var b /*@@@*/ strings.Builder
	// @ b.ZeroBuilderIsReadyToUse()
	b.Grow(maxLen)
	// @ invariant b.Mem()
	// @ decreases asParts - i
	for i := 0; i < asParts; i++ {
		if i > 0 {
			b.WriteString(sep)
		}
		shift := uint(asPartBits * (asParts - i - 1))
		// (VerifiedSCION) the following property is guaranteed by the type system,
		// but Gobra cannot infer it yet
		// @ assume 0 <= uint64(as_>>shift)&asPartMask
		b.WriteString(strconv.FormatUint(uint64(as_>>shift)&asPartMask, asPartBase))
	}
	return b.String()
}

// (VerifiedSCION) revert this change when Gobra is fixed.
// type FormatOption func(*formatOptions)
type FormatOption = func(*formatOptions)

type formatOptions struct {
	defaultPrefix bool
	separator     string
}

// @ trusted
// @ requires false
func applyFormatOptions(opts []FormatOption) formatOptions {
	o := formatOptions{
		defaultPrefix: false,
		separator:     ":",
	}
	for _, opt := range opts {
		opt(&o)
	}
	return o
}

// WithDefaultPrefix enables the default prefix which depends on the type. For
// the AS number, the prefix is 'AS'. For the ISD number, the prefix is 'ISD'.
// @ trusted
// @ requires false
func WithDefaultPrefix() FormatOption {
	return func(o *formatOptions) {
		o.defaultPrefix = true
	}
}

// WithSeparator sets the separator to use for formatting AS numbers. In case of
// the empty string, the ':' is used.
// @ trusted
// @ requires false
func WithSeparator(separator string) FormatOption {
	return func(o *formatOptions) {
		o.separator = separator
	}
}

// WithFileSeparator returns an option that sets the separator to underscore.
// @ trusted
// @ requires false
func WithFileSeparator() FormatOption {
	return WithSeparator("_")
}
