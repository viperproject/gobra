package list;

type node struct {
  value int;
  next *node;
};

pred list(ptr *node) {
  ptr != nil ==> acc(ptr.value) && acc(ptr.next) && list(ptr.next)
};

ghost
requires list(ptr);
ensures list(ptr);
pure func first(ptr *node) (res int) {
  return unfolding list(ptr) in (ptr == nil ? -1 : ptr.value);
};
