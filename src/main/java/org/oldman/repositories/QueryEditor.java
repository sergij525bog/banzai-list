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
        for (var filter : filters.entrySet()) {
            parameters = parameters.and(filter.getKey(), filter.getValue());
        }
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
