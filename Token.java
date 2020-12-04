public class Token {

    private Code code;
    private String lexeme;
    /**
     * This is the token class I made for the analyzer
     * Uses basic enumeration so the tokens can be classified into C, java, perl literals, etc. 
     */
    public enum Code {
        IDENT, //Used to check for indent
        STRING, //Used to check for strings
        CHAR, //Used to check for characters
        INT, //Used to check for integers 
        FLOAT, //Used to check for decimals as floats
        ASSIGN, // Used to check for equal signs 
        ADD, //Used to check for addition
        SUB, //Used to check for subtraction
        MUL, //Used to check for multiplication 
        DIV, //Used to check for division
        INC, //Used to check for increments
        DEC, //Used to check for decrements
        MOD, //Used to check for modulus
        AND, //Used to check for AND
        OR, //Used to check for OR
        BLOC_O, //Used to check for open block
        BLOC_C, //Used to check for closed block
        PAR_O, //Used to check for open parentheses
        PAR_C, //Used to check for closed parentheses 
        FOR, //Used to check for FOR
        WHILE, //Used to check for WHILE
        IF, //Used to check for IF
    }

    public Token(Code code, String lexeme) {
        this.code = code;
        this.lexeme = lexeme;
    }
    /**
     * Get and Sets' in order to properly utilize lexer 
     */
    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
}
																									