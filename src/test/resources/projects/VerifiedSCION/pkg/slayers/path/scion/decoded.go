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

package scion

import (
	//@ "encoding/binary"
	"github.com/scionproto/scion/pkg/private/serrors"
	"github.com/scionproto/scion/pkg/slayers/path"
	//@ . "github.com/scionproto/scion/verification/utils/definitions"
	//@ sl "github.com/scionproto/scion/verification/utils/slices"
)

// Decoded implements the SCION (data-plane) path type. Decoded is intended to be used in
// non-performance critical code paths, where the convenience of having a fully parsed path trumps
// the loss of performance.
type Decoded struct {
	Base
	// InfoFields contains all the InfoFields of the path.
	InfoFields []path.InfoField
	// HopFields contains all the HopFields of the path.
	HopFields []path.HopField
}

// DecodeFromBytes fully decodes the SCION path into the corresponding fields.
// @ requires  s.NonInitMem()
// @ preserves acc(sl.Bytes(data, 0, len(data)), R42)
// @ ensures   r == nil ==> (
// @ 	s.Mem(data) &&
// @ 	let lenD := len(data) in
// @ 	MetaLen <= lenD &&
// @ 	let b0 := sl.GetByte(data, 0, lenD, 0) in
// @ 	let b1 := sl.GetByte(data, 0, lenD, 1) in
// @ 	let b2 := sl.GetByte(data, 0, lenD, 2) in
// @ 	let b3 := sl.GetByte(data, 0, lenD, 3) in
// @ 	let line := binary.BigEndian.Uint32Spec(b0, b1, b2, b3) in
// @ 	let metaHdr := DecodedFrom(line) in
// @ 	metaHdr == s.GetMetaHdr(data))
// @ ensures   r == nil ==> s.GetBase(data).WeaklyValid()
// @ ensures   r != nil ==> (r.ErrorMem() && s.NonInitMem())
// @ decreases
func (s *Decoded) DecodeFromBytes(data []byte) (r error) {
	//@ unfold s.NonInitMem()
	if err := s.Base.DecodeFromBytes(data); err != nil {
		//@ fold s.NonInitMem()
		return err
	}
	// (VerifiedSCION) Gobra expects a stronger contract for s.Len() when in fact
	// what happens here is that we just call the same function in s.Base.
	if minLen := s. /*@ Base. @*/ Len(); len(data) < minLen {
		//@ s.Base.DowngradePerm()
		//@ fold s.NonInitMem()
		return serrors.New("DecodedPath raw too short", "expected", minLen, "actual", int(len(data)))
	}
	offset := MetaLen
	s.InfoFields = make([]path.InfoField, ( /*@ unfolding s.Base.Mem() in @*/ s.NumINF))
	//@ assert len(data) >= MetaLen + s.Base.GetNumINF() * path.InfoLen + s.Base.GetNumHops() * path.HopLen
	//@ sl.SplitByIndex_Bytes(data, 0, len(data), offset, R43)

	//@ invariant acc(&s.InfoFields)
	//@ invariant acc(s.Base.Mem(), R1)
	//@ invariant len(s.InfoFields) == s.Base.GetNumINF()
	//@ invariant 0 <= i && i <= s.Base.GetNumINF()
	//@ invariant len(data) >= MetaLen + s.Base.GetNumINF() * path.InfoLen + s.Base.GetNumHops() * path.HopLen
	//@ invariant offset == MetaLen + i * path.InfoLen
	//@ invariant forall j int :: { &s.InfoFields[j] } 0 <= j && j < s.Base.GetNumINF() ==> acc(&s.InfoFields[j])
	//@ invariant acc(sl.Bytes(data, 0, offset), R43)
	//@ invariant acc(sl.Bytes(data, offset, len(data)), R43)
	//@ decreases s.Base.GetNumINF() - i
	for i := 0; i < /*@ unfolding acc(s.Base.Mem(), _) in @*/ s.NumINF; i++ {
		//@ sl.SplitByIndex_Bytes(data, offset, len(data), offset + path.InfoLen, R43)
		//@ sl.Reslice_Bytes(data, offset, offset + path.InfoLen, R43)
		if err := s.InfoFields[i].DecodeFromBytes(data[offset : offset+path.InfoLen]); err != nil {
			// (VerifiedSCION) infofield.DecodeFromBytes guarantees that err == nil.
			// Thus, this branch is not reachable.
			return err
		}
		//@ assert len(data[offset:offset+path.InfoLen]) == path.InfoLen
		//@ sl.Unslice_Bytes(data, offset, offset + path.InfoLen, R43)
		//@ sl.CombineAtIndex_Bytes(data, 0, offset + path.InfoLen, offset, R43)
		offset += path.InfoLen
	}
	s.HopFields = make([]path.HopField, ( /*@ unfolding s.Base.Mem() in @*/ s.NumHops))
	//@ invariant acc(&s.HopFields)
	//@ invariant acc(s.Base.Mem(), R1)
	//@ invariant len(s.HopFields) == s.Base.GetNumHops()
	//@ invariant 0 <= i && i <= s.Base.GetNumHops()
	//@ invariant forall j int :: { &s.HopFields[j] } i <= j && j < s.Base.GetNumHops() ==> acc(&s.HopFields[j])
	//@ invariant forall j int :: { &s.HopFields[j] } 0 <= j && j < i ==> s.HopFields[j].Mem()
	//@ invariant len(data) >= MetaLen + s.Base.GetNumINF() * path.InfoLen + s.Base.GetNumHops() * path.HopLen
	//@ invariant offset == MetaLen + s.Base.GetNumINF() * path.InfoLen + i * path.HopLen
	//@ invariant acc(sl.Bytes(data, 0, offset), R43)
	//@ invariant acc(sl.Bytes(data, offset, len(data)), R43)
	//@ decreases s.Base.GetNumHops() - i
	for i := 0; i < /*@ unfolding acc(s.Base.Mem(), R2) in @*/ s.NumHops; i++ {
		//@ sl.SplitByIndex_Bytes(data, offset, len(data), offset + path.HopLen, R43)
		//@ sl.Reslice_Bytes(data, offset, offset + path.HopLen, R43)
		if err := s.HopFields[i].DecodeFromBytes(data[offset : offset+path.HopLen]); err != nil {
			// (VerifiedSCION) infofield.DecodeFromBytes guarantees that err == nil.
			// Thus, this branch should not be reachable.
			return err
		}
		//@ assert len(data[offset:offset+path.HopLen]) == path.HopLen
		//@ sl.Unslice_Bytes(data, offset, offset + path.HopLen, R43)
		//@ sl.CombineAtIndex_Bytes(data, 0, offset + path.HopLen, offset, R43)
		offset += path.HopLen
	}
	//@ sl.CombineAtIndex_Bytes(data, 0, len(data), offset, R43)
	//@ fold s.Mem(data)
	return nil
}

