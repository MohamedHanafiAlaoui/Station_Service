package com.example.station_service.domain.user.mapper;

import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)

    @Mapping(target = "roleEmploye", ignore = true)
    @Mapping(target = "stationId", ignore = true)
    UserDto toDto(Client client);

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "station.id", target = "stationId")
    @Mapping(source = "roleEmp", target = "roleEmploye")
    UserDto toDto(Employe employe);

    default UserDto toDto(User user) {
        if (user instanceof Client client) {
            return toDto(client);
        }
        if (user instanceof Employe employe) {
            return toDto(employe);
        }
        throw new IllegalArgumentException("Unsupported user type");
    }
}

