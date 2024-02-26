package org.oldman.repositories.bulders;

import org.oldman.repositories.bulders.pojo.FieldInfo;
import org.oldman.repositories.bulders.where.Operator;

public interface IWhereClauseBuilder extends QueryPartBuilder {
    void where(FieldInfo fieldInfo, Operator operator, String parameterString);

    void and(FieldInfo fieldInfo, Operator operator, String parameterString);

    void or(FieldInfo fieldInfo, Operator operator, String parameterString);
}
