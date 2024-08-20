package Classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static Classes.TokenType.*;

@AllArgsConstructor
@Getter
@Setter
public class Parser {

    List<Token> tokenList;
    int indx = 0;


    private Token getCurrentToken() {
        if (indx < tokenList.size()){
            return tokenList.get(indx);
        }
        else  {
            return new Token(EOF, "");
        }
    }

    private void advanceToNextToken() {
        indx++;
    }

    private void match(TokenType expectedToken) {
        if (getCurrentToken().type == expectedToken) {
            advanceToNextToken();
        }
        else {
            throw new RuntimeException("Unexpected token: " + getCurrentToken().type);
        }
    }


    public Node parseProgram() {
        List<Node> statements = new ArrayList<>();
        while (getCurrentToken().type != TokenType.EOF) {
            statements.add(parseStatement());  // Parse each statement and add to the list
        }
        return new BlockNode(statements);  // Return a BlockNode containing all statements
    }

    public Node parseDeclaration() {
        Token keyword = getCurrentToken();
        boolean isMutable;
        if (keyword.type != VAR && keyword.type != LET) {
            throw new RuntimeException("Invalid Variable Declaration");
        }
        isMutable = keyword.type == VAR;
        advanceToNextToken();
        Token identifier = getCurrentToken();
        match(IDENTIFIER);
        match(COLON);
        Token varType = getCurrentToken();
        match(TYPE);
        match(ASSIGNMENT);
        Node expression = parseExpression();
        match(SEMI_COLON);

        SymbolTable symbolTable = new SymbolTable();
        return new AssignmentNode(isMutable, identifier, varType, expression);
    }
    public Node parseExpression() {
        Node node = parseTerm();
        while(getCurrentToken().type == PLUS || getCurrentToken().type == DASH){
            Token operator = getCurrentToken();
            advanceToNextToken();
            Node rightNode = parseTerm();
            node = new binaryOperatorNode(operator, node, rightNode);
        }
        return node;
    }

    public Node parseTerm() {
        Node node = parseFactor();
        while(getCurrentToken().type == MULTIPLY || getCurrentToken().type == SLASH) {
            Token operator = getCurrentToken();
            advanceToNextToken();
            Node rightNode = parseFactor();
            node = new binaryOperatorNode(operator, node, rightNode);
        }
        return node;
    }

    public Node parseFactor() {
        Token currToken = getCurrentToken();
        if (currToken.type == OPEN_PAREN) {
           advanceToNextToken();
           Node node = parseExpression();
           match(CLOSE_PAREN);
           return node;
        } else if (currToken.type == NUMBER) {
            advanceToNextToken();
            return new numberNode(currToken);
        } else if (currToken.type == IDENTIFIER) {
            advanceToNextToken();
            return new IdentifierNode(currToken);
        } else {
            throw new RuntimeException("in parse factor error");
        }
    }

    public Node parseIfStatement() {
         match(IF);
         match(OPEN_PAREN);
         Node condition = parseCondition();
         match(CLOSE_PAREN);
         Node ifBlock = parseBlock();
         Node elseBlock = null;
         if (getCurrentToken().type == ELSE) {
             match(ELSE);
             elseBlock = parseBlock();
         }
        return new IfStatementNode(condition, ifBlock, elseBlock);
    }


    public Node parseBlock() {
        match(OPEN_CURLY);

        List<Node> statements = new ArrayList<>();

        while (getCurrentToken().type != CLOSE_CURLY) {
           statements.add(parseStatement());
        }

        match(CLOSE_CURLY);
        return new BlockNode(statements);
    }


    public Node parseStatement() {
        if (getCurrentToken().type == IF) {
            return parseIfStatement();
        }
        else if (getCurrentToken().type == VAR || getCurrentToken().type == LET) {
            return parseDeclaration();
        } else {
            throw new RuntimeException("Syntax Error: Unexpected Token at" + getCurrentToken().value);
        }
    }


    public Node parseCondition() {
        Node left = parseExpression();

        if (isComparisonOperator(getCurrentToken().type)) {
            Token operator = getCurrentToken();
            advanceToNextToken();
            Node right = parseExpression();
            return new binaryOperatorNode(operator, left, right);
        }

        while (isLogicalOperator(getCurrentToken().type)){
            Token operator = getCurrentToken();
            advanceToNextToken();

            Node right = parseCondition();
            left = new binaryOperatorNode(operator, left ,right);
        }

        return left;
    }

    private boolean isComparisonOperator(TokenType type) {
        return type == TokenType.EQUALS || type == TokenType.NOT_EQUALS ||
                type == TokenType.LESS || type == TokenType.GREATER ||
                type == TokenType.LESS_EQUALS || type == TokenType.GREATER_EQUALS;
    }

    private boolean isLogicalOperator(TokenType type) {
        return type == TokenType.AND || type == TokenType.OR;
    }

}



/*
<if-statement> ::= "if" "(" <expression> ")" <block> ["else" <block>]
<block> ::= "{" { <statement> } "}"
<statement> ::= <if-statement>
              | <assignment>
              | <declaration>
              | <expression>
              | <block>
<expression> ::= <expression> "+" <term>
               | <expression> "-" <term>
               | <term>
<term> ::= <term> "*" <factor>
         | <term> "/" <factor>
         | <factor>
<factor> ::= "(" <expression> ")"
           | <number>
           | <identifier>
<identifier> ::= [a-zA-Z_][a-zA-Z0-9_]*
<number> ::= [0-9]+

 */
