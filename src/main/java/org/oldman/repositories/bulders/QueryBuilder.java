package org.oldman.repositories.bulders;

import org.oldman.repositories.bulders.join.*;

import java.util.*;

public class QueryBuilder {
    private TableInfo fromTable;
    private final List<JoinClause> joinClauses = new ArrayList<>();
    private final List<WhereData> whereClauses = new ArrayList<>();
    private final List<String> groupingByClauses = new ArrayList<>();
    private TableInfo tempTableInfo;
    private FetchType tempFetchType;
    private JoinType tempJoinType;

    private WhereClauseBuilder whereClauseBuilder;

    public QueryBuilder from(TableInfo tableInfo) {
        fromTable = tableInfo;
        return this;
    }

    public QueryBuilder where(FieldInfo fieldInfo, Operator operator, String parameterString) {
        if (whereClauseBuilder != null) {
            throw new IllegalStateException("You call where() method already");
        }
        whereClauseBuilder = new WhereClauseBuilder();
        whereClauseBuilder.where(fieldInfo, operator, parameterString);
        return this;
    }

    public QueryBuilder and(FieldInfo fieldInfo, Operator operator, String parameterString) {
        if (whereClauseBuilder == null) {
            throw new IllegalStateException("Before calling this method, you must call the where() method");
        }
        whereClauseBuilder.and(fieldInfo, operator, parameterString);
        return this;
    }

    public QueryBuilder or(FieldInfo fieldInfo, Operator operator, String parameterString) {
        if (whereClauseBuilder == null) {
            throw new IllegalStateException("Before calling this method, you must call the where() method");
        }
        whereClauseBuilder.or(fieldInfo, operator, parameterString);
        return this;
    }

    public JoinPart joinOn(TableInfo tableInfo) {
        return joinOn(tableInfo, JoinType.JOIN, FetchType.NONE);
    }

    public JoinPart joinOnFetch(TableInfo tableInfo) {
        return joinOn(tableInfo, JoinType.JOIN, FetchType.FETCH);
    }

    public JoinPart leftJoinOn(TableInfo tableInfo) {
        return joinOn(tableInfo, JoinType.LEFT_JOIN, FetchType.NONE);
    }

    public JoinPart leftJoinOnFetch(TableInfo tableInfo) {
        return joinOn(tableInfo, JoinType.LEFT_JOIN, FetchType.FETCH);
    }

    public JoinPart rightJoinOn(TableInfo tableInfo) {
        return joinOn(tableInfo, JoinType.RIGHT_JOIN, FetchType.NONE);
    }

    public JoinPart rightJoinOnFetch(TableInfo tableInfo) {
        return joinOn(tableInfo, JoinType.RIGHT_JOIN, FetchType.FETCH);
    }

    private JoinPart joinOn(TableInfo tableInfo, JoinType joinType, FetchType fetchType) {
        storeTempInfo(tableInfo, joinType, fetchType);
        return new JoinPart(this);
    }

    private void storeTempInfo(TableInfo tableInfo, JoinType joinType, FetchType fetchType) {
        tempTableInfo = tableInfo;
        tempJoinType = joinType;
        tempFetchType = fetchType;
    }

    private void clearTempInfo() {
        tempTableInfo = null;
        tempJoinType = null;
        tempFetchType = null;
    }

    private QueryBuilder join(JoinData joinData, JoinType joinType, FetchType fetchType) {
        joinClauses.add(JoinClause.createJoinClause(joinData, joinType, fetchType));
        return this;
    }

    public QueryBuilder join(FieldInfo collectionField) {
        return join(collectionField, JoinType.JOIN, FetchType.NONE);
    }

    public QueryBuilder joinFetch(FieldInfo collectionField) {
        return join(collectionField, JoinType.JOIN, FetchType.FETCH);
    }

    public QueryBuilder leftJoin(FieldInfo collectionField) {
        return join(collectionField, JoinType.LEFT_JOIN, FetchType.NONE);
    }

    public QueryBuilder leftJoinFetch(FieldInfo collectionField) {
        return join(collectionField, JoinType.LEFT_JOIN, FetchType.FETCH);
    }

    public QueryBuilder rightJoin(FieldInfo collectionField) {
        return join(collectionField, JoinType.RIGHT_JOIN, FetchType.NONE);
    }

    public QueryBuilder rightJoinFetch(FieldInfo collectionField) {
        return join(collectionField, JoinType.RIGHT_JOIN, FetchType.FETCH);
    }

    private QueryBuilder join(FieldInfo fieldInfo, JoinType joinType, FetchType fetchType) {
        return join(JoinDirector.createJoinByCollection(fieldInfo), joinType, fetchType);
    }

    public String build() {
        String alias = fromTable.getAlias();
        StringBuilder builder = new StringBuilder("select " + alias);
        builder.append(" from ")
                .append(fromTable.getTable())
                .append(" ")
                .append(alias);
        appendJoinClauses(builder);
        builder.append("\n");
        builder.append(whereClauseBuilder.buildWhereQueryPart());

        return builder.toString();
    }

    private void appendJoinClauses(StringBuilder builder) {
        joinClauses.forEach(joinClause -> builder.append(joinClause.buildJoinClause()));
    }

    public static class JoinPart {
        private final QueryBuilder builder;

        JoinPart(QueryBuilder builder) {
            this.builder = builder;
        }

        public QueryBuilder on(FieldInfo firstField, FieldInfo secondField) {
            JoinData joinByTwoTables = JoinDirector.createJoinByTwoTables(firstField, secondField, builder.tempTableInfo);
            builder.join(joinByTwoTables, builder.tempJoinType, builder.tempFetchType);
            builder.clearTempInfo();
            return builder;
        }
    }
}
