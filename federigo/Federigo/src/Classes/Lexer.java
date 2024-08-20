package Classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.*;
import Classes.TokenType;

import static Classes.TokenType.Token.createNewToken;
import static Classes.TokenType.Token.reservedWords;

@AllArgsConstructor
@Getter
@Setter
public class Lexer {
    @AllArgsConstructor
    @Getter
    @Setter
    private static class RegexPattern {
        private final Pattern pattern;
        private final RegexHandler handler;

        public Matcher match(String input) {
            return pattern.matcher(input);
        }

        public void handle(Lexer lexer, Matcher matcher) {
            handler.handle(lexer, matcher);
        }
    }
    private interface RegexHandler {
        void handle(Lexer lexer, Matcher matcher);
    }

    private final List<RegexPattern> patterns = new ArrayList<>();
    private final List<TokenType.Token> tokens = new ArrayList<>();
    private final String source;
    private int pos = 0;
    private int line = 1;


    public Lexer(String source) {
        this.source = source;
        patterns.add(new RegexPattern(Pattern.compile("\\s+"), this::skipHandler));
        patterns.add(new RegexPattern(Pattern.compile("//.*"), this::commentHandler));
        patterns.add(new RegexPattern(Pattern.compile("\"[^\"]*\""), this::stringHandler));
        patterns.add(new RegexPattern(Pattern.compile("[0-9]+(\\.[0-9]+)?"), this::numberHandler));
        patterns.add(new RegexPattern(Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*"), this::symbolHandler));
        patterns.add(new RegexPattern(Pattern.compile("\\["), defaultHandler(TokenType.OPEN_BRACKET, "[")));
        patterns.add(new RegexPattern(Pattern.compile("\\]"), defaultHandler(TokenType.CLOSE_BRACKET, "]")));
        patterns.add(new RegexPattern(Pattern.compile("\\{"), defaultHandler(TokenType.OPEN_CURLY, "{")));
        patterns.add(new RegexPattern(Pattern.compile("\\}"), defaultHandler(TokenType.CLOSE_CURLY, "}")));
        patterns.add(new RegexPattern(Pattern.compile("\\("), defaultHandler(TokenType.OPEN_PAREN, "(")));
        patterns.add(new RegexPattern(Pattern.compile("\\)"), defaultHandler(TokenType.CLOSE_PAREN, ")")));
        patterns.add(new RegexPattern(Pattern.compile("=="), defaultHandler(TokenType.EQUALS, "==")));
        patterns.add(new RegexPattern(Pattern.compile("!="), defaultHandler(TokenType.NOT_EQUALS, "!=")));
        patterns.add(new RegexPattern(Pattern.compile("="), defaultHandler(TokenType.ASSIGNMENT, "=")));
        patterns.add(new RegexPattern(Pattern.compile("!"), defaultHandler(TokenType.NOT, "!")));
        patterns.add(new RegexPattern(Pattern.compile("<="), defaultHandler(TokenType.LESS_EQUALS, "<=")));
        patterns.add(new RegexPattern(Pattern.compile("<"), defaultHandler(TokenType.LESS, "<")));
        patterns.add(new RegexPattern(Pattern.compile(">="), defaultHandler(TokenType.GREATER_EQUALS, ">=")));
        patterns.add(new RegexPattern(Pattern.compile(">"), defaultHandler(TokenType.GREATER, ">")));
        patterns.add(new RegexPattern(Pattern.compile("\\|\\|"), defaultHandler(TokenType.OR, "||")));
        patterns.add(new RegexPattern(Pattern.compile("&&"), defaultHandler(TokenType.AND, "&&")));
        patterns.add(new RegexPattern(Pattern.compile("\\.\\."), defaultHandler(TokenType.DOT_DOT, "..")));
        patterns.add(new RegexPattern(Pattern.compile("\\."), defaultHandler(TokenType.DOT, ".")));
        patterns.add(new RegexPattern(Pattern.compile(";"), defaultHandler(TokenType.SEMI_COLON, ";")));
        patterns.add(new RegexPattern(Pattern.compile(":"), defaultHandler(TokenType.COLON, ":")));
        patterns.add(new RegexPattern(Pattern.compile("\\?\\?="), defaultHandler(TokenType.NULLISH_ASSIGNMENT, "??=")));
        patterns.add(new RegexPattern(Pattern.compile("\\?"), defaultHandler(TokenType.QUESTION, "?")));
        patterns.add(new RegexPattern(Pattern.compile(","), defaultHandler(TokenType.COMMA, ",")));
        patterns.add(new RegexPattern(Pattern.compile("\\+\\+"), defaultHandler(TokenType.PLUS_PLUS, "++")));
        patterns.add(new RegexPattern(Pattern.compile("--"), defaultHandler(TokenType.MINUS_MINUS, "--")));
        patterns.add(new RegexPattern(Pattern.compile("\\+="), defaultHandler(TokenType.PLUS_EQUALS, "+=")));
        patterns.add(new RegexPattern(Pattern.compile("-="), defaultHandler(TokenType.MINUS_EQUALS, "-=")));
        patterns.add(new RegexPattern(Pattern.compile("\\+"), defaultHandler(TokenType.PLUS, "+")));
        patterns.add(new RegexPattern(Pattern.compile("-"), defaultHandler(TokenType.DASH, "-")));
        patterns.add(new RegexPattern(Pattern.compile("/"), defaultHandler(TokenType.SLASH, "/")));
        patterns.add(new RegexPattern(Pattern.compile("\\*"), defaultHandler(TokenType.MULTIPLY, "*")));
        patterns.add(new RegexPattern(Pattern.compile("%"), defaultHandler(TokenType.PERCENT, "%")));
    }

    public List<TokenType.Token> tokenize() {
        while(!atEof()) {
            Boolean matched = false;

            for (RegexPattern pattern : patterns) {
                Matcher matcher = pattern.match(remainder());
                if (matcher.find() && matcher.start() == 0) {
                    pattern.handle(this, matcher);
                    matched = true;
                    break;
                }
            }
            if (!matched){
                throw new RuntimeException("Lexer error: unrecognized token near '" + remainder() + "'");
            }

        }
        tokens.add(createNewToken(TokenType.EOF, "EOF"));
        return tokens;
    }



    private String remainder() {
        return source.substring(pos);
    }

    private void advanceN(int n) {
        pos += n;
    }

    private void push (TokenType.Token token) {
        tokens.add(token);
    }

    private void advance() {
        pos++;
    }

    private char at() {
        return source.charAt(pos);
    }

    private boolean atEof() {
       return pos >= source.length();
    }


    private RegexHandler defaultHandler(TokenType kind, String value) {
        return (lexer, matcher) -> {
            lexer.advanceN(value.length());
            lexer.push(createNewToken(kind, value));
        };
    }

    private void stringHandler(Lexer lexer, Matcher matcher) {
        String stringLiteral = matcher.group();
        lexer.push(createNewToken(TokenType.STRING, stringLiteral));
        lexer.advanceN(stringLiteral.length());
    }

    private void numberHandler(Lexer lexer, Matcher matcher) {
        String number = matcher.group();
        lexer.push(createNewToken(TokenType.NUMBER, number));
        lexer.advanceN(number.length());
    }

    private void symbolHandler(Lexer lexer, Matcher matcher) {
        String symbol = matcher.group();
        TokenType kind = reservedWords.getOrDefault(symbol, TokenType.IDENTIFIER);
        lexer.push(new TokenType.Token(kind, symbol));
        lexer.advanceN(symbol.length());
    }

    private void skipHandler(Lexer lexer, Matcher matcher) {
        lexer.advanceN(matcher.end());
    }

    private void commentHandler(Lexer lexer, Matcher matcher) {
        lexer.advanceN(matcher.end());
        line++;
    }


}
