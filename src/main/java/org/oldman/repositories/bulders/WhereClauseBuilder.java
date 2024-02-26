package org.oldman.repositories.bulders;

import org.oldman.repositories.bulders.pojo.FieldInfo;
import org.oldman.repositories.bulders.where.Operator;
import org.oldman.repositories.bulders.where.WhereType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class WhereClauseBuilder implements IWhereClauseBuilder {
    private final List<WhereData> whereDataList = new ArrayList<>();

    @Override
    public void where(FieldInfo fieldInfo, Operator operator, String parameterString) {
        whereDataList.add(new WhereData(fieldInfo, operator, parameterString, WhereType.WHERE));
    }

    @Override
    public void and(FieldInfo fieldInfo, Operator operator, String parameterString) {
        whereDataList.add(new WhereData(fieldInfo, operator, parameterString, WhereType.AND));
    }

    @Override
    public void or(FieldInfo fieldInfo, Operator operator, String parameterString) {
        whereDataList.add(new WhereData(fieldInfo, operator, parameterString, WhereType.OR));
    }

    @Override
    public String buildQueryPart() {
        return whereDataList.stream()
                .map(WhereData::buildWhereClause)
                .collect(Collectors.joining("\n"));
    }
}
