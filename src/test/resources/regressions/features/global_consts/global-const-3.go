package pkg

type cell struct{
		val int;
};

//:: ExpectedOutput(type_error)
const Answer = &cell{42}
