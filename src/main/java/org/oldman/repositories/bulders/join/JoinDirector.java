package org.oldman.repositories.bulders.join;

import org.oldman.repositories.bulders.FieldInfo;
import org.oldman.repositories.bulders.TableInfo;

public class JoinDirector {
    public static JoinData createJoinByCollection(FieldInfo fieldInfo) {
        return new JoinByCollection.JoinByCollectionBuilder()
                .fieldInfo(fieldInfo)
                .build();
    }

    public static JoinData createJoinByTwoTables(FieldInfo firstField, FieldInfo secondField, TableInfo secondTable) {
        return new JoinByTwoTables.JoinByTwoTablesBuilder()
                .firstField(firstField)
                .secondField(secondField)
                .secondTable(secondTable)
                .build();
    }
}
