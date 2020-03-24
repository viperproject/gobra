package tree

type Tree struct {
  Left  *Tree
  Value int
  Right *Tree
}

pred tree(self *Tree) {
  acc(self.Left) && acc(self.Value) && acc(self.Right) &&
  (self.Left != nil ==> tree(self.Left)) &&
  (self.Right != nil ==> tree(self.Right))
}

requires self != nil ==> tree(self)
ensures  self != nil ==> tree(self)
func (self *Tree) Contains(v int) (res bool) {
  if self == nil { res = false
  } else {
    unfold tree(self)
    if self.Value == v { res = true } else {
      res = (self.Left).Contains(v) || (self.Right).Contains(v)
    }
    fold tree(self)
  }
}