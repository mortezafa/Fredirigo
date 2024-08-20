package Classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
public enum TokenType {
    // End of File
    EOF,

    // Literals
    NULL,
    CRAIGAPPROVED,
    CRAIGDISAPPROVED,
    NUMBER,
    STRING,
    IDENTIFIER,
    TYPE,
    // Grouping & Braces
    OPEN_BRACKET,  // [
    CLOSE_BRACKET, // ]
    OPEN_CURLY,    // {
    CLOSE_CURLY,   // }
    OPEN_PAREN,    // (
    CLOSE_PAREN,   // )

    // Equivalence
    ASSIGNMENT,    // =
    EQUALS,        // ==
    NOT_EQUALS,    // !=
    NOT,           // !

    // Conditional
    LESS,          // <
    LESS_EQUALS,   // <=
    GREATER,       // >
    GREATER_EQUALS,// >=

    // Logical
    OR,            // ||
    AND,           // &&

    // Symbols
    DOT,           // .
    DOT_DOT,       // ..
    SEMI_COLON,    // ;
    COLON,         // :
    QUESTION,      // ?
    COMMA,         // ,

    // Shorthand
    PLUS_PLUS,         // ++
    MINUS_MINUS,       // --
    PLUS_EQUALS,       // +=
    MINUS_EQUALS,      // -=
    NULLISH_ASSIGNMENT,// ??=

    // Maths
    PLUS,         // +
    DASH,         // -
    SLASH,        // /
    MULTIPLY,     // *
    PERCENT,      // %

    // Reserved Keywords
    VAR,
    LET,
    CLASS,
    NEW,
    FROM,
    FUNC,
    IF,
    ELSE,
    FOREACH,
    WHILE,
    FOR,
    EXPORT,
    TYPEOF,
    IN,
    CRAIG,
    INCLUDE,
    UNVEIL,
    ANY,
    //


    // Misc
    NUM_TOKENS;

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Token {
        public TokenType type;
        public String value;
        public static String debug(Token token) {
            TokenType type = token.getType();
            if (type == TokenType.IDENTIFIER || type == TokenType.NUMBER || type == TokenType.STRING) {
                return String.format("%s(%s)\n", tokenKindToString(type), token.getValue());
            } else {
                return String.format("%s()\n", tokenKindToString(type));
            }
        }

        public static String tokenKindToString(TokenType kind) {
            return switch (kind) {
                case EOF -> "eof";
                case NULL -> "null";
                case NUMBER -> "number";
                case STRING -> "string";
                case CRAIGAPPROVED -> "true";
                case CRAIGDISAPPROVED -> "false";
                case IDENTIFIER -> "identifier";
                case OPEN_BRACKET -> "open_bracket";
                case CLOSE_BRACKET -> "close_bracket";
                case OPEN_CURLY -> "open_curly";
                case CLOSE_CURLY -> "close_curly";
                case OPEN_PAREN -> "open_paren";
                case CLOSE_PAREN -> "close_paren";
                case ASSIGNMENT -> "assignment";
                case EQUALS -> "equals";
                case NOT_EQUALS -> "not_equals";
                case NOT -> "not";
                case LESS -> "less";
                case LESS_EQUALS -> "less_equals";
                case GREATER -> "greater";
                case GREATER_EQUALS -> "greater_equals";
                case OR -> "or";
                case AND -> "and";
                case DOT -> "dot";
                case DOT_DOT -> "dot_dot";
                case SEMI_COLON -> "semi_colon";
                case COLON -> "colon";
                case QUESTION -> "question";
                case COMMA -> "comma";
                case PLUS_PLUS -> "plus_plus";
                case MINUS_MINUS -> "minus_minus";
                case PLUS_EQUALS -> "plus_equals";
                case MINUS_EQUALS -> "minus_equals";
                case NULLISH_ASSIGNMENT -> "nullish_assignment";
                case PLUS -> "plus";
                case DASH -> "dash";
                case SLASH -> "slash";
                case MULTIPLY -> "star";
                case PERCENT -> "percent";
                case LET -> "let";
                case CLASS -> "class";
                case NEW -> "new";
                case FROM -> "from";
                case FUNC -> "func";
                case IF -> "if";
                case ELSE -> "else";
                case FOREACH -> "foreach";
                case FOR -> "for";
                case WHILE -> "while";
                case EXPORT -> "export";
                case IN -> "in";
                case CRAIG -> "craig";
                case INCLUDE -> "include";
                case VAR -> "var";
                case TYPEOF -> "typeOf";
                case UNVEIL -> "unveil";
                case ANY -> "Any";

                default -> String.format("unknown(%s)", kind.name());
            };
        }
        public static Token createNewToken (TokenType tokenType, String value) {
            return new Token(tokenType, value);
        }

        public Boolean isOneOfMany (TokenType ... expectedTokens) {
            for (TokenType expected: expectedTokens) {
                if (expected == this.type) {
                    return true;
                }
            }
            return false;
        }

        public static final java.util.Map<String, TokenType> reservedWords = new java.util.HashMap<>();
        static {
            reservedWords.put("if", TokenType.IF);
            reservedWords.put("else", TokenType.ELSE);
            reservedWords.put("while", TokenType.WHILE);
            reservedWords.put("Craig", TokenType.CRAIG);
            reservedWords.put("include", TokenType.INCLUDE);
            reservedWords.put("var", TokenType.VAR);
            reservedWords.put("let", TokenType.LET);
            reservedWords.put("Int", TokenType.TYPE);
            reservedWords.put("String", TokenType.TYPE);
            reservedWords.put("Float", TokenType.TYPE);
            reservedWords.put("Bool", TokenType.TYPE);
        }

    }
}