// SerializeTo writePerms the path to a slice. The slice must be big enough to hold the entire data,
// otherwise an error is returned.
// @ preserves acc(s.Mem(ubuf), R1)
// @ preserves sl.Bytes(ubuf, 0, len(ubuf))
// @ preserves b !== ubuf ==> sl.Bytes(b, 0, len(b))
// @ ensures   r != nil ==> r.ErrorMem()
// @ decreases
func (s *Decoded) SerializeTo(b []byte /*@, ghost ubuf []byte @*/) (r error) {
	if len(b) < s.Len( /*@ ubuf @*/ ) {
		return serrors.New("buffer too small to serialize path.", "expected", s.Len( /*@ ubuf @*/ ),
			"actual", len(b))
	}
	//@ unfold acc(s.Mem(ubuf), R1)
	//@ assert sl.Bytes(b, 0, len(b))
	//@ sl.SplitByIndex_Bytes(b, 0, len(b), MetaLen, writePerm)
	//@ sl.Reslice_Bytes(b, 0, MetaLen, writePerm)
	//@ unfold acc(s.Base.Mem(), R1)
	if err := s.PathMeta.SerializeTo(b[:MetaLen]); err != nil {
		// @ Unreachable()
		return err
	}
	//@ fold acc(s.Base.Mem(), R1)
	//@ sl.Unslice_Bytes(b, 0, MetaLen, writePerm)
	//@ sl.CombineAtIndex_Bytes(b, 0, len(b), MetaLen, writePerm)
	//@ fold acc(s.Mem(ubuf), R1)
	offset := MetaLen

	//@ invariant acc(s.Mem(ubuf), R1)
	//@ invariant sl.Bytes(ubuf, 0, len(ubuf))
	//@ invariant b !== ubuf ==> sl.Bytes(b, 0, len(b))
	//@ invariant s.LenSpec(ubuf) <= len(b)
	//@ invariant 0 <= i && i <= s.getLenInfoFields(ubuf)
	//@ invariant offset == MetaLen + i * path.InfoLen
	//@ invariant MetaLen + s.getLenInfoFields(ubuf) * path.InfoLen + s.getLenHopFields(ubuf) * path.HopLen <= len(b)
	//@ decreases s.getLenInfoFields(ubuf) - i
	// (VerifiedSCION) TODO: reinstate the original range clause
	// for _, info := range s.InfoFields {
	for i := 0; i < /*@ unfolding acc(s.Mem(ubuf), _) in @*/ len(s.InfoFields); i++ {
		//@ unfold acc(s.Mem(ubuf), R1)
		info := &s.InfoFields[i]
		//@ sl.SplitByIndex_Bytes(b, 0, len(b), offset, writePerm)
		//@ sl.SplitByIndex_Bytes(b, offset, len(b), offset + path.InfoLen, writePerm)
		//@ sl.Reslice_Bytes(b, offset, offset + path.InfoLen, writePerm)
		//@ assert sl.Bytes(b[offset:offset+path.InfoLen], 0, path.InfoLen)
		if err := info.SerializeTo(b[offset : offset+path.InfoLen]); err != nil {
			//@ Unreachable()
			return err
		}
		//@ sl.Unslice_Bytes(b, offset, offset + path.InfoLen, writePerm)
		//@ sl.CombineAtIndex_Bytes(b, offset, len(b), offset + path.InfoLen, writePerm)
		//@ sl.CombineAtIndex_Bytes(b, 0, len(b), offset, writePerm)
		//@ fold acc(s.Mem(ubuf), R1)
		offset += path.InfoLen
	}
	//@ invariant acc(s.Mem(ubuf), R1)
	//@ invariant sl.Bytes(ubuf, 0, len(ubuf))
	//@ invariant b !== ubuf ==> sl.Bytes(b, 0, len(b))
	//@ invariant s.LenSpec(ubuf) <= len(b)
	//@ invariant 0 <= i && i <= s.getLenHopFields(ubuf)
	//@ invariant offset == MetaLen + s.getLenInfoFields(ubuf) * path.InfoLen + i * path.HopLen
	//@ invariant MetaLen + s.getLenInfoFields(ubuf) * path.InfoLen + s.getLenHopFields(ubuf) * path.HopLen <= len(b)
	//@ decreases s.getLenHopFields(ubuf)-i
	// (VerifiedSCION) TODO: reinstate the original range clause
	// for _, hop := range s.HopFields {
	for i := 0; i < /*@ unfolding acc(s.Mem(ubuf), _) in @*/ len(s.HopFields); i++ {
		//@ unfold acc(s.Mem(ubuf), R1)
		hop := &s.HopFields[i]
		//@ sl.SplitByIndex_Bytes(b, 0, len(b), offset, writePerm)
		//@ sl.SplitByIndex_Bytes(b, offset, len(b), offset + path.HopLen, writePerm)
		//@ sl.Reslice_Bytes(b, offset, offset + path.HopLen, writePerm)
		if err := hop.SerializeTo(b[offset : offset+path.HopLen]); err != nil {
			//@ Unreachable()
			return err
		}
		//@ sl.Unslice_Bytes(b, offset, offset + path.HopLen, writePerm)
		//@ sl.CombineAtIndex_Bytes(b, offset, len(b), offset + path.HopLen, writePerm)
		//@ sl.CombineAtIndex_Bytes(b, 0, len(b), offset, writePerm)
		//@ fold acc(s.Mem(ubuf), R1)
		offset += path.HopLen
	}
	return nil
}

