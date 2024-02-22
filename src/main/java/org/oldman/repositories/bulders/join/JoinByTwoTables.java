package org.oldman.repositories.bulders.join;

import org.oldman.repositories.bulders.FieldInfo;
import org.oldman.repositories.bulders.TableInfo;

class JoinByTwoTables implements JoinData {
    private final FieldInfo firstField;
    private final FieldInfo secondField;
    private final TableInfo secondTable;

    public JoinByTwoTables(FieldInfo firstField, FieldInfo secondField, TableInfo secondTable) {
        this.firstField = firstField;
        this.secondField = secondField;
        this.secondTable = secondTable;
    }

    @Override
    public String buildJoinData() {
        return secondTable.getTable() +
                " " +
                secondField.getTableAlias() +
                " on " +
                firstField.getTableAlias() +
                "." +
                firstField.getField() +
                " = " +
                secondField.getTableAlias() +
                "." +
                secondField.getField();
    }

    public static class JoinByTwoTablesBuilder implements JoinDataBuilder {
        private FieldInfo firstField;
        private FieldInfo secondField;
        private TableInfo secondTable;

        public JoinByTwoTablesBuilder firstField(FieldInfo firstField) {
            this.firstField = firstField;
            return this;
        }

        public JoinByTwoTablesBuilder secondField(FieldInfo secondField) {
            this.secondField = secondField;
            return this;
        }

        public JoinByTwoTablesBuilder secondTable(TableInfo secondTable) {
            this.secondTable = secondTable;
            return this;
        }

        @Override
        public JoinData build() {
            if (firstField == null || secondField == null || secondTable == null) {
                throw new IllegalStateException("Builder has not enough info for creating new object");
            }
            return new JoinByTwoTables(firstField, secondField, secondTable);
        }
    }
}
