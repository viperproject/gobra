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
	// @ "encoding/binary"
	"github.com/scionproto/scion/pkg/private/serrors"
	"github.com/scionproto/scion/pkg/slayers/path"
	//@ . "github.com/scionproto/scion/verification/utils/definitions"
	//@ sl "github.com/scionproto/scion/verification/utils/slices"
	//@ io "verification/io"
)

// Raw is a raw representation of the SCION (data-plane) path type. It is designed to parse as
// little as possible and should be used if performance matters.
type Raw struct {
	Base
	Raw []byte
}

// DecodeFromBytes only decodes the PathMetaHeader. Otherwise the nothing is decoded and simply kept
// as raw bytes.
// @ requires  s.NonInitMem()
// @ preserves acc(sl.Bytes(data, 0, len(data)), R42)
// @ ensures   res == nil ==> s.Mem(data)
// @ ensures   res == nil ==>
// @ 	s.GetBase(data).WeaklyValid() &&
// @ 	s.GetBase(data).EqAbsHeader(data)
// @ ensures   res != nil ==> (s.NonInitMem() && res.ErrorMem())
// @ decreases
func (s *Raw) DecodeFromBytes(data []byte) (res error) {
	//@ unfold s.NonInitMem()
	if err := s.Base.DecodeFromBytes(data); err != nil {
		//@ fold s.NonInitMem()
		return err
	}
	// (VerifiedSCION) Gobra expects a stronger contract for s.Len() when in fact what
	// happens here is that we just call the same function in s.Base.
	pathLen := s. /*@ Base. @*/ Len()
	if len(data) < pathLen {
		//@ s.Base.DowngradePerm()
		//@ fold s.NonInitMem()
		return serrors.New("RawPath raw too short", "expected", pathLen, "actual", int(len(data)))
	}
	s.Raw = data[:pathLen]
	//@ fold s.Mem(data)
	return nil
}

// SerializeTo writes the path to a slice. The slice must be big enough to hold the entire data,
// otherwise an error is returned.
// @ preserves acc(s.Mem(ubuf), R1)
// @ preserves sl.Bytes(ubuf, 0, len(ubuf))
// @ preserves sl.Bytes(b, 0, len(b))
// @ ensures   r != nil ==> r.ErrorMem()
// @ decreases
func (s *Raw) SerializeTo(b []byte /*@, ghost ubuf []byte @*/) (r error) {
	if /*@ unfolding acc(s.Mem(ubuf), _) in @*/ s.Raw == nil {
		return serrors.New("raw is nil")
	}
	if minLen := s.Len( /*@ ubuf @*/ ); len(b) < minLen {
		return serrors.New("buffer too small", "expected", minLen, "actual", len(b))
	}
	//@ unfold acc(s.Mem(ubuf), R1)
	// XXX(roosd): This modifies the underlying buffer. Consider writing to data
	// directly.
	//@ unfold acc(s.Base.Mem(), R1)
	//@ sl.SplitRange_Bytes(ubuf, 0, len(s.Raw), writePerm)
	//@ assert sl.Bytes(s.Raw, 0, len(s.Raw))
	//@ sl.SplitRange_Bytes(s.Raw, 0, MetaLen, writePerm)
	if err := s.PathMeta.SerializeTo(s.Raw[:MetaLen]); err != nil {
		// @ Unreachable()
		return err
	}
	//@ fold acc(s.Base.Mem(), R1)
	//@ sl.CombineRange_Bytes(s.Raw, 0, MetaLen, writePerm)
	//@ unfold acc(sl.Bytes(s.Raw, 0, len(s.Raw)), R2)
	//@ unfold sl.Bytes(b, 0, len(b))
	copy(b, s.Raw /*@ , R2 @*/)
	//@ fold sl.Bytes(b, 0, len(b))
	//@ fold acc(sl.Bytes(s.Raw, 0, len(s.Raw)), R2)
	//@ sl.CombineRange_Bytes(ubuf, 0, len(s.Raw), writePerm)
	//@ fold acc(s.Mem(ubuf), R1)
	return nil
}

// Reverse reverses the path such that it can be used in the reverse direction.
// @ requires  s.Mem(ubuf)
// @ preserves sl.Bytes(ubuf, 0, len(ubuf))
// @ ensures   err == nil ==> typeOf(p) == type[*Raw]
// @ ensures   err == nil ==> p != nil && p != (*Raw)(nil)
// @ ensures   err == nil ==> p.Mem(ubuf)
// @ ensures   err != nil ==> err.ErrorMem()
// @ decreases
func (s *Raw) Reverse( /*@ ghost ubuf []byte @*/ ) (p path.Path, err error) {
	// XXX(shitz): The current implementation is not the most performant, since it parses the entire
	// path first. If this becomes a performance bottleneck, the implementation should be changed to
	// work directly on the raw representation.
	decoded, err := s.ToDecoded( /*@ ubuf @*/ )
	if err != nil {
		return nil, err
	}
	reversed, err := decoded.Reverse( /*@ unfolding s.Mem(ubuf) in s.Raw @*/ )
	if err != nil {
		return nil, err
	}
	//@ unfold s.Mem(ubuf)
	//@ sl.SplitRange_Bytes(ubuf, 0, len(s.Raw), writePerm)
	if err := reversed. /*@ (*Decoded). @*/ SerializeTo(s.Raw /*@, s.Raw @*/); err != nil {
		//@ sl.CombineRange_Bytes(ubuf, 0, len(s.Raw), writePerm)
		return nil, err
	}
	//@ ghost sraw := s.Raw
	//@ fold s.Mem(ubuf)
	//@ s.DowngradePerm(ubuf)
	err = s.DecodeFromBytes( /*@ unfolding s.NonInitMem() in @*/ s.Raw)
	//@ sl.CombineRange_Bytes(ubuf, 0, len(sraw), writePerm)
	//@ ghost if err == nil { s.Widen(sraw, ubuf) }
	return s, err
}

