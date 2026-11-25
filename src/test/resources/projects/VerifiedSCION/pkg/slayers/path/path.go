// Copyright 2020 Anapaya Systems
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

package path

// @ friendPkg "../" PkgMem()
// We have a non-quantified friendPkg clause below to avoid perf. issues.
// @ friendPkg "../" RegisteredTypes().DoesNotContain(0) &&
// @ 	RegisteredTypes().DoesNotContain(1) &&
// @ 	RegisteredTypes().DoesNotContain(2) &&
// @ 	RegisteredTypes().DoesNotContain(3)

import (
	"fmt"

	"github.com/scionproto/scion/pkg/private/serrors"
	//@ . "github.com/scionproto/scion/verification/utils/definitions"
	//@ "github.com/scionproto/scion/verification/utils/monoset"
	//@ sl "github.com/scionproto/scion/verification/utils/slices"
)

// PathType is uint8 so 256 values max.
const maxPathType = 256

var (
	registeredPaths/*@@@*/ [maxPathType]metadata
	strictDecoding/*@@@*/ bool = true
)

// @ ghost var registeredKeys monoset.BoundedMonotonicSet = monoset.Alloc(0, int64(maxPathType))

func init() {
	// (VerifiedSCION) ghost initialization code to establish the PathPackageMem predicate.
	// @ assert acc(&registeredPaths)
	// @ assert acc(&strictDecoding)
	// @ fold PkgMem()
}

// Type indicates the type of the path contained in the SCION header.
type Type uint8

// @ requires 0 <= t && t < MaxPathType
// @ preserves acc(PkgMem(), R20)
// @ decreases
func (t Type) String() string {
	//@ unfold acc(PkgMem(), R20)
	//@ ghost defer fold acc(PkgMem(), R20)
	pm := registeredPaths[t]
	if !pm.inUse {
		return fmt.Sprintf("UNKNOWN (%d)", t)
	}
	return fmt.Sprintf("%v (%d)", pm.Desc, t)
}

// Path is the path contained in the SCION header.
type Path interface {
	// (VerifiedSCION) Must hold for every valid Path.
	//@ pred Mem(ub []byte)
	// (VerifiedSCION) Must imply the resources required to initialize
	// a new instance of a predicate.
	//@ pred NonInitMem()
	// SerializeTo serializes the path into the provided buffer.
	// (VerifiedSCION) There are implementations of this interface that modify the underlying
	// structure when serializing (e.g. scion.Raw)
	//@ preserves sl.Bytes(ub, 0, len(ub))
	//@ preserves acc(Mem(ub), R1)
	//@ preserves sl.Bytes(b, 0, len(b))
	//@ ensures   e != nil ==> e.ErrorMem()
	//@ decreases
	SerializeTo(b []byte /*@, ghost ub []byte @*/) (e error)
	// DecodesFromBytes decodes the path from the provided buffer.
	// (VerifiedSCION) There are implementations of this interface (e.g., scion.Raw) that
	// store b and use it as internal data.
	//@ requires  NonInitMem()
	//@ preserves acc(sl.Bytes(b, 0, len(b)), R42)
	//@ ensures   err == nil ==> Mem(b)
	//@ ensures   err == nil ==> IsValidResultOfDecoding(b)
	//@ ensures   err != nil ==> err.ErrorMem()
	//@ ensures   err != nil ==> NonInitMem()
	//@ decreases
	DecodeFromBytes(b []byte) (err error)
	//@ ghost
	//@ pure
	//@ requires Mem(b)
	//@ requires sl.Bytes(b, 0, len(b))
	//@ decreases
	//@ IsValidResultOfDecoding(b []byte) bool
	// Reverse reverses a path such that it can be used in the reversed direction.
	// XXX(shitz): This method should possibly be moved to a higher-level path manipulation package.
	//@ requires  Mem(ub)
	//@ preserves sl.Bytes(ub, 0, len(ub))
	//@ ensures   e == nil ==> p != nil
	//@ ensures   e == nil ==> p.Mem(ub)
	//@ ensures   e != nil ==> e.ErrorMem()
	//@ decreases
	Reverse( /*@ ghost ub []byte @*/ ) (p Path, e error)
	//@ ghost
	//@ pure
	//@ requires Mem(ub)
	//@ ensures  0 <= l
	//@ decreases
	//@ LenSpec(ghost ub []byte) (l int)

	// Len returns the length of a path in bytes.
	//@ preserves acc(Mem(ub), R50)
	//@ ensures   l == LenSpec(ub)
	//@ decreases
	Len( /*@ ghost ub []byte @*/ ) (l int)
	// Type returns the type of a path.
	//@ pure
	//@ requires Mem(ub)
	//@ decreases
	Type( /*@ ghost ub []byte @*/ ) Type
	//@ ghost
	//@ requires Mem(ub)
	//@ ensures  NonInitMem()
	//@ decreases
	//@ DowngradePerm(ghost ub []byte)
}

type metadata struct {
	inUse bool
	Metadata
}

