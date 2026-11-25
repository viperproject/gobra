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

package onehop

import (
	"github.com/scionproto/scion/pkg/private/serrors"
	"github.com/scionproto/scion/pkg/slayers/path"
	"github.com/scionproto/scion/pkg/slayers/path/scion"
	//@ . "github.com/scionproto/scion/verification/utils/definitions"
	//@ sl "github.com/scionproto/scion/verification/utils/slices"
)

// PathLen is the length of a serialized one hop path in bytes.
const PathLen = path.InfoLen + 2*path.HopLen

const PathType path.Type = 2

// @ requires path.PkgMem()
// @ requires path.RegisteredTypes().DoesNotContain(int64(PathType))
// @ ensures  path.PkgMem()
// @ ensures  path.RegisteredTypes().Contains(int64(PathType))
// @ decreases
func RegisterPath() {
	tmp := path.Metadata{
		Type: PathType,
		Desc: "OneHop",
		New:
		//@ ensures p.NonInitMem()
		//@ ensures p != nil
		//@ decreases
		func /*@ newPath @*/ () (p path.Path) {
			onehopTmp := &Path{}
			//@ fold onehopTmp.NonInitMem()
			return onehopTmp
		},
	}
	//@ proof tmp.New implements path.NewPathSpec {
	//@ 	return tmp.New() as newPath
	//@ }
	path.RegisterPath(tmp)
}

// Path encodes a one hop path. A one hop path is a special path that is created by a SCION router
// in the first AS and completed by a SCION router in the second AS. It is used during beaconing
// when there is not yet any other path segment available.
type Path struct {
	Info      path.InfoField
	FirstHop  path.HopField
	SecondHop path.HopField
}

// @ requires  o.NonInitMem()
// @ preserves acc(sl.Bytes(data, 0, len(data)), R42)
// @ ensures   (len(data) >= PathLen) == (r == nil)
// @ ensures   r == nil ==> o.Mem(data)
// @ ensures   r != nil ==> o.NonInitMem()
// @ ensures   r != nil ==> r.ErrorMem()
// @ decreases
func (o *Path) DecodeFromBytes(data []byte) (r error) {
	if len(data) < PathLen {
		return serrors.New("buffer too short for OneHop path", "expected", int(PathLen), "actual",
			len(data))
	}
	offset := 0
	//@ unfold o.NonInitMem()
	//@ sl.SplitRange_Bytes(data, 0, path.InfoLen, R42)
	if err := o.Info.DecodeFromBytes(data[:path.InfoLen]); err != nil {
		// @ Unreachable()
		return err
	}
	//@ sl.CombineRange_Bytes(data,0,  path.InfoLen, R42)
	offset += path.InfoLen
	//@ sl.SplitRange_Bytes(data, offset, offset+path.HopLen, R42)
	if err := o.FirstHop.DecodeFromBytes(data[offset : offset+path.HopLen]); err != nil {
		// @ Unreachable()
		return err
	}
	//@ sl.CombineRange_Bytes(data, offset, offset+path.HopLen, R42)
	offset += path.HopLen
	//@ sl.SplitRange_Bytes(data, offset, offset+path.HopLen, R42)
	r = o.SecondHop.DecodeFromBytes(data[offset : offset+path.HopLen])
	//@ sl.CombineRange_Bytes(data, offset, offset+path.HopLen, R42)
	//@ ghost if r == nil { fold o.Mem(data) } else { fold o.NonInitMem() }
	return r
}

// @ preserves acc(o.Mem(ubuf), R1)
// @ preserves acc(sl.Bytes(ubuf, 0, len(ubuf)), R1)
// @ preserves sl.Bytes(b, 0, len(b))
// @ ensures   (len(b) >= PathLen) == (err == nil)
// @ ensures   err != nil ==> err.ErrorMem()
// @ ensures   err == nil ==> o.LenSpec(ubuf) <= len(b)
// @ decreases
func (o *Path) SerializeTo(b []byte /*@, ubuf []byte @*/) (err error) {
	if len(b) < PathLen {
		return serrors.New("buffer too short for OneHop path", "expected", int(PathLen), "actual",
			int(len(b)))
	}
	offset := 0
	//@ unfold acc(o.Mem(ubuf), R1)
	//@ sl.SplitRange_Bytes(b, 0, offset+path.InfoLen, writePerm)
	if err := o.Info.SerializeTo(b[:offset+path.InfoLen]); err != nil {
		//@ sl.CombineRange_Bytes(b, 0, offset+path.InfoLen, writePerm)
		return err
	}
	//@ sl.CombineRange_Bytes(b, 0, offset+path.InfoLen, writePerm)
	offset += path.InfoLen
	//@ sl.SplitRange_Bytes(b, offset, offset+path.HopLen, writePerm)
	if err := o.FirstHop.SerializeTo(b[offset : offset+path.HopLen]); err != nil {
		//@ sl.CombineRange_Bytes(b, offset, offset+path.HopLen, writePerm)
		return err
	}
	//@ sl.CombineRange_Bytes(b, offset, offset+path.HopLen, writePerm)
	offset += path.HopLen
	//@ sl.SplitRange_Bytes(b, offset, offset+path.HopLen, writePerm)
	err = o.SecondHop.SerializeTo(b[offset : offset+path.HopLen])
	//@ sl.CombineRange_Bytes(b, offset, offset+path.HopLen, writePerm)
	//@ fold acc(o.Mem(ubuf), R1)
	return err
}

