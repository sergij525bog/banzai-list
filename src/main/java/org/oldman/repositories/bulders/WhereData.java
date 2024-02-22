package org.oldman.repositories.bulders;

final class WhereData {
    private final FieldInfo fieldInfo;
    private final Operator operator;
    private final String parameterString;
    private final WhereType whereType;

    WhereData(FieldInfo fieldInfo, Operator operator, String parameterString, WhereType whereType) {
        this.fieldInfo = fieldInfo;
        this.operator = operator;
        this.parameterString = parameterString;
        this.whereType = whereType;
    }

    public String buildWhereClause() {
        return whereType.getWhereType() +
                " " +
                fieldInfo.getTableAlias() +
                "." +
                fieldInfo.getField() +
                " " +
                operator.getOperator() +
                " " +
                parameterString;
    }
}
