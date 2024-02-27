package org.oldman.repositories.bulders;

public interface IWhereClauseBuilder extends QueryPartBuilder {
    void addWhereClause(WhereData whereData);
}
