package com.example.station_service.infrastructure.exception.custom;

public class ResourceNotFoundException extends  RuntimeException{

    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
