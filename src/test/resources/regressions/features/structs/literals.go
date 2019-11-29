
package pkg;

type cell struct{
		val int;
};

type list struct{
		val int;
		next *list;
};





func client() {
    x := &list{42, nil};

};