// ToDecoded transforms a scion.Raw to a scion.Decoded.
// @ preserves acc(s.Mem(ubuf), R5)
// @ preserves sl.Bytes(ubuf, 0, len(ubuf))
// @ ensures   err == nil ==> (
// @ 	let newUb := s.RawBufferMem(ubuf) in
// @ 	d.Mem(newUb) &&
// @ 	(old(s.GetBase(ubuf).Valid()) ==> d.GetBase(newUb).Valid()))
// @ ensures   err != nil ==> err.ErrorMem()
// @ decreases
func (s *Raw) ToDecoded( /*@ ghost ubuf []byte @*/ ) (d *Decoded, err error) {
	//@ unfold acc(s.Mem(ubuf), R6)
	//@ unfold acc(s.Base.Mem(), R6)
	//@ ghost var base Base = s.Base
	//@ ghost var pathMeta MetaHdr = s.Base.PathMeta
	//@ ghost validIdxs := s.GetBase(ubuf).Valid()
	//@ assert validIdxs ==> s.Base.PathMeta.InBounds()
	//@ assert validIdxs ==> base.Valid()
	//@ assert s.Raw[:MetaLen] === ubuf[:MetaLen]

	// (VerifiedSCION) In this method, many slice operations are done in two
	// steps to preserve framming information.
	//@ sl.SplitRange_Bytes(ubuf, 0, MetaLen, HalfPerm)
	//@ sl.SplitRange_Bytes(ubuf, 0, MetaLen, HalfPerm)
	// Serialize PathMeta to ensure potential changes are reflected Raw.
	if err := s.PathMeta.SerializeTo(s.Raw[:MetaLen]); err != nil {
		// @ Unreachable()
		return nil, err
	}
	//@ ghost b0 := (unfolding acc(sl.Bytes(s.Raw[:MetaLen], 0, MetaLen), _) in s.Raw[0])
	//@ ghost b1 := (unfolding acc(sl.Bytes(s.Raw[:MetaLen], 0, MetaLen), _) in s.Raw[1])
	//@ ghost b2 := (unfolding acc(sl.Bytes(s.Raw[:MetaLen], 0, MetaLen), _) in s.Raw[2])
	//@ ghost b3 := (unfolding acc(sl.Bytes(s.Raw[:MetaLen], 0, MetaLen), _) in s.Raw[3])
	//@ assert let line := s.PathMeta.SerializedToLine() in binary.BigEndian.PutUint32Spec(b0, b1, b2, b3, line)
	//@ sl.CombineRange_Bytes(ubuf, 0, MetaLen, HalfPerm)
	//@ assert &ubuf[0] == &s.Raw[:MetaLen][0]
	//@ assert &ubuf[1] == &s.Raw[:MetaLen][1]
	//@ assert &ubuf[2] == &s.Raw[:MetaLen][2]
	//@ assert &ubuf[3] == &s.Raw[:MetaLen][3]
	//@ assert b0 == (unfolding acc(sl.Bytes(s.Raw[:MetaLen], 0, MetaLen), _) in ubuf[0])
	//  (VerifiedSCION): for some reason, silicon requires the following line to be able to prove
	//  bX == ubuf[X].
	//@ assert unfolding acc(sl.Bytes(s.Raw[:MetaLen], 0, MetaLen), _) in
	//@ 	(ubuf[0] == (unfolding acc(sl.Bytes(ubuf, 0, len(ubuf)), _) in ubuf[0]))
	//@ sl.CombineRange_Bytes(ubuf, 0, MetaLen, HalfPerm)
	decoded := &Decoded{}
	//@ fold decoded.Base.NonInitMem()
	//@ fold decoded.NonInitMem()
	//@ sl.SplitByIndex_Bytes(ubuf, 0, len(ubuf), len(s.Raw), HalfPerm)
	//@ assert unfolding acc(sl.Bytes(ubuf, 0, len(ubuf)), _) in
	//@ 	(ubuf[0] == (unfolding acc(sl.Bytes(ubuf, 0, len(s.Raw)), _) in ubuf[0]))
	//@ sl.SplitByIndex_Bytes(ubuf, 0, len(ubuf), len(s.Raw), HalfPerm)
	//@ sl.Reslice_Bytes(ubuf, 0, len(s.Raw), HalfPerm)
	//@ assert &ubuf[0] == &ubuf[:len(s.Raw)][0]
	//@ assert &ubuf[1] == &ubuf[:len(s.Raw)][1]
	//@ assert &ubuf[2] == &ubuf[:len(s.Raw)][2]
	//@ assert &ubuf[3] == &ubuf[:len(s.Raw)][3]
	//@ assert unfolding acc(sl.Bytes(ubuf[:len(s.Raw)], 0, len(s.Raw)), _) in
	//@ 	(ubuf[0] == (unfolding acc(sl.Bytes(ubuf, 0, len(s.Raw)), _) in ubuf[0]))
	//@ assert b0 == (unfolding acc(sl.Bytes(ubuf, 0, len(s.Raw)), _) in ubuf[0])
	//@ assert b1 == (unfolding acc(sl.Bytes(ubuf, 0, len(s.Raw)), _) in ubuf[1])
	//@ assert b2 == (unfolding acc(sl.Bytes(ubuf, 0, len(s.Raw)), _) in ubuf[2])
	//@ assert b3 == (unfolding acc(sl.Bytes(ubuf, 0, len(s.Raw)), _) in ubuf[3])
	//@ sl.Reslice_Bytes(ubuf, 0, len(s.Raw), HalfPerm)
	if err := decoded.DecodeFromBytes(s.Raw); err != nil {
		//@ sl.Unslice_Bytes(ubuf, 0, len(s.Raw), writePerm)
		//@ sl.CombineAtIndex_Bytes(ubuf, 0, len(ubuf), len(s.Raw), writePerm)
		//@ fold acc(s.Base.Mem(), R6)
		//@ fold acc(s.Mem(ubuf), R6)
		return nil, err
	}
	//@ ghost lenR := len(s.Raw) // TODO: move to the top and rewrite body
	//@ ghost if validIdxs {
	//@ 	s.PathMeta.SerializeAndDeserializeLemma(b0, b1, b2, b3)
	//@ 	assert pathMeta == decoded.GetMetaHdr(s.Raw)
	//@ 	assert decoded.GetBase(s.Raw).Valid()
	//@ }
	//@ sl.Unslice_Bytes(ubuf, 0, len(s.Raw), HalfPerm)
	//@ sl.Unslice_Bytes(ubuf, 0, len(s.Raw), HalfPerm)
	//@ sl.CombineAtIndex_Bytes(ubuf, 0, len(ubuf), len(s.Raw), HalfPerm)
	//@ sl.CombineAtIndex_Bytes(ubuf, 0, len(ubuf), len(s.Raw), HalfPerm)
	//@ fold acc(s.Base.Mem(), R6)
	//@ fold acc(s.Mem(ubuf), R6)
	return decoded, nil
}

