package org.oldman.repositories.bulders;

import org.oldman.repositories.bulders.join.JoinClause;
import org.oldman.repositories.bulders.join.JoinData;
import org.oldman.repositories.bulders.join.enums.FetchType;
import org.oldman.repositories.bulders.join.enums.JoinType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class JoinClauseBuilder implements IJoinClauseBuilder {
    private final List<JoinClause> joinClauses = new ArrayList<>();

    public void join(JoinData joinData, JoinType joinType, FetchType fetchType) {
        joinClauses.add(JoinClause.createJoinClause(joinData, joinType, fetchType));
    }

    @Override
    public String buildQueryPart() {
        return joinClauses.stream()
                .map(JoinClause::buildJoinClause)
                .collect(Collectors.joining("\n"));
    }
}
