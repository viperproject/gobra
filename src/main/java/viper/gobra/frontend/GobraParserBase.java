package viper.gobra.frontend;
import java.util.List;
import org.antlr.v4.runtime.*;
import viper.gobra.frontend.GobraLexer;

/**
 * All parser methods that used in grammar (p, prev, notLineTerminator, etc.)
 * should start with lower case char similar to parser rules.
 */
public abstract class GobraParserBase extends org.antlr.v4.runtime.Parser
{
    protected GobraParserBase(TokenStream input) {
        super(input);
    }

    /**
     * Returns true if the current Token is a closing bracket (")" or "}")
     */
    protected boolean closingBracket()
    {
        BufferedTokenStream stream = (BufferedTokenStream)_input;
        int prevTokenType = stream.LA(1);
        // Gobra change: Also allow semicolons to be left out right before !>
        return prevTokenType == GobraParser.R_CURLY || prevTokenType == GobraParser.R_PAREN || prevTokenType == GobraParser.R_PRED;
    }
}