// IncPath increments the path and writes it to the buffer.
// @ requires s.Mem(ubuf)
// @ requires sl.Bytes(ubuf, 0, len(ubuf))
// pres for IO:
// @ requires s.GetBase(ubuf).EqAbsHeader(ubuf)
// @ requires ValidPktMetaHdr(ubuf)
// @ requires s.AbsPkt(ubuf).PathNotFullyTraversed()
// @ requires s.GetBase(ubuf).IsXoverSpec() ==>
// @ 	s.AbsPkt(ubuf).LeftSeg != none[io.Seg]
// @ ensures  sl.Bytes(ubuf, 0, len(ubuf))
// @ ensures  old(unfolding s.Mem(ubuf) in unfolding
// @ 	s.Base.Mem() in (s.NumINF <= 0 || int(s.PathMeta.CurrHF) >= s.NumHops-1)) ==> r != nil
// @ ensures  r == nil ==> s.Mem(ubuf)
// @ ensures  r != nil ==> s.NonInitMem()
// @ ensures  r != nil ==> r.ErrorMem()
// post for IO:
// @ ensures  r == nil ==>
// @ 	s.GetBase(ubuf).EqAbsHeader(ubuf) && ValidPktMetaHdr(ubuf)
// @ ensures  r == nil && old(s.GetBase(ubuf).IsXoverSpec()) ==>
// @ 	s.AbsPkt(ubuf) == AbsXover(old(s.AbsPkt(ubuf)))
// @ ensures  r == nil && !old(s.GetBase(ubuf).IsXoverSpec()) ==>
// @ 	s.AbsPkt(ubuf) == AbsIncPath(old(s.AbsPkt(ubuf)))
// (VerifiedSCION) the following post is technically redundant,
// as it conveys information that could, in principle, be conveyed
// with the previous posts. We should at some point revisit all
// abstractions we use for paths and potentially unify them.
// @ ensures  r == nil ==>
// @ 	s.GetBase(ubuf) == old(s.GetBase(ubuf).IncPathSpec())
// @ decreases
func (s *Raw) IncPath( /*@ ghost ubuf []byte @*/ ) (r error) {
	//@ unfold s.Mem(ubuf)
	//@ reveal ValidPktMetaHdr(ubuf)
	//@ unfold acc(s.Base.Mem(), R56)
	//@ oldCurrInfIdx := int(s.PathMeta.CurrINF)
	//@ oldCurrHfIdx := int(s.PathMeta.CurrHF)
	//@ oldSeg1Len := int(s.PathMeta.SegLen[0])
	//@ oldSeg2Len := int(s.PathMeta.SegLen[1])
	//@ oldSeg3Len := int(s.PathMeta.SegLen[2])
	//@ oldSegs := io.CombineSegLens(oldSeg1Len, oldSeg2Len, oldSeg3Len)
	//@ oldSegLen := oldSegs.LengthOfCurrSeg(oldCurrHfIdx)
	//@ oldPrevSegLen := oldSegs.LengthOfPrevSeg(oldCurrHfIdx)
	//@ oldOffset := HopFieldOffset(s.Base.NumINF, oldPrevSegLen, 0)
	//@ fold acc(s.Base.Mem(), R56)
	if err := s.Base.IncPath(); err != nil {
		//@ fold s.NonInitMem()
		return err
	}
	//@ fold acc(s.Mem(ubuf), HalfPerm)
	//@ sl.SplitRange_Bytes(ubuf, 0, MetaLen, HalfPerm)
	//@ ValidPktMetaHdrSublice(ubuf, MetaLen)
	//@ sl.Reslice_Bytes(ubuf, MetaLen, len(ubuf), HalfPerm)
	//@ tail := ubuf[MetaLen:]
	//@ unfold acc(sl.Bytes(tail, 0, len(tail)), R50)
	//@ oldHfIdxSeg := oldCurrHfIdx-oldPrevSegLen
	//@ WidenCurrSeg(ubuf, oldOffset + MetaLen, oldCurrInfIdx, oldHfIdxSeg, oldSegLen, MetaLen, MetaLen, len(ubuf))
	//@ WidenLeftSeg(ubuf, oldCurrInfIdx + 1, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ WidenMidSeg(ubuf, oldCurrInfIdx + 2, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ WidenRightSeg(ubuf, oldCurrInfIdx - 1, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ LenCurrSeg(tail, oldOffset, oldCurrInfIdx, oldHfIdxSeg, oldSegLen)
	//@ oldAbsPkt := reveal s.AbsPkt(ubuf)
	//@ sl.SplitRange_Bytes(ubuf, 0, MetaLen, HalfPerm)
	//@ unfold acc(s.Base.Mem(), R2)
	err := s.PathMeta.SerializeTo(s.Raw[:MetaLen])
	//@ assert s.Base.Valid()
	//@ assert s.PathMeta.InBounds()
	//@ v := s.Raw[:MetaLen]
	//@ b0 := sl.GetByte(v, 0, MetaLen, 0)
	//@ b1 := sl.GetByte(v, 0, MetaLen, 1)
	//@ b2 := sl.GetByte(v, 0, MetaLen, 2)
	//@ b3 := sl.GetByte(v, 0, MetaLen, 3)
	//@ s.PathMeta.SerializeAndDeserializeLemma(b0, b1, b2, b3)
	//@ assert s.PathMeta.EqAbsHeader(v)
	//@ assert RawBytesToBase(v).Valid()
	//@ sl.CombineRange_Bytes(ubuf, 0, MetaLen, HalfPerm)
	//@ ValidPktMetaHdrSublice(ubuf, MetaLen)
	//@ assert s.EqAbsHeader(ubuf) == s.PathMeta.EqAbsHeader(ubuf)
	//@ assert reveal ValidPktMetaHdr(ubuf)
	//@ currInfIdx := int(s.PathMeta.CurrINF)
	//@ currHfIdx := int(s.PathMeta.CurrHF)
	//@ assert currHfIdx == oldCurrHfIdx + 1

	//@ ghost if(currInfIdx == oldCurrInfIdx) {
	//@ 	IncCurrSeg(tail, oldOffset, oldCurrInfIdx, oldHfIdxSeg, oldSegLen)
	//@ 	WidenCurrSeg(ubuf, oldOffset + MetaLen, oldCurrInfIdx, oldHfIdxSeg + 1,
	//@ 		oldSegLen, MetaLen, MetaLen, len(ubuf))
	//@ 	WidenLeftSeg(ubuf, oldCurrInfIdx + 1, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ 	WidenMidSeg(ubuf, oldCurrInfIdx + 2, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ 	WidenRightSeg(ubuf, oldCurrInfIdx - 1, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ 	assert reveal s.AbsPkt(ubuf) == AbsIncPath(oldAbsPkt)
	//@ } else {
	//@ 	segLen := oldSegs.LengthOfCurrSeg(currHfIdx)
	//@ 	prevSegLen := oldSegs.LengthOfPrevSeg(currHfIdx)
	//@ 	offsetWithHops := HopFieldOffset(s.Base.NumINF, prevSegLen, MetaLen)
	//@ 	hfIdxSeg := currHfIdx-prevSegLen
	//@ 	XoverSegNotNone(tail, oldCurrInfIdx, oldSegs)
	//@ 	XoverCurrSeg(tail, oldCurrInfIdx + 1, oldCurrHfIdx, oldSegs)
	//@ 	XoverLeftSeg(tail, oldCurrInfIdx + 2, oldSegs)
	//@ 	XoverMidSeg(tail, oldCurrInfIdx - 1, oldSegs)
	//@ 	XoverRightSeg(tail, oldCurrInfIdx, oldCurrHfIdx, oldSegs)
	//@ 	WidenCurrSeg(ubuf, offsetWithHops, currInfIdx, hfIdxSeg, segLen, MetaLen, MetaLen, len(ubuf))
	//@ 	WidenLeftSeg(ubuf, currInfIdx + 1, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ 	WidenMidSeg(ubuf, currInfIdx + 2, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ 	WidenRightSeg(ubuf, currInfIdx - 1, oldSegs, MetaLen, MetaLen, len(ubuf))
	//@ 	assert reveal s.AbsPkt(ubuf) == AbsXover(oldAbsPkt)
	//@ }

	//@ fold acc(sl.Bytes(tail, 0, len(tail)), R50)
	//@ sl.Unslice_Bytes(ubuf, MetaLen, len(ubuf), HalfPerm)
	//@ sl.CombineRange_Bytes(ubuf, 0, MetaLen, HalfPerm)
	//@ fold acc(s.Base.Mem(), R2)
	//@ fold acc(s.Mem(ubuf), HalfPerm)
	return err
}

