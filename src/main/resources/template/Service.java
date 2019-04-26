package jetchart.template;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ::NAME::Service extends BaseIdService<::ENTITY::, ::TYPEID::, ::DTO::, ::NAME::Business, BaseModelMapper> {

    @Override
    public ::DTO:: convertToDto(::ENTITY:: entity) {
        return entity;
    }

    @Override
    public ::ENTITY:: convertToEntity(::DTO:: dto) {
        return dto;
    }

    @Override
    public Page<::ENTITY::> convertToPageDto(Page<::ENTITY::> objetosEntity) {
        return objetosEntity;
    }

}

