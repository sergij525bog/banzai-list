package org.oldman.entities;

import java.util.Collection;

public interface Item<J extends JoinData> extends DataObject {
    Collection<J> getJoinTableData();
}
