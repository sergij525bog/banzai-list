package org.oldman.repositories;

import io.quarkus.panache.common.Parameters;

import java.util.List;
import java.util.Map;

class QueryEditor {
    private String query;
    private Parameters parameters;
    private final String tableAlias;

    private QueryEditor(String query, Parameters parameters, String tableAlias) {
        this.query = query;
        this.parameters = parameters;
        this.tableAlias = tableAlias;
    }

    public static QueryEditor getEditor(
            String baseQuery,
            Parameters parameters,
            String tableAlias,
            Map<String, Object> filters,
            List<String> sortBy,
            String sortOrder) {
        if (filters != null && !filters.isEmpty()) {
            QueryEditor editor = new QueryEditor(baseQuery, parameters, tableAlias);
            return editor
                    .addFiltering(filters)
                    .addSorting(sortBy, sortOrder);
        }
        return null;
    }

    public static QueryEditor getEditor(String query, Parameters parameters, String tableAlias, final Map<String, Object> updateParams) {
        if (updateParams == null || updateParams.isEmpty()) {
            return null;
        }

        QueryEditor editor = new QueryEditor(query, parameters, tableAlias);
        return editor.addUpdating(updateParams);
    }

    private QueryEditor addUpdating(final Map<String, Object> updateParams) {
        this.addUpdateParameters(updateParams);
        this.addUpdateStringToQuery(updateParams);

        return this;
    }

    //        "lt.priority = :priority"
    private void addUpdateStringToQuery(Map<String, Object> updateParams) {
        StringBuilder updateString = new StringBuilder(query);
        int i = 0;
        for (var param: updateParams.keySet()) {
            i++;
            updateString
                    .append(" ")
                    .append(tableAlias)
                    .append(".")
                    .append(param)
                    .append(" = :")
                    .append(param)
                    .append(i < updateParams.size() ? ", " : " ");
        }

        System.out.println(updateString);

        query = updateString.toString();
    }

    private void addUpdateParameters(Map<String, Object> updateParams) {
        updateParams.forEach((key, value) -> parameters = parameters.and(key, value));
    }

    private QueryEditor addFiltering(final Map<String, Object> filters) {
        this.addFilterParameters(filters);
        this.addFilterStringToQuery(filters);

        return this;
    }

    private QueryEditor addSorting(final List<String> sortBy, String sortOrder) {
        addSortingToQuery(sortBy, sortOrder);

        return this;
    }

    private void addFilterStringToQuery(final Map<String, Object> filters) {
        StringBuilder filterString = new StringBuilder(query);
        filters.forEach((key, value) -> {
            if (value != null) {
                filterString.append(" and ")
                        .append(tableAlias)
                        .append(".")
                        .append(key)
                        .append(" = :")
                        .append(key);
            }
        });
        query = filterString.toString();
    }

    private void addFilterParameters(final Map<String, Object> filters) {
        filters.forEach((key, value) -> parameters = parameters.and(key, value));
    }

    private void addSortingToQuery(final List<String> sortBy, String sortOrder) {
        if (sortBy == null || sortBy.isEmpty()) {
            return;
        }

        String collect = String.join(", ", sortBy);

        if (sortOrder == null) {
            sortOrder = "desc";
        }
        query += " order by " + collect + " " + sortOrder;
    }

    public String getQuery() {
        return query;
    }

    public Parameters getParameters() {
        return parameters;
    }
}
