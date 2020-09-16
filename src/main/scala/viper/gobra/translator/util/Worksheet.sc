import TypePatterns.::
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context

object TypePatterns {


  object :: {
    def unapply(arg: in.Expr): Option[(in.Expr, in.Type)] = Some(arg, arg.typ)
  }


}

def foo(x: in.Expr): Unit  = {
  x match {
    case ::(a,b) => ???
    case a :: b =>
  }
}