package org.oldman.resources;

import org.oldman.entities.Item;
import org.oldman.entities.JoinData;

public interface BaseItemResource<T extends Item<J>, J extends JoinData> extends BaseDataObjectResource<T> {
}
