package org.oldman.repositories.bulders;

import java.util.HashMap;
import java.util.Map;

class TaskQueryBuilder {
    private static StringBuilder queryStringBuilder = new StringBuilder();
    private Map<String, Object> params = new HashMap<>();

    public static StringBuilder getBasicQuery() {
        queryStringBuilder.append("select t from TaskList l ").append("join l.tasks t");
        return queryStringBuilder;
    }

    private String buildQueryString() {
        return queryStringBuilder.toString();
    }

    private Map<String, Object> getParams() {
        return params;
    }

    public TaskQueryBuilder addParam(String paramName, Object param) {
        params.put(paramName, param);
        return this;
    }

    /*
    collectionColumn - String name of join collection specified as table.collectionColumn
    * */
    private TaskQueryBuilder join(String collectionColumn) {
        queryStringBuilder
                .append("join on ")
                .append(collectionColumn);
        return this;
    }

    /*
    collectionColumn - String name of join collection specified as table.collectionColumn
    * */
    private TaskQueryBuilder join(String collectionColumn, String as) {
        queryStringBuilder
                .append("join on ")
                .append(collectionColumn)
                .append(" ")
                .append(as);
        return this;
    }

    /*
    joinTable - String name of join table
    joinColumn1 - String name of join column specified as joinTable1.joinColumn1
    joinColumn2 - String name of join column specified as joinTable2.joinColumn2
    * */
    private TaskQueryBuilder joinOn(String joinTable2, String joinColumn1, String joinColumn2) {
        queryStringBuilder
                .append("join ")
                .append(joinTable2)
                .append(" on ")
                .append(joinColumn1)
                .append(" = ")
                .append(joinColumn2);
        return this;
    }

    private TaskQueryBuilder where(String whereClause) {
        queryStringBuilder
                .append("where ")
                .append(whereClause);
        return this;
    }

    private TaskQueryBuilder and(String whereClause) {
        queryStringBuilder
                .append(" and ")
                .append(whereClause);
        return this;
    }

    private TaskQueryBuilder or(String whereClause) {
        queryStringBuilder
                .append(" or ")
                .append(whereClause);
        return this;
    }
}
