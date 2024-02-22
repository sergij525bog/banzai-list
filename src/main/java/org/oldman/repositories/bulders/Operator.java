package org.oldman.repositories.bulders;

public enum Operator {
    EQUAL("="),
    LESS("<"),
    MORE(">"),
    LESS_OR_EQUAL("<="),
    MORE_OR_EQUAL(">=");

    private final String operator;

    Operator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}