// GetInfoField returns the InfoField at a given index.
// @ requires  0 <= idx
// @ preserves acc(sl.Bytes(ubuf, 0, len(ubuf)), R10)
// @ preserves acc(s.Mem(ubuf), R10)
// @ ensures   (idx < s.GetNumINF(ubuf)) == (err == nil)
// @ ensures   err == nil ==> s.CorrectlyDecodedInfWithIdx(ubuf, idx, ifield)
// @ ensures   err != nil ==> err.ErrorMem()
// @ decreases
func (s *Raw) GetInfoField(idx int /*@, ghost ubuf []byte @*/) (ifield path.InfoField, err error) {
	//@ unfold acc(s.Mem(ubuf), R11)
	//@ unfold acc(s.Base.Mem(), R12)
	if idx >= s.NumINF {
		e := serrors.New("InfoField index out of bounds", "max", s.NumINF-1, "actual", idx)
		//@ fold acc(s.Base.Mem(), R12)
		//@ fold acc(s.Mem(ubuf), R11)
		return path.InfoField{}, e
	}
	//@ fold acc(s.Base.Mem(), R12)
	infOffset := MetaLen + idx*path.InfoLen
	info /*@@@*/ := path.InfoField{}
	//@ sl.SplitRange_Bytes(ubuf, infOffset, infOffset+path.InfoLen, R20)
	if err := info.DecodeFromBytes(s.Raw[infOffset : infOffset+path.InfoLen]); err != nil {
		//@ Unreachable()
		return path.InfoField{}, err
	}
	//@ sl.CombineRange_Bytes(ubuf, infOffset, infOffset+path.InfoLen, R21)
	//@ path.BytesToAbsInfoFieldOffsetEq(ubuf, infOffset)
	//@ sl.CombineRange_Bytes(ubuf, infOffset, infOffset+path.InfoLen, R21)
	//@ fold acc(s.Mem(ubuf), R11)
	//@ assert reveal s.CorrectlyDecodedInfWithIdx(ubuf, idx, info)
	return info, nil
}

