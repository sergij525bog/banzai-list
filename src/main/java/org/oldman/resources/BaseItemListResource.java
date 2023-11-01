package org.oldman.resources;

import jakarta.ws.rs.core.Response;
import org.oldman.entities.ItemList;

public interface BaseItemListResource<T extends ItemList> extends BaseDataObjectResource<T> {
    Response addItem(Long listId, Long itemId);
    Response deleteItem(Long listId, Long itemId);
    Response clearList(Long id);
}
