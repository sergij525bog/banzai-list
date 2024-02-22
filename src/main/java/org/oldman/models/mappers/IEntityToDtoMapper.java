package org.oldman.models.mappers;

import org.oldman.entities.Item;
import org.oldman.entities.JoinData;
import org.oldman.models.Dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IEntityToDtoMapper<I extends Item<J>, J extends JoinData, D extends Dto> {
    D toDto(J joinTable);
    J toEntity(D dto);

    default Stream<D> toDtoSteam(I entity) {
        return entity.getJoinTableData().stream().map(this::toDto);
    }

    default List<D> toDtoList(I entity) {
        return toDtoSteam(entity).collect(Collectors.toList());
    }

    default Stream<D> toDtoStream(Collection<J> joinTableCollection) {
        return joinTableCollection.stream().map(this::toDto);
    }

    default Stream<D> toDtoStream(Stream<I> itemStream) {
        return itemStream.flatMap(item -> item.getJoinTableData().stream()).map(this::toDto);
    }

    default List<D> toDtoList(Collection<J> joinTableCollection) {
        return toDtoStream(joinTableCollection).collect(Collectors.toList());
    }

    default List<D> toDtoList(Stream<I> itemStream) {
        return toDtoStream(itemStream).collect(Collectors.toList());
    }

    default Stream<J> toEntityStream(List<D> dtos) {
        return dtos.stream().map(this::toEntity);
    }

    default List<J> toEntityList(List<D> dtos) {
        return toEntityStream(dtos).collect(Collectors.toList());
    }
}