// GetCurrentInfoField is a convenience method that returns the current hop field pointed to by the
// CurrINF index in the path meta header.
// @ preserves acc(s.Mem(ubuf), R8)
// @ preserves acc(sl.Bytes(ubuf, 0, len(ubuf)), R9)
// @ ensures   (r == nil) == s.GetBase(ubuf).ValidCurrInfSpec()
// @ ensures   r == nil ==> s.CorrectlyDecodedInf(ubuf, res)
// @ ensures   r != nil ==> r.ErrorMem()
// @ decreases
func (s *Raw) GetCurrentInfoField( /*@ ghost ubuf []byte @*/ ) (res path.InfoField, r error) {
	//@ unfold acc(s.Mem(ubuf), R9)
	//@ unfold acc(s.Base.Mem(), R10)
	idx := int(s.PathMeta.CurrINF)
	// (VerifiedSCION) Cannot assert bounds of uint:
	// https://github.com/viperproject/gobra/issues/192
	//@ assume 0 <= idx
	//@ fold acc(s.Base.Mem(), R10)
	//@ fold acc(s.Mem(ubuf), R9)
	//@ assert forall res path.InfoField :: { s.CorrectlyDecodedInf(ubuf, res) } s.GetBase(ubuf).ValidCurrInfSpec() ==>
	//@ 	reveal s.CorrectlyDecodedInf(ubuf, res) == reveal s.CorrectlyDecodedInfWithIdx(ubuf, idx, res)
	return s.GetInfoField(idx /*@, ubuf @*/)
}

