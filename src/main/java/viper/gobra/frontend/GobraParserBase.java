// Imported to Gobra from https://github.com/antlr/grammars-v4/blob/4c06ad8cc8130931c75ca0b17cbc1453f3830cd2/golang

package viper.gobra.frontend;
import org.antlr.v4.runtime.*;

/**
 * All parser methods that are used in grammar (p, prev, notLineTerminator, etc.)
 * should start with lower case char similar to parser rules.
 */
public abstract class GobraParserBase extends org.antlr.v4.runtime.Parser
{
    protected GobraParserBase(TokenStream input) {
        super(input);
    }

    /**
     * Returns true if the current Token is a closing bracket (")", "}" or "!>")
     */
    protected boolean closingBracket()
    {
        BufferedTokenStream stream = (BufferedTokenStream)_input;
        int prevTokenType = stream.LA(1);
        // Gobra change: Also allow semicolons to be left out right before !>
        return prevTokenType == GobraParser.R_CURLY || prevTokenType == GobraParser.R_PAREN || prevTokenType == GobraParser.R_PRED;
    }
}
