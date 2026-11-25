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

package empty

import (
	"github.com/scionproto/scion/pkg/private/serrors"
	"github.com/scionproto/scion/pkg/slayers/path"
)

// PathLen is the length of a serialized empty path in bytes.
const PathLen = 0

const PathType path.Type = 0

// @ requires path.PkgMem()
// @ requires path.RegisteredTypes().DoesNotContain(int64(PathType))
// @ ensures  path.PkgMem()
// @ ensures  path.RegisteredTypes().Contains(int64(PathType))
// @ decreases
func RegisterPath() {
	tmp := path.Metadata{
		Type: PathType,
		Desc: "Empty",
		New:
		//@ ensures p.NonInitMem()
		//@ ensures p != nil
		//@ decreases
		func /*@ newPath @*/ () (p path.Path) {
			emptyTmp := Path{}
			//@ fold emptyTmp.NonInitMem()
			return emptyTmp
		},
	}
	//@ proof tmp.New implements path.NewPathSpec {
	//@ 	return tmp.New() as newPath
	//@ }
	path.RegisterPath(tmp)
}

// Path encodes an empty path. An empty path is a special path that takes zero
// bytes on the wire and is used for AS internal communication.
type Path struct{}

// @ ensures  len(r) == 0 ==> (e == nil && o.Mem(r))
// @ ensures  len(r) != 0 ==> (e != nil && e.ErrorMem() && o.NonInitMem())
// @ decreases
func (o Path) DecodeFromBytes(r []byte) (e error) {
	if len(r) != 0 {
		//@ fold o.NonInitMem()
		return serrors.New("decoding an empty path", "len", len(r))
	}
	//@ fold o.Mem(r)
	return nil
}

// @ ensures e == nil
// @ decreases
func (o Path) SerializeTo(b []byte /*@, ub []byte @*/) (e error) {
	return nil
}

// @ requires o.Mem(ub)
// @ ensures  p == o
// @ ensures  p.Mem(ub)
// @ ensures  e == nil
// @ decreases
func (o Path) Reverse( /*@ ub []byte @*/ ) (p path.Path, e error) {
	return o, nil
}

// @ ensures r == o.LenSpec(ub)
// @ decreases
func (o Path) Len( /*@ ub []byte @*/ ) (r int) {
	return PathLen
}

// @ pure
// @ ensures r == PathType
// @ decreases
func (o Path) Type( /*@ ub []byte @*/ ) (r path.Type) {
	return PathType
}