// SetInfoField updates the InfoField at a given index.
// @ requires 0 <= idx
// @ requires sl.Bytes(ubuf, 0, len(ubuf))
// @ requires acc(s.Mem(ubuf), R20)
// pres for IO:
// @ requires ValidPktMetaHdr(ubuf)
// @ requires s.GetBase(ubuf).EqAbsHeader(ubuf)
// @ ensures  acc(s.Mem(ubuf), R20)
// @ ensures  sl.Bytes(ubuf, 0, len(ubuf))
// @ ensures  r != nil ==> r.ErrorMem()
// posts for IO:
// @ ensures  r == nil ==>
// @ 	ValidPktMetaHdr(ubuf) && s.GetBase(ubuf).EqAbsHeader(ubuf)
// @ ensures  r == nil && idx == int(old(s.GetCurrINF(ubuf))) ==>
// @ 	let oldPkt := old(s.AbsPkt(ubuf)) in
// @ 	let newPkt := oldPkt.UpdateInfoField(info.ToAbsInfoField()) in
// @ 	s.AbsPkt(ubuf) == newPkt
// @ decreases
// @ #backend[exhaleMode(1)]
func (s *Raw) SetInfoField(info path.InfoField, idx int /*@, ghost ubuf []byte @*/) (r error) {
	//@ share info
	//@ reveal ValidPktMetaHdr(ubuf)
	//@ unfold acc(s.Mem(ubuf), R50)
	//@ unfold acc(s.Base.Mem(), R50)
	//@ currInfIdx := int(s.PathMeta.CurrINF)
	//@ currHfIdx := int(s.PathMeta.CurrHF)
	//@ seg1Len := int(s.PathMeta.SegLen[0])
	//@ seg2Len := int(s.PathMeta.SegLen[1])
	//@ seg3Len := int(s.PathMeta.SegLen[2])
	//@ segLens := io.CombineSegLens(seg1Len, seg2Len, seg3Len)
	//@ segLen := segLens.LengthOfCurrSeg(currHfIdx)
	//@ prevSegLen := segLens.LengthOfPrevSeg(currHfIdx)
	//@ offset := HopFieldOffset(s.Base.NumINF, prevSegLen, MetaLen)
	//@ hopfieldOffset := MetaLen + s.NumINF*path.InfoLen
	if idx >= s.NumINF {
		err := serrors.New("InfoField index out of bounds", "max", s.NumINF-1, "actual", idx)
		//@ fold acc(s.Base.Mem(), R50)
		//@ fold acc(s.Mem(ubuf), R50)
		return err
	}
	infOffset := MetaLen + idx*path.InfoLen

	//@ SliceBytesIntoInfoFields(ubuf, s.NumINF, segLens, HalfPerm)
	//@ SliceBytesIntoSegments(ubuf, segLens, R40)

	//@ ValidPktMetaHdrSublice(ubuf, MetaLen)
	//@ oldInfo := path.BytesToAbsInfoField(ubuf[infOffset : infOffset+path.InfoLen], 0)
	//@ newInfo := info.ToAbsInfoField()
	//@ hfIdxSeg := currHfIdx-prevSegLen
	//@ hopfields := ubuf[offset:offset + segLen*path.HopLen]
	//@ ghost if idx == currInfIdx {
	//@ 	CurrSegEquality(ubuf, offset, currInfIdx, hfIdxSeg, segLen)
	//@ 	LeftSegEquality(ubuf, currInfIdx+1, segLens)
	//@ 	MidSegEquality(ubuf, currInfIdx+2, segLens)
	//@ 	RightSegEquality(ubuf, currInfIdx-1, segLens)
	//@ }
	//@ reveal s.AbsPkt(ubuf)
	//@ sl.SplitRange_Bytes(ubuf[:hopfieldOffset], infOffset, infOffset+path.InfoLen, R40)
	//@ sl.SplitRange_Bytes(ubuf, infOffset, infOffset+path.InfoLen, HalfPerm-R40)
	ret := info.SerializeTo(s.Raw[infOffset : infOffset+path.InfoLen])
	//@ sl.CombineRange_Bytes(ubuf[:hopfieldOffset], infOffset, infOffset+path.InfoLen, R40)
	//@ sl.CombineRange_Bytes(ubuf, infOffset, infOffset+path.InfoLen, HalfPerm-R40)
	//@ ValidPktMetaHdrSublice(ubuf, MetaLen)
	//@ assert reveal ValidPktMetaHdr(ubuf)
	//@ ghost if idx == currInfIdx {
	//@ 	CurrSegEquality(ubuf, offset, currInfIdx, hfIdxSeg, segLen)
	//@ 	UpdateCurrSegInfo(hopfields, hfIdxSeg, segLen, oldInfo, newInfo)
	//@ 	LeftSegEquality(ubuf, currInfIdx+1, segLens)
	//@ 	MidSegEquality(ubuf, currInfIdx+2, segLens)
	//@ 	RightSegEquality(ubuf, currInfIdx-1, segLens)
	//@ 	reveal s.AbsPkt(ubuf)
	//@ }
	//@ CombineBytesFromSegments(ubuf, segLens, R40)
	//@ CombineBytesFromInfoFields(ubuf, s.NumINF, segLens, HalfPerm)
	//@ fold acc(s.Base.Mem(), R50)
	//@ fold acc(s.Mem(ubuf), R50)
	return ret
}

// GetHopField returns the HopField at a given index.
// @ requires  0 <= idx
// @ preserves acc(sl.Bytes(ubuf, 0, len(ubuf)), R10)
// @ preserves acc(s.Mem(ubuf), R10)
// @ ensures   (idx < s.GetNumHops(ubuf)) == (r == nil)
// @ ensures   r == nil ==> s.CorrectlyDecodedHfWithIdx(ubuf, idx, res)
// @ ensures   r != nil ==> r.ErrorMem()
// @ decreases
func (s *Raw) GetHopField(idx int /*@, ghost ubuf []byte @*/) (res path.HopField, r error) {
	//@ unfold acc(s.Mem(ubuf), R11)
	//@ unfold acc(s.Base.Mem(), R12)
	if idx >= s.NumHops {
		err := serrors.New("HopField index out of bounds", "max", s.NumHops-1, "actual", idx)
		//@ fold acc(s.Base.Mem(), R12)
		//@ fold acc(s.Mem(ubuf), R11)
		return path.HopField{}, err
	}
	hopOffset := MetaLen + s.NumINF*path.InfoLen + idx*path.HopLen
	//@ fold acc(s.Base.Mem(), R12)
	hop /*@@@*/ := path.HopField{}
	//@ sl.SplitRange_Bytes(ubuf, hopOffset, hopOffset+path.HopLen, R20)
	if err := hop.DecodeFromBytes(s.Raw[hopOffset : hopOffset+path.HopLen]); err != nil {
		//@ Unreachable()
		return path.HopField{}, err
	}
	//@ unfold hop.Mem()
	//@ sl.CombineRange_Bytes(ubuf, hopOffset, hopOffset+path.HopLen, R21)
	//@ path.BytesToAbsHopFieldOffsetEq(ubuf, hopOffset)
	//@ sl.CombineRange_Bytes(ubuf, hopOffset, hopOffset+path.HopLen, R21)
	//@ fold acc(s.Mem(ubuf), R11)
	//@ assert reveal s.CorrectlyDecodedHfWithIdx(ubuf, idx, hop)
	return hop, nil
}

