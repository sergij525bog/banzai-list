package org.oldman.repositories.bulders.pojo;

public final class FieldInfo {
    private final String tableAlias;
    private final String field;

    private final String fieldAlias;

    private FieldInfo(String tableAlias, String field, String fieldAlias) {
        this.tableAlias = tableAlias;
        this.field = field;
        this.fieldAlias = fieldAlias;
    }

    public static FieldInfo withoutAlias(String aliasWithField) {
        return withAlias(aliasWithField, null);
    }

    public String tableAliasWithField() {
        return tableAlias + "." + field;
    }

    public String fullFieldInfo() {
        return tableAliasWithField() + " " + fieldAlias;
    }

    public static FieldInfo withAlias(String aliasWithField, String fieldAlias) {
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

    public static class FieldInfoBuilder {
        private String tableAlias;
        private String field;

        private String fieldAlias;

        public FieldInfoBuilder tableAliasWithField(String tableAliasWithField) {
            String[] split = tableAliasWithField.split("\\.");
            int length = split.length;
            if (length < 2 || length > 3) {
                throw new IllegalArgumentException("Cannot split input string on table alias and column name");
            }
            if (length == 3) {
                split[1] = split[1] + "." + split[2];
            }
            this.tableAlias = split[0];
            this.field = split[1];
            return this;
        }

        public FieldInfoBuilder fullFieldInfo(String fullFieldString) {
            String[] split = fullFieldString.split("[. ]");
            int length = split.length;
            if (length != 3) {
                throw new IllegalArgumentException("Cannot split input string on table alias and column name");
            }
            this.tableAlias = split[0];
            this.field = split[1];
            this.fieldAlias = split[2];
            return this;
        }

        public FieldInfoBuilder tableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
            return this;
        }

        public FieldInfoBuilder field(String field) {
            this.field = field;
            return this;
        }

        public FieldInfoBuilder fieldAlias(String fieldAlias) {
            this.fieldAlias = fieldAlias;
            return this;
        }

        public FieldInfo build() {
            return new FieldInfo(tableAlias, field, fieldAlias);
        }
    }
}
