package com.example.station_service.domain.user.mapper;


import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);




}