// GetCurrentHopField is a convenience method that returns the current hop field pointed to by the
// CurrHF index in the path meta header.
// @ preserves acc(s.Mem(ubuf), R8)
// @ preserves acc(sl.Bytes(ubuf, 0, len(ubuf)), R9)
// @ ensures   (r == nil) == s.GetBase(ubuf).ValidCurrHfSpec()
// @ ensures   r == nil ==> s.CorrectlyDecodedHf(ubuf, res)
// @ ensures   r != nil ==> r.ErrorMem()
// @ decreases
func (s *Raw) GetCurrentHopField( /*@ ghost ubuf []byte @*/ ) (res path.HopField, r error) {
	//@ unfold acc(s.Mem(ubuf), R9)
	//@ unfold acc(s.Base.Mem(), R10)
	idx := int(s.PathMeta.CurrHF)
	// (VerifiedSCION) Cannot assert bounds of uint:
	// https://github.com/viperproject/gobra/issues/192
	//@ assume 0 <= idx
	//@ fold acc(s.Base.Mem(), R10)
	//@ fold acc(s.Mem(ubuf), R9)
	//@ assert forall res path.HopField :: { s.CorrectlyDecodedHf(ubuf, res) } s.GetBase(ubuf).ValidCurrHfSpec() ==>
	//@ 	reveal s.CorrectlyDecodedHf(ubuf, res) == reveal s.CorrectlyDecodedHfWithIdx(ubuf, idx, res)
	return s.GetHopField(idx /*@, ubuf @*/)
}

// SetHopField updates the HopField at a given index.
// @ requires  0 <= idx
// @ requires  acc(s.Mem(ubuf), R20)
// @ requires  sl.Bytes(ubuf, 0, len(ubuf))
// pres for IO:
// @ requires ValidPktMetaHdr(ubuf)
// @ requires s.GetBase(ubuf).EqAbsHeader(ubuf)
// @ requires s.AbsPkt(ubuf).PathNotFullyTraversed()
// @ ensures  acc(s.Mem(ubuf), R20)
// @ ensures  sl.Bytes(ubuf, 0, len(ubuf))
// @ ensures  r != nil ==> r.ErrorMem()
// posts for IO:
// @ ensures  r == nil ==>
// @ 	ValidPktMetaHdr(ubuf) &&
// @	s.GetBase(ubuf).EqAbsHeader(ubuf)
// @ ensures  r == nil && idx == int(old(s.GetCurrHF(ubuf))) ==>
// @ 	let oldPkt := old(s.AbsPkt(ubuf)) in
// @ 	let newPkt := oldPkt.UpdateHopField(hop.Abs()) in
// @ 	s.AbsPkt(ubuf) == newPkt
// @ decreases
// @ #backend[exhaleMode(1)]
func (s *Raw) SetHopField(hop path.HopField, idx int /*@, ghost ubuf []byte @*/) (r error) {
	// (VerifiedSCION) Due to an incompleteness (https://github.com/viperproject/gobra/issues/770),
	// we introduce a temporary variable to be able to call `path.AbsMacArrayCongruence()`.
	tmpHopField /*@@@*/ := hop
	//@ path.AbsMacArrayCongruence(hop.Mac, tmpHopField.Mac)
	// (VerifiedSCION) Cannot assert bounds of uint:
	// https://github.com/viperproject/gobra/issues/192
	//@ assume 0 <= tmpHopField.ConsIngress && 0 <= tmpHopField.ConsEgress
	//@ fold acc(tmpHopField.Mem(), R9)
	//@ reveal ValidPktMetaHdr(ubuf)
	//@ unfold acc(s.Mem(ubuf), R50)
	//@ unfold acc(s.Base.Mem(), R50)
	//@ ghost currInfIdx := int(s.PathMeta.CurrINF)
	//@ ghost currHfIdx := int(s.PathMeta.CurrHF)
	//@ ghost seg1Len := int(s.PathMeta.SegLen[0])
	//@ ghost seg2Len := int(s.PathMeta.SegLen[1])
	//@ ghost seg3Len := int(s.PathMeta.SegLen[2])
	//@ ghost segLens := io.CombineSegLens(seg1Len, seg2Len, seg3Len)
	//@ ghost segLen := segLens.LengthOfCurrSeg(idx)
	//@ ghost prevSegLen := segLens.LengthOfPrevSeg(idx)
	//@ ghost offset := HopFieldOffset(s.Base.NumINF, prevSegLen, MetaLen)
	//@ ghost hopfieldOffset := MetaLen + s.NumINF*path.InfoLen
	if idx >= s.NumHops {
		// (VerifiedSCION) introduced `err`
		err := serrors.New("HopField index out of bounds", "max", s.NumHops-1, "actual", idx)
		//@ fold acc(s.Base.Mem(), R50)
		//@ fold acc(s.Mem(ubuf), R50)
		return err
	}
	hopOffset := MetaLen + s.NumINF*path.InfoLen + idx*path.HopLen

	//@ SliceBytesIntoSegments(ubuf, segLens, HalfPerm)
	//@ SliceBytesIntoInfoFields(ubuf[:hopfieldOffset], s.NumINF, segLens, R40)

	//@ ValidPktMetaHdrSublice(ubuf, MetaLen)
	//@ ghost inf := path.BytesToAbsInfoField(InfofieldByteSlice(ubuf, currInfIdx), 0)
	//@ ghost hfIdxSeg := idx-prevSegLen
	//@ ghost currHopfields := HopfieldsByteSlice(ubuf, currInfIdx, segLens)
	//@ ghost if idx == currHfIdx {
	//@ 	CurrSegEquality(ubuf, offset, currInfIdx, hfIdxSeg, segLen)
	//@ 	LeftSegEquality(ubuf, currInfIdx+1, segLens)
	//@ 	MidSegEquality(ubuf, currInfIdx+2, segLens)
	//@ 	RightSegEquality(ubuf, currInfIdx-1, segLens)
	//@ 	reveal s.AbsPkt(ubuf)
	//@ 	SplitHopfields(currHopfields, hfIdxSeg, segLen, R0)
	//@ 	EstablishBytesStoreCurrSeg(currHopfields, hfIdxSeg, segLen, inf)
	//@ 	SplitHopfields(currHopfields, hfIdxSeg, segLen, R0)
	//@ } else {
	//@ 	sl.SplitRange_Bytes(ubuf[offset:offset+segLen*path.HopLen], hfIdxSeg*path.HopLen,
	//@ 		(hfIdxSeg+1)*path.HopLen, HalfPerm)
	//@ }
	//@ sl.SplitRange_Bytes(ubuf, hopOffset, hopOffset+path.HopLen, HalfPerm)
	ret := tmpHopField.SerializeTo(s.Raw[hopOffset : hopOffset+path.HopLen])
	//@ sl.CombineRange_Bytes(ubuf, hopOffset, hopOffset+path.HopLen, HalfPerm)
	//@ ValidPktMetaHdrSublice(ubuf, MetaLen)
	//@ assert reveal ValidPktMetaHdr(ubuf)
	//@ ghost if idx == currHfIdx {
	//@ 	CombineHopfields(currHopfields, hfIdxSeg, segLen, R0)
	//@ 	EstablishBytesStoreCurrSeg(currHopfields, hfIdxSeg, segLen, inf)
	//@ 	CombineHopfields(currHopfields, hfIdxSeg, segLen, R0)
	//@ 	CurrSegEquality(ubuf, offset, currInfIdx, hfIdxSeg, segLen)
	//@ 	LeftSegEquality(ubuf, currInfIdx+1, segLens)
	//@ 	MidSegEquality(ubuf, currInfIdx+2, segLens)
	//@ 	RightSegEquality(ubuf, currInfIdx-1, segLens)
	//@ 	reveal s.AbsPkt(ubuf)
	//@ 	assert s.AbsPkt(ubuf).CurrSeg.Future ==
	//@ 		seq[io.HF]{tmpHopField.Abs()} ++ old(s.AbsPkt(ubuf).CurrSeg.Future[1:])
	//@ } else {
	//@ 	sl.CombineRange_Bytes(ubuf[offset:offset+segLen*path.HopLen], hfIdxSeg*path.HopLen,
	//@ 		(hfIdxSeg+1)*path.HopLen, HalfPerm)
	//@ }
	//@ CombineBytesFromInfoFields(ubuf[:hopfieldOffset], s.NumINF, segLens, R40)
	//@ CombineBytesFromSegments(ubuf, segLens, HalfPerm)
	//@ fold acc(s.Base.Mem(), R50)
	//@ fold acc(s.Mem(ubuf), R50)
	return ret
}

