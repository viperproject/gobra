import org.bitbucket.inkytonik.kiama.util.StringSource
import viper.gobra.frontend.Parser

val res = Parser.parseExpr(StringSource("\"\\\"\""))
res
res.isRight
