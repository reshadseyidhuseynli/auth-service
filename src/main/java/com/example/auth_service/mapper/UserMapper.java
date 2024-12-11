package com.example.auth_service.mapper;

import com.example.auth_service.model.dto.request.SignUpRequest;
import com.example.auth_service.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToUser(SignUpRequest request);

}
