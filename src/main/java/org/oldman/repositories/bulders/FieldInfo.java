package org.oldman.repositories.bulders;

public class FieldInfo {
    private final String tableAlias;
    private final String field;

    private final String fieldAlias;

    private FieldInfo(String tableAlias, String field, String fieldAlias) {
        this.tableAlias = tableAlias;
        this.field = field;
        this.fieldAlias = fieldAlias;
    }

    public static FieldInfo createWithoutAlias(String aliasWithField) {
        return createWithAlias(aliasWithField, null);
    }

    public static FieldInfo createWithAlias(String aliasWithField, String fieldAlias) {
        String[] split = aliasWithField.split("\\.");
        int length = split.length;
        if (length < 2 || length > 3) {
            throw new IllegalArgumentException("Cannot split input string on table alias and column name");
        }
        if (length == 3) {
            split[1] = split[1] + "." + split[2];
        }
        return new FieldInfo(split[0], split[1], fieldAlias);
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public String getField() {
        return field;
    }

    public String getFieldAlias() {
        return fieldAlias;
    }
}