// Reverse reverses a SCION path.
// @ requires s.Mem(ubuf)
// @ ensures  r == nil ==> (
// @	p != nil                    &&
// @	p.Mem(ubuf)                 &&
// @	p == s                      &&
// @	typeOf(p) == type[*Decoded] &&
// @	(old(s.GetBase(ubuf).Valid()) ==> s.GetBase(ubuf).Valid()))
// @ ensures  r != nil ==> r.ErrorMem() && s.Mem(ubuf)
// @ decreases
func (s *Decoded) Reverse( /*@ ghost ubuf []byte @*/ ) (p path.Path, r error) {
	//@ ghost isValid := s.GetBase(ubuf).Valid()
	//@ ghost base := s.GetBase(ubuf)
	//@ ghost metaHdrAferReversingSegLen := MetaHdr {
	//@ 	CurrINF: base.PathMeta.CurrINF,
	//@ 	CurrHF:  base.PathMeta.CurrHF,
	//@ 	SegLen:  base.ReverseSegLen(),
	//@ }
	//@ ghost baseAfterReversingSegLen := Base {
	//@ 	PathMeta: metaHdrAferReversingSegLen,
	//@ 	NumINF:   base.NumINF,
	//@ 	NumHops:  base.NumHops,
	//@ }
	//@ unfold s.Mem(ubuf)
	//@ unfold s.Base.Mem()
	if s.NumINF == 0 {
		//@ fold s.Base.Mem()
		//@ fold s.Mem(ubuf)
		return nil, serrors.New("empty decoded path is invalid and cannot be reversed")
	}

	// Reverse order of InfoFields and SegLens
	if s.NumINF > 1 {
		lastIdx := s.NumINF - 1
		s.InfoFields[0], s.InfoFields[lastIdx] = s.InfoFields[lastIdx], s.InfoFields[0]
		s.PathMeta.SegLen[0], s.PathMeta.SegLen[lastIdx] = s.PathMeta.SegLen[lastIdx], s.PathMeta.SegLen[0]
	}
	//@ fold s.Base.Mem()
	//@ invariant acc(s.Base.Mem(), R10)
	//@ invariant 0 <= i && i <= s.Base.GetNumINF()
	//@ invariant acc(&s.InfoFields, R10)
	//@ invariant len(s.InfoFields) == s.Base.GetNumINF()
	//@ invariant forall i int :: { &s.InfoFields[i] } 0 <= i && i < len(s.InfoFields) ==>
	//@ 	(acc(&s.InfoFields[i].ConsDir))
	//@ decreases MaxINFs-i
	// Reverse cons dir flags
	for i := 0; i < ( /*@ unfolding acc(s.Base.Mem(), R11) in @*/ s.NumINF); i++ {
		info := &s.InfoFields[i]
		info.ConsDir = !info.ConsDir
	}
	//@ fold s.Mem(ubuf)

	// Reverse order of hop fields
	//@ invariant s.Mem(ubuf)
	//@ invariant 0 <= i && i <= s.GetNumHops(ubuf)
	//@ invariant -1 <= j && j < s.GetNumHops(ubuf)
	//@ invariant s.GetBase(ubuf) == baseAfterReversingSegLen
	//@ decreases j-i
	for i, j := 0, ( /*@ unfolding s.Mem(ubuf) in (unfolding s.Base.Mem() in @*/ s.NumHops - 1 /*@ ) @*/); i < j; i, j = i+1, j-1 {
		//@ unfold s.Mem(ubuf)
		//@ assert &s.HopFields[i] != &s.HopFields[j]
		//@ unfold s.HopFields[i].Mem()
		//@ unfold s.HopFields[j].Mem()
		//@ assert acc(&s.HopFields[i]) && acc(&s.HopFields[j])
		s.HopFields[i], s.HopFields[j] = s.HopFields[j], s.HopFields[i]
		//@ assert acc(&s.HopFields[i]) && acc(&s.HopFields[j])
		//@ fold s.HopFields[i].Mem()
		//@ fold s.HopFields[j].Mem()
		//@ fold s.Mem(ubuf)
	}
	// Update CurrINF and CurrHF and SegLens
	//@ unfold s.Mem(ubuf)
	//@ unfold s.Base.Mem()
	s.PathMeta.CurrINF = uint8(s.NumINF) - s.PathMeta.CurrINF - 1
	s.PathMeta.CurrHF = uint8(s.NumHops) - s.PathMeta.CurrHF - 1
	//@ assert s.Base == base.ReverseSpec()
	//@ ghost if isValid { base.ReversingBaseValidSegLenHasValidSegLen() }
	//@ assert isValid ==> s.Base.Valid()
	//@ fold s.Base.Mem()
	//@ fold s.Mem(ubuf)
	return s, nil
}

// ToRaw tranforms scion.Decoded into scion.Raw.
// @ preserves s.Mem(ubuf1)
// @ preserves sl.Bytes(ubuf1, 0, len(ubuf1))
// @ ensures   err == nil ==> r.Mem(ubuf2)
// @ ensures   err != nil ==> err.ErrorMem()
// @ decreases
func (s *Decoded) ToRaw( /*@ ghost ubuf1 []byte @*/ ) (r *Raw, err error /*@, ghost ubuf2 []byte @*/) {
	// (VerifiedSCION) if `tmp` is not used, Gobra complains that
	// make cannot contain ghost subexpressions
	tmp := s.Len( /*@ ubuf1 @*/ )
	b := make([]byte, tmp)
	//@ fold sl.Bytes(b, 0, len(b))
	if err := s.SerializeTo(b /*@, ubuf1 @*/); err != nil {
		return nil, err /*@, b @*/
	}
	raw := &Raw{}
	//@ fold raw.Base.NonInitMem()
	//@ fold raw.NonInitMem()
	if err := raw.DecodeFromBytes(b); err != nil {
		return nil, err /*@, b @*/
	}
	return raw, nil /*@, b @*/
}