// ToSCIONDecoded converts the one hop path in to a normal SCION path in the
// decoded format.
// @ preserves o.Mem(ubuf)
// @ preserves sl.Bytes(ubuf, 0, len(ubuf))
// @ ensures   err == nil ==> (sd != nil && sd.Mem(ubuf))
// @ ensures   err != nil ==> err.ErrorMem()
// @ decreases
func (o *Path) ToSCIONDecoded( /*@ ghost ubuf []byte @*/ ) (sd *scion.Decoded, err error) {
	//@ unfold acc(o.Mem(ubuf), R1)
	//@ unfold acc(o.SecondHop.Mem(), R10)
	if o.SecondHop.ConsIngress == 0 {
		//@ fold acc(o.SecondHop.Mem(), R10)
		//@ fold acc(o.Mem(ubuf), R1)
		return nil, serrors.New("incomplete path can't be converted")
	}
	//@ unfold acc(o.FirstHop.Mem(), R10)
	p := &scion.Decoded{
		Base: scion.Base{
			PathMeta: scion.MetaHdr{
				SegLen: [3]uint8{2, 0, 0},
			},
			NumHops: 2,
			NumINF:  1,
		},
		InfoFields: []path.InfoField{
			{
				ConsDir:   true,
				SegID:     o.Info.SegID,
				Timestamp: o.Info.Timestamp,
			},
		},
		HopFields: []path.HopField{
			{
				IngressRouterAlert: o.FirstHop.IngressRouterAlert,
				EgressRouterAlert:  o.FirstHop.EgressRouterAlert,
				ConsIngress:        o.FirstHop.ConsIngress,
				ConsEgress:         o.FirstHop.ConsEgress,
				ExpTime:            o.FirstHop.ExpTime,
				Mac:                o.FirstHop.Mac,
			},
			{
				IngressRouterAlert: o.SecondHop.IngressRouterAlert,
				EgressRouterAlert:  o.SecondHop.EgressRouterAlert,
				ConsIngress:        o.SecondHop.ConsIngress,
				ConsEgress:         o.SecondHop.ConsEgress,
				ExpTime:            o.SecondHop.ExpTime,
				Mac:                o.SecondHop.Mac,
			},
		},
	}
	//@ fold acc(o.FirstHop.Mem(), R10)
	//@ fold acc(o.SecondHop.Mem(), R10)
	//@ fold acc(o.Mem(ubuf), R1)
	//@ assert forall i int :: { &p.InfoFields[i] } 0 <= i && i < len(p.InfoFields) ==> acc(&p.InfoFields[i])
	//@ assert forall i int :: { &p.HopFields[i] } 0 <= i && i < len(p.HopFields) ==> acc(&p.HopFields[i])
	//@ fold p.Base.Mem()
	//@ fold p.HopFields[0].Mem()
	//@ fold p.HopFields[1].Mem()
	//@ assert forall i int :: { &p.InfoFields[i] } 0 <= i && i < len(p.InfoFields) ==> acc(&p.InfoFields[i])
	//@ assert forall i int :: { &p.HopFields[i] } 0 <= i && i < len(p.HopFields) ==> p.HopFields[i].Mem()
	//@ fold p.Mem(ubuf)
	return p, nil
}

// Reverse a OneHop path that returns a reversed SCION path.
// @ requires o.Mem(ubuf)
// @ preserves sl.Bytes(ubuf, 0, len(ubuf))
// @ ensures err == nil ==> p != nil
// @ ensures err == nil ==> p.Mem(ubuf)
// @ ensures err == nil ==> typeOf(p) == type[*scion.Decoded]
// @ ensures err != nil ==> err.ErrorMem()
// @ decreases
func (o *Path) Reverse( /*@ ghost ubuf []byte @*/ ) (p path.Path, err error) {
	sp, err := o.ToSCIONDecoded( /*@ ubuf @*/ )
	if err != nil {
		return nil, serrors.WrapStr("converting to scion path", err)
	}
	// increment the path, since we are at the receiver side.
	if err := sp.IncPath( /*@ ubuf @*/ ); err != nil {
		return nil, serrors.WrapStr("incrementing path", err)
	}
	return sp.Reverse( /*@ ubuf @*/ )
}

// @ ensures l == o.LenSpec(ubuf)
// @ decreases
func (o *Path) Len( /*@ ghost ubuf []byte @*/ ) (l int) {
	return PathLen
}

// @ pure
// @ ensures t == PathType
// @ decreases
func (o *Path) Type( /*@ ghost ubuf []byte @*/ ) (t path.Type) {
	return PathType
}
