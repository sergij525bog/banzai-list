package org.oldman.repositories.bulders.join;

import org.oldman.repositories.bulders.FieldInfo;

class JoinByCollection implements JoinData {
    private final FieldInfo fieldInfo;

    public JoinByCollection(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public JoinByCollection(String tableAliasWithCollection, String collectionAlias) {
        this.fieldInfo = FieldInfo.createWithAlias(tableAliasWithCollection, collectionAlias);
    }

    @Override
    public String buildJoinData() {
        return fieldInfo.getTableAlias() +
                "." +
                fieldInfo.getField() +
                " " +
                fieldInfo.getFieldAlias();
    }

    public static class JoinByCollectionBuilder implements JoinDataBuilder {
        private FieldInfo fieldInfo;

        public JoinByCollectionBuilder fieldInfo(FieldInfo fieldInfo) {
            this.fieldInfo = fieldInfo;
            return this;
        }

        @Override
        public JoinData build() {
            if (fieldInfo == null) {
                throw new IllegalStateException("Builder has not enough info for creating new object");
            }
            return new JoinByCollection(fieldInfo);
        }
    }
}
