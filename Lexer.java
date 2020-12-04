import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lexer class for lexical analysis
 */
public class Lexer {     
    private String input;   // input text 
    private int iSize;		// input size
    private int offset;     // offset: current lexing position (scan position)
    private ArrayList<Token> tokens;    // list of tokens

   //Constructor 
    public Lexer() {
        this.input = null;
        this.iSize = -1;
        this.offset = -1;
        this.tokens = null;
    }

    /**
     * Initialization of the lexer object when the text input is in. 

     */
    private boolean init(String input)
    {
        if (input == null)
            return false;
        this.input = input;
        this.iSize = input.length();
        this.offset = 0;
        this.tokens = new ArrayList<>();
        return true;
    }

    /**
  	 * Analyzer that takes the initialization of the input, checks if passes the size, offset and tokens.
  	 * Begins assigning attributes (through the enumeration found in Token.java) to each one of the operators, literals, and data types
     */
    public void analyze(String input)
    {
        if (init(input) == false)
            return;
        findAllTokens();
    }

    public String getInput() {
        return input;
    }
    
    public ArrayList<Token> getTokens() {
        return tokens;
    }


    /**
     * Add a new token to the tokens list, given a token code (type), and token size
     * The lexeme is subtracted from the input from offset to offset + tsize
     * Returns the token size
     */
    private int addToken(Token.Code code, int tsize)
    {
        String lexeme = input.substring(offset, offset + tsize);
        Token newToken = new Token(code, lexeme);
        this.tokens.add(newToken);
        this.offset += tsize;
        return tsize;
//        System.out.println(lexeme);
    }

    // Lexical analysis methods

    /**
     * Start scanning all kinds of tokens
     */
    private void findAllTokens()
    {
        while (offset < iSize)
        {
            // If no token is found then we move on to next char, offset++ meaning the index is moved to the next part of the .txt
            if (findIndent() == 0
                    && findString() == 0 
                    && findChar() == 0
                    && findInt() == 0
                    && findFloat() == 0
                    && findEqual() == 0
                    && findAdd() == 0
                    && findMin() == 0
                    && findDiv() == 0
                    && findMul() == 0
                    && findMod() == 0
                    && findOrAnd() == 0
                    && findBlock() == 0
                    && findPar() == 0
            )
                offset++;
        }
    }

    /**
     * Scanning for identifiers
     * return token size
     */
    private int findIndent()
    {
        int i = 0;
        char c = input.charAt(offset + i);
        // Checks the begining of perl identifier
        if ("$@".indexOf(c) < 0)
            return 0;
        // scan the rest of perl identifier
        for (i = 1; offset + i < iSize; i++)
        {
            c = input.charAt(offset + i);
            // check if the current character is valid for perl identifiers
            if (!Character.isLetterOrDigit(c) && c != '_')
                break;
        }
        // add new token, and return its size
        return addToken(Token.Code.IDENT, i);
    }

    /**
     * Scanning for strings
     * return token size
     */
    private int findString()
    {
        int i = 0;
        char c = input.charAt(offset + i);
        // scan for the beginning of a string literal
        if (c != '"')
            return 0;
        // scan the rest of a string literal
        for (i = 1; offset + i < iSize; i++)
        {
            c = input.charAt(offset + i);
            // check the end of a string literal
            if (c == '"')
                break;
        }
        //check if the for loop was broken by the end of a string, or end of line
        if (c == '"')
            return addToken(Token.Code.STRING, i + 1);
        else
            return 0;
    }
    // The rest under this is redundant so I don't think I need to explain it a ton
    /**
     * Scanning for characters
     * return token size
     */
    private int findChar()
    {
        if (offset + 3 > iSize)
            return 0;
        // slice the input from offset to offset + 3
        String ch = input.substring(offset, offset + 3);
        // create regex pattern for character literal
        Pattern patt = Pattern.compile("'.'"); // regex
        // link the pattern with the slice
        Matcher matcher = patt.matcher(ch);
        // check if the slice maths the pattern
        if (!matcher.find())
            return 0;
        return addToken(Token.Code.CHAR, 3);
    }

