package org.oldman.repositories.bulders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhereClauseBuilder {
    private final List<WhereData> whereDataList = new ArrayList<>();

    public void where(FieldInfo fieldInfo, Operator operator, String parameterString) {
        whereDataList.add(new WhereData(fieldInfo, operator, parameterString, WhereType.WHERE));
    }

    public void and(FieldInfo fieldInfo, Operator operator, String parameterString) {
        whereDataList.add(new WhereData(fieldInfo, operator, parameterString, WhereType.AND));
    }

    public void or(FieldInfo fieldInfo, Operator operator, String parameterString) {
        whereDataList.add(new WhereData(fieldInfo, operator, parameterString, WhereType.OR));
    }

    public String buildWhereQueryPart() {
        return whereDataList.stream()
                .map(WhereData::buildWhereClause)
                .collect(Collectors.joining("\n"));
    }
}
