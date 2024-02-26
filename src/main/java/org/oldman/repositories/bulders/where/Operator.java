package org.oldman.repositories.bulders.where;

import org.oldman.repositories.bulders.pojo.FieldInfo;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public enum Operator {
    EQUAL("="),
    NOT_EQUAL("<>"),
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

    public String apply(FieldInfo field, Object value) {
        return field.tableAliasWithField() + " " + operator + " " + value.toString();
    }
}
