package org.oldman.models.mappers;

import org.oldman.entities.ListWithProduct;
import org.oldman.entities.Product;
import org.oldman.models.ProductDto;

public class ListWithProductToDtoMapper implements IEntityToDtoMapper<Product, ListWithProduct, ProductDto> {
    @Override
    public ProductDto toDto(ListWithProduct joinTable) {
        ProductDto dto = new ProductDto();
        dto.setId(joinTable.getId());
        dto.setDone(joinTable.isDone());
        dto.setCount(joinTable.getCount());
        dto.setEndDate(joinTable.getEndDate());
        dto.setPriority(joinTable.getPriority());
        dto.setRetrieved(joinTable.isRetrieved());
        dto.setWeight(joinTable.getWeight());

        Product product = joinTable.getProduct();
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setProductCategory(product.getProductCategory());

        return dto;
    }

    @Override
    public ListWithProduct toEntity(ProductDto dto) {
        ListWithProduct listWithProduct = new ListWithProduct();

        listWithProduct.setId(dto.getId());
        listWithProduct.setDone(dto.isDone());
        listWithProduct.setCount(dto.getCount());
        listWithProduct.setEndDate(dto.getEndDate());
        listWithProduct.setPriority(dto.getPriority());
        listWithProduct.setRequired(dto.isRetrieved());
        listWithProduct.setWeight(dto.getWeight());

        Product product = new Product();
        product.setId(dto.getProductId());
        product.setName(dto.getName());
        product.setProductCategory(dto.getProductCategory());
        listWithProduct.setProduct(product);

        return listWithProduct;
    }
}