    /**
     * Scanning for integers
     * return token size
     */
    private int findInt()
    {
        int i = 0;
        for (; offset + i < iSize; i++)
        {
            char c = input.charAt(offset + i);
            // check the presence of a dot so we can see if it is a Float
            if (c == '.')
                return findFloat();
            if (!Character.isDigit(c))
                break;
        }
        if (i == 0)
            return 0;
        addToken(Token.Code.INT, i);
        return i;
    }

    /**
     * Scanning for floats
     * return token size
     */
    private int findFloat()
    {
        int i = 0;
        // create a dot tracker, to define first dot, from second
        boolean dotted = false;
        for (; offset + i < iSize; i++)
        {
            char c = input.charAt(offset + i);
            // check the presence of the first dot, if yes, dotted become true
            if (c == '.' && !dotted)
                dotted = true;
            else if (!Character.isDigit(c)) // check the end of a float
                break;
        }
        // check if any chars have been matched a float pattern
        if (i == 0)
            return 0;
        addToken(Token.Code.FLOAT, i);
        return i;
    }


    private int findEqual()
    {
        if (input.charAt(offset) != '=')
            return 0;
        addToken(Token.Code.ASSIGN, 1);
        return 1;
    }


    private int findAdd()
    {
        // check for increment (double ++) first to avoid conflict with addition (single +)
        if (offset + 2 <= iSize && "++".equals(input.substring(offset, offset + 2)))
        {
            addToken(Token.Code.INC, 2);
            return 2;
        }
        if (input.charAt(offset) != '+')
            return 0;
        addToken(Token.Code.ADD, 1);
        return 1;
    }


    private int findMin()
    {
        if (offset + 2 <= iSize && "--".equals(input.substring(offset, offset + 2)))
        {
            addToken(Token.Code.DEC, 2);
            return 2;
        }
        if (input.charAt(offset) != '-')
            return 0;
        addToken(Token.Code.SUB, 1);
        return 1;
    }

    /**
     * Scanning for division sign
     * return token size
     */
    private int findDiv()
    {
        if (input.charAt(offset) != '/')
            return 0;
        addToken(Token.Code.DIV, 1);
        return 1;
    }

    /**
     * Scanning for multiplication sign
     * return token size
     */
    private int findMul()
    {
        if (input.charAt(offset) != '*')
            return 0;
        addToken(Token.Code.MUL, 1);
        return 1;
    }

    /**
     * Scanning for modulo
     * return token size
     */
    private int findMod()
    {
        if (input.charAt(offset) != '%')
            return 0;
        addToken(Token.Code.MOD, 1);
        return 1;
    }

    /**
     * Scanning logical and, or
     * return token size
     */
    private int findOrAnd()
    {
        if (offset + 2 > iSize)
            return 0;
        String str = input.substring(offset, offset + 2);
        if (str.equals("&&"))
        {
            addToken(Token.Code.AND, 2);
            return 2;
        }
        else if (str.equals("||"))
        {
            addToken(Token.Code.OR, 2);
            return 2;
        }
        return 0;
    }

    /**
     * Scanning for opening and closing blocks symbols
     * return token size
     */
    private int findBlock()
    {
        if (input.charAt(offset) == '{')
            return addToken(Token.Code.BLOC_O, 1);
        else if (input.charAt(offset) == '}')
            return addToken(Token.Code.BLOC_C, 1);
        return 0;
    }

    /**
     * Scanning for opening and closing parentheses
     * return token size
     */
    private int findPar()
    {
        if (input.charAt(offset) == '(')
            return addToken(Token.Code.PAR_O, 1);
        else if (input.charAt(offset) == ')')
            return addToken(Token.Code.PAR_C, 1);
        return 0;
    }
}
