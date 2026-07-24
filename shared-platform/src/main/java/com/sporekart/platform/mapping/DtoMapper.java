package com.sporekart.platform.mapping;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface DtoMapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);

    default List<D> toDtoList(Collection<E> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    default List<E> toEntityList(Collection<D> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    static <T, R> List<R> mapList(Collection<T> source, Function<T, R> mapper) {
        return source.stream().map(mapper).collect(Collectors.toList());
    }
}