// IsFirstHop returns whether the current hop is the first hop on the path.
// @ pure
// @ requires  s.Mem(ubuf)
// @ decreases
func (s *Raw) IsFirstHop( /*@ ghost ubuf []byte @*/ ) bool {
	return /*@ unfolding s.Mem(ubuf) in (unfolding s.Base.Mem() in @*/ s.PathMeta.CurrHF == 0 /*@ ) @*/
}

// IsPenultimateHop returns whether the current hop is the penultimate hop on the path.
// @ preserves acc(s.Mem(ubuf), R20)
// @ decreases
func (s *Raw) IsPenultimateHop( /*@ ghost ubuf []byte @*/ ) bool {
	//@ unfold acc(s.Mem(ubuf), R20)
	//@ defer  fold acc(s.Mem(ubuf), R20)
	//@ unfold acc(s.Base.Mem(), R20)
	//@ defer  fold acc(s.Base.Mem(), R20)
	return int(s.PathMeta.CurrHF) == (s.NumHops - 2)
}

// IsLastHop returns whether the current hop is the last hop on the path.
// @ preserves acc(s.Mem(ubuf), R40)
// @ ensures   res == s.IsLastHopSpec(ubuf)
// @ decreases
func (s *Raw) IsLastHop( /*@ ghost ubuf []byte @*/ ) (res bool) {
	//@ unfold acc(s.Mem(ubuf), R40)
	//@ defer  fold acc(s.Mem(ubuf), R40)
	//@ unfold acc(s.Base.Mem(), R40)
	//@ defer  fold acc(s.Base.Mem(), R40)
	return int(s.PathMeta.CurrHF) == (s.NumHops - 1)
}

// CurrINFMatchesCurrHF returns whether the the path's current hopfield
// is in the path's current segment.
// @ preserves acc(s.Mem(ub), R40)
// @ ensures   res == s.GetBase(ub).CurrInfMatchesCurrHFSpec()
// @ decreases
func (s *Raw) CurrINFMatchesCurrHF( /*@ ghost ub []byte @*/ ) (res bool) {
	// @ unfold acc(s.Mem(ub), R40)
	// @ defer fold acc(s.Mem(ub), R40)
	// @ unfold acc(s.Base.Mem(), R40)
	// @ defer fold acc(s.Base.Mem(), R40)
	return s.PathMeta.CurrINF == s.infIndexForHF(s.PathMeta.CurrHF)
}
