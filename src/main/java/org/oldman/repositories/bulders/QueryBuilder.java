package org.oldman.repositories.bulders;

import org.oldman.repositories.bulders.join.JoinData;
import org.oldman.repositories.bulders.join.JoinDirector;
import org.oldman.repositories.bulders.join.enums.FetchType;
import org.oldman.repositories.bulders.join.enums.JoinType;
import org.oldman.repositories.bulders.pojo.FieldInfo;
import org.oldman.repositories.bulders.pojo.TableInfo;
import org.oldman.repositories.bulders.where.Operator;
import org.oldman.repositories.bulders.where.WhereType;

public class QueryBuilder {
    private TableInfo fromTable;

    private IWhereClauseBuilder whereClauseBuilder;
    private final JoinClauseBuilder joinClauseBuilder = new JoinClauseBuilder();
    private final SelectClauseBuilder selectClauseBuilder = new SelectClauseBuilder();
    private int limit = -1;
    private int offset = -1;

    public QueryBuilder select(String selectString) {
        selectClauseBuilder.select(selectString);
        return this;
    }

    public QueryBuilder from(TableInfo tableInfo) {
        fromTable = tableInfo;
        return this;
    }

//    public QueryBuilder where(FieldInfo fieldInfo, Operator operator, String parameterString) {
//        if (whereClauseBuilder != null) {
//            throw new IllegalStateException("You call where() method already");
//        }
//        whereClauseBuilder = new WhereClauseBuilder();
//        whereClauseBuilder.addWhereClause(new WhereData(fieldInfo, operator, parameterString, WhereType.WHERE));
//        return this;
//    }

    public WherePart where(FieldInfo fieldInfo) {
        if (whereClauseBuilder != null) {
            throw new IllegalStateException("You call where() method already");
        }
        whereClauseBuilder = new WhereClauseBuilder();

        return new WherePart(whereClauseBuilder, this, new WherePart.WhereTempInfo(fieldInfo, WhereType.WHERE));
    }

    public WherePart and(FieldInfo fieldInfo) {
        return new WherePart(whereClauseBuilder, this, new WherePart.WhereTempInfo(fieldInfo, WhereType.AND));
    }

