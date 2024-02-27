package org.oldman.repositories.bulders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class WhereClauseBuilder implements IWhereClauseBuilder {
    private final List<WhereData> whereDataList = new ArrayList<>();

    @Override
    public void addWhereClause(WhereData whereData) {
        whereDataList.add(whereData);
    }

    @Override
    public String buildQueryPart() {
        return whereDataList.stream()
                .map(WhereData::buildWhereClause)
                .collect(Collectors.joining("\n"));
    }
}
