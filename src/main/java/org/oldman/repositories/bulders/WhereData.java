package org.oldman.repositories.bulders;

import org.oldman.repositories.bulders.pojo.FieldInfo;
import org.oldman.repositories.bulders.where.Operator;
import org.oldman.repositories.bulders.where.WhereType;

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
                operator.apply(fieldInfo, parameterString);
    }
}
