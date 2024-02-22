package org.oldman.repositories.bulders;

public class TableInfo {
    private final String table;
    private final String alias;

    public TableInfo(String table, String alias) {
        this.table = table;
        this.alias = alias;
    }

    public String getTable() {
        return table;
    }

    public String getAlias() {
        return alias;
    }
}
