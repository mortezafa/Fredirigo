package Classes;


import lombok.AllArgsConstructor;

import java.util.List;

public abstract class Node {
    public abstract void print(String indent);
}

@AllArgsConstructor
class binaryOperatorNode extends Node {
    private final TokenType.Token operator;
    private final Node left;
    private final Node right;


    @Override
    public void print(String indent) {
        System.out.println(indent + "BinaryOperationNode(" + operator.value + ")");
        if (left != null) {
            left.print(indent + "  ");
        }
        if (right != null) {
            right.print(indent + "  ");
        }
    }
}
@AllArgsConstructor
class numberNode extends Node {
    private final TokenType.Token token;

    @Override
    public void print(String indent) {
        System.out.println(indent + "NumberNode(" + token.value + ")");
    }
}

@AllArgsConstructor
class AssignmentNode extends Node {
    public final Boolean varType;
    public final TokenType.Token identifier;
    public final TokenType.Token type;
    public final Node expression;


    @Override
    public void print(String indent) {
        System.out.println(indent + "AssignmentNode(" + identifier.value + ")");
        if (expression != null) {
            expression.print(indent + "  ");
        }
    }
}

@AllArgsConstructor
class IdentifierNode extends Node {
    public final TokenType.Token token;

    @Override
    public void print(String indent) {
        System.out.println(indent + "IdentifierNode(" + token.value + ")");
    }
}

@AllArgsConstructor
class IfStatementNode extends Node {
    public final Node condition;
    public final Node ifBlock;
    public final Node elseBlock;

    public void print(String indent) {
        System.out.println(indent + "IfStatementNode");
        System.out.println(indent + "  Condition:");
        condition.print(indent + "    ");
        System.out.println(indent + "  IfBlock:");
        ifBlock.print(indent + "    ");
        if (elseBlock != null) {
            System.out.println(indent + "  ElseBlock:");
            elseBlock.print(indent + "    ");
        }
    }
}

@AllArgsConstructor
class BlockNode extends Node {
    public final List<Node> statements;

    @Override
    public void print(String indent) {
        System.out.println(indent + "BlockNode");
        for (Node statement : statements) {
            statement.print(indent + "  ");
        }
    }
}