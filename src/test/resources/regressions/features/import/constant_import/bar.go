package bar

const Answer = 42
const SimpleUntypedBoolConst = true
const SimpleTypedBoolConst bool = true
const BoolExprConst = SimpleUntypedBoolConst && SimpleTypedBoolConst
const DoesDeclOrderMatter bool = TypedIntConst < 43
const TypedIntConst int = 42
const (
    multiConst1 = 1
    multiConst2 = false
    multiConst3 int = 3
)
