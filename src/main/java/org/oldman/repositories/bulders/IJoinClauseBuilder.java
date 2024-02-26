package org.oldman.repositories.bulders;

import org.oldman.repositories.bulders.join.JoinData;
import org.oldman.repositories.bulders.join.enums.FetchType;
import org.oldman.repositories.bulders.join.enums.JoinType;

public interface IJoinClauseBuilder extends QueryPartBuilder {
    void join(JoinData joinData, JoinType joinType, FetchType fetchType);
}
