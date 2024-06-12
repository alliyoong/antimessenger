package com.khanh.livechat.model.dto.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DtoMapper<T,E> {
    private Supplier<T> dtoSupplier;
    private Supplier<E> entitySupplier;

    public DtoMapper(Supplier<T> dtoSupplier, Supplier<E> entitySupplier) {
        this.dtoSupplier = dtoSupplier;
        this.entitySupplier = entitySupplier;
    }

    public <T> T fromEntity(E entity) {
        T dto = (T) dtoSupplier.get();
        BeanUtils.copyProperties(entity, dto, getNullProperties(entity));
        return dto;
    }

    public <T> T fromEntity(E entity, T dto) {
        BeanUtils.copyProperties(entity, dto, getNullProperties(entity));
        return dto;
    }

    public <E> E toEntity(T dto) {
        E entity = (E) entitySupplier.get();
        BeanUtils.copyProperties(dto, entity, getNullProperties(dto));
        return entity;
    }

    public <E> E toEntity(T dto, E entity) {
        BeanUtils.copyProperties(dto, entity, getNullProperties(dto));
        return entity;
    }

    private String[] getNullProperties(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