    public WherePart or(FieldInfo fieldInfo) {
        return new WherePart(whereClauseBuilder, this, new WherePart.WhereTempInfo(fieldInfo, WhereType.OR));
    }

//    public QueryBuilder and(FieldInfo fieldInfo, Operator operator, String parameterString) {
//        if (whereClauseBuilder == null) {
//            throw new IllegalStateException("Before calling this method, you must call the where() method");
//        }
//        whereClauseBuilder.addWhereClause(new WhereData(fieldInfo, operator, parameterString, WhereType.AND));
//        return this;
//    }
//
//    public QueryBuilder or(FieldInfo fieldInfo, Operator operator, String parameterString) {
//        if (whereClauseBuilder == null) {
//            throw new IllegalStateException("Before calling this method, you must call the where() method");
//        }
//        whereClauseBuilder.addWhereClause(new WhereData(fieldInfo, operator, parameterString, WhereType.OR));
//        return this;
//    }

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
        joinClauseBuilder.join(JoinDirector.createJoinByCollection(fieldInfo), joinType, fetchType);
        return this;
    }

    private JoinPart joinOn(TableInfo tableInfo, JoinType joinType, FetchType fetchType) {
        return new JoinPart(joinClauseBuilder, this, new JoinPart.JoinTempInfo(tableInfo, joinType, fetchType));
    }

    public QueryBuilder limit(int limit) {
        if (limit < 0 ) {
            throw new IllegalArgumentException("Limit cannot be less than 0");
        }
        this.limit = limit;
        return this;
    }

    public QueryBuilder offset(int offset) {
        if (offset < 0 ) {
            throw new IllegalArgumentException("Limit cannot be less than 0");
        }
        this.offset = offset;
        return this;
    }

    private String buildLimitIfExists() {
        return buildIntFieldClause("limit", limit);
    }

    private String buildOffsetIfExists() {
        return buildIntFieldClause("offset", offset);
    }

    private String buildIntFieldClause(String clause, int intField) {
        if (intField >= 0) {
            return " " + clause + " " + intField;
        }
        return "";
    }

    public String build() {
        String alias = fromTable.getAlias();

        return "select " + alias + " from " +
                fromTable.getTable() +
                " " +
                alias +
                joinClauseBuilder.buildQueryPart() +
                "\n" +
                whereClauseBuilder.buildQueryPart() +
                buildLimitIfExists() +
                buildOffsetIfExists();
    }

    public static class JoinPart {
        private final JoinClauseBuilder builder;
        private final QueryBuilder queryBuilder;
        private final JoinTempInfo joinTempInfo;

        private JoinPart(JoinClauseBuilder builder, QueryBuilder queryBuilder, JoinTempInfo joinTempInfo) {
            this.builder = builder;
            this.queryBuilder = queryBuilder;
            this.joinTempInfo = joinTempInfo;
        }

        public QueryBuilder on(FieldInfo firstField, FieldInfo secondField) {
            JoinData joinByTwoTables = JoinDirector.createJoinByTwoTables(firstField, secondField, joinTempInfo.tempTableInfo);

            builder.join(joinByTwoTables, joinTempInfo.tempJoinType, joinTempInfo.tempFetchType);

            return queryBuilder;
        }

        private static class JoinTempInfo {
            private final TableInfo tempTableInfo;
            private final JoinType tempJoinType;
            private final FetchType tempFetchType;

            private JoinTempInfo(TableInfo tempTableInfo, JoinType tempJoinType, FetchType tempFetchType) {
                this.tempTableInfo = tempTableInfo;
                this.tempJoinType = tempJoinType;
                this.tempFetchType = tempFetchType;
            }
        }
    }

    public static class WherePart {
        private final IWhereClauseBuilder whereClauseBuilder;
        private final QueryBuilder queryBuilder;
        private final WhereTempInfo whereTempInfo;

        private WherePart(IWhereClauseBuilder whereClauseBuilder, QueryBuilder queryBuilder, WhereTempInfo whereTempInfo) {
            this.whereClauseBuilder = whereClauseBuilder;
            this.queryBuilder = queryBuilder;
            this.whereTempInfo = whereTempInfo;
        }

        public QueryBuilder equals(String parameter) {
            whereClauseBuilder.addWhereClause(new WhereData(whereTempInfo.whereField, Operator.EQUAL, parameter, whereTempInfo.whereType));
            return queryBuilder;
        }

        public QueryBuilder more(String parameter) {
            whereClauseBuilder.addWhereClause(new WhereData(whereTempInfo.whereField, Operator.MORE, parameter, whereTempInfo.whereType));
            return queryBuilder;
        }

        public QueryBuilder less(String parameter) {
            whereClauseBuilder.addWhereClause(new WhereData(whereTempInfo.whereField, Operator.LESS, parameter, whereTempInfo.whereType));
            return queryBuilder;
        }

        public QueryBuilder lessOrEqual(String parameter) {
            whereClauseBuilder.addWhereClause(new WhereData(whereTempInfo.whereField, Operator.LESS_OR_EQUAL, parameter, whereTempInfo.whereType));
            return queryBuilder;
        }

        public QueryBuilder moreOrEqual(String parameter) {
            whereClauseBuilder.addWhereClause(new WhereData(whereTempInfo.whereField, Operator.MORE_OR_EQUAL, parameter, whereTempInfo.whereType));
            return queryBuilder;
        }

        public QueryBuilder notEqual(String parameter) {
            whereClauseBuilder.addWhereClause(new WhereData(whereTempInfo.whereField, Operator.NOT_EQUAL, parameter, whereTempInfo.whereType));
            return queryBuilder;
        }

        private static class WhereTempInfo {
            private final FieldInfo whereField;
            private final WhereType whereType;

            private WhereTempInfo(FieldInfo whereField, WhereType whereType) {
                this.whereField = whereField;
                this.whereType = whereType;
            }
        }
    }
}
