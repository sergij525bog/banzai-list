package org.oldman.repositories.bulders.join;

import org.oldman.repositories.bulders.join.enums.FetchType;
import org.oldman.repositories.bulders.join.enums.JoinType;

public class JoinClause {
    private final JoinData joinData;
    private final JoinType joinType;
    private final FetchType fetchType;

    private JoinClause(JoinData joinData, JoinType joinType, FetchType fetchType) {
        this.joinData = joinData;
        this.joinType = joinType;
        this.fetchType = fetchType;
    }

    public static JoinClause createJoinClause(JoinData joinData, JoinType joinType, FetchType fetchType) {
        if (joinData == null || joinType == null || fetchType == null) {
            throw new IllegalStateException("Builder has not enough info for creating new object");
        }
        return new JoinClause(joinData, joinType, fetchType);
    }

    public String buildJoinClause() {
        return joinType.getJoinType() +
                fetchType.getFetchType() +
                " " +
                joinData.buildJoinData();
    }
}
