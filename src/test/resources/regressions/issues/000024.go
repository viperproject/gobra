type Cell struct {
    a int;
    b int;
};

func test1(c Cell) () {
    x := c.a;
    p := &(c.a);
    *p = x+1;
    y := c.a;
    assert x != y;
};

func test2() (c Cell) {
    x := c.a;
    p := &(c.a);
    *p = x+1;
    y := c.a;
    assert x != y;
};