// Metadata defines a new SCION path type, used for dynamic SICON path type registration.
type Metadata struct {
	// Type is a unique value for the path.
	Type Type
	// Desc is the description/name of the path.
	Desc string
	// New is a path constructor function.
	New func() Path
}

// RegisterPath registers a new SCION path type globally.
// The PathType passed in must be unique, or a runtime panic will occur.
// @ requires 0 <= pathMeta.Type && pathMeta.Type < MaxPathType
// @ requires PkgMem()
// @ requires RegisteredTypes().DoesNotContain(int64(pathMeta.Type))
// @ requires pathMeta.New implements NewPathSpec
// @ ensures  PkgMem()
// @ ensures  RegisteredTypes().Contains(int64(pathMeta.Type))
// @ decreases
func RegisterPath(pathMeta Metadata) {
	//@ unfold PkgMem()
	pm := registeredPaths[pathMeta.Type]
	// @ RegisteredTypes().DoesNotContainImpliesNotFContains(int64(pathMeta.Type))
	if pm.inUse {
		panic("path type already registered")
	}
	// @ RegisteredTypes().Add(int64(pathMeta.Type))
	registeredPaths[pathMeta.Type].inUse = true
	registeredPaths[pathMeta.Type].Metadata = pathMeta
	// @ RegisteredTypes().ContainsImpliesFContains(int64(pathMeta.Type))
	//@ fold PkgMem()
}

// StrictDecoding enables or disables strict path decoding. If enabled, unknown
// path types fail to decode. If disabled, unknown path types are decoded into a
// raw path that keeps the encoded path around for re-serialization.
//
// Strict parsing is enabled by default.
//
// Experimental: This function is experimental and might be subject to change.
// @ requires PkgMem()
// @ ensures  PkgMem()
// @ decreases
func StrictDecoding(strict bool) {
	//@ unfold PkgMem()
	strictDecoding = strict
	//@ fold PkgMem()
}

// NewPath returns a new path object of pathType.
// @ requires 0 <= pathType && pathType < MaxPathType
// @ requires acc(PkgMem(), _)
// @ ensures  e != nil ==> e.ErrorMem()
// @ ensures  e == nil ==> p != nil && p.NonInitMem()
// @ decreases
func NewPath(pathType Type) (p Path, e error) {
	//@ unfold acc(PkgMem(), _)
	pm := registeredPaths[pathType]
	if !pm.inUse {
		if strictDecoding {
			return nil, serrors.New("unsupported path", "type", uint8(pathType))
		}
		tmp := &rawPath{}
		//@ fold tmp.NonInitMem()
		return tmp, nil
	}
	return pm.New() /*@ as NewPathSpec @*/, nil
}

// NewRawPath returns a new raw path that can hold any path type.
// @ ensures p != nil
// @ ensures p.NonInitMem()
// @ decreases
func NewRawPath() (p Path) {
	p = &rawPath{}
	//@ fold p.NonInitMem()
	return p
}

type rawPath struct {
	raw      []byte
	pathType Type
}

// @ preserves acc(p.Mem(ub), R10)
// @ preserves acc(sl.Bytes(ub, 0, len(ub)), R10)
// @ preserves sl.Bytes(b, 0, len(b))
// @ ensures   e == nil
// @ decreases
func (p *rawPath) SerializeTo(b []byte /*@, ghost ub []byte @*/) (e error) {
	//@ unfold sl.Bytes(b, 0, len(b))
	//@ unfold acc(p.Mem(ub), R10)
	//@ unfold acc(sl.Bytes(p.raw, 0, len(p.raw)), R11)
	copy(b, p.raw /*@, R11 @*/)
	//@ fold acc(sl.Bytes(p.raw, 0, len(p.raw)), R11)
	//@ fold acc(p.Mem(ub), R10)
	//@ fold sl.Bytes(b, 0, len(b))
	return nil
}

// @ requires  p.NonInitMem()
// @ preserves acc(sl.Bytes(b, 0, len(b)), R42)
// @ ensures   p.Mem(b)
// @ ensures   e == nil
// @ decreases
func (p *rawPath) DecodeFromBytes(b []byte) (e error) {
	//@ unfold p.NonInitMem()
	p.raw = b
	//@ fold p.Mem(b)
	return nil
}

// @ ensures  e != nil && e.ErrorMem()
// @ decreases
func (p *rawPath) Reverse( /*@ ghost ub []byte @*/ ) (r Path, e error) {
	return nil, serrors.New("not supported")
}

// @ preserves acc(p.Mem(ub), R50)
// @ ensures   l == p.LenSpec(ub)
// @ decreases
func (p *rawPath) Len( /*@ ghost ub []byte @*/ ) (l int) {
	return /*@ unfolding acc(p.Mem(ub), R50) in @*/ len(p.raw)
}

// @ pure
// @ requires p.Mem(ub)
// @ decreases
func (p *rawPath) Type( /*@ ghost ub []byte @*/ ) Type {
	return /*@ unfolding p.Mem(ub) in @*/ p.pathType
}
