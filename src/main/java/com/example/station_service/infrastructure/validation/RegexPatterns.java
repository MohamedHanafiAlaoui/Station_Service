package com.example.station_service.infrastructure.validation;

public final class RegexPatterns {

    private RegexPatterns(){};

    public static final String USERNAME =
            "^[a-zA-Z0-9._-]{4,20}$";

    public static final String NAME =
            "^[a-zA-ZÀ-ÿ\\s'-]{2,50}$";

    public static final String RFID =
            "^[A-Fa-f0-9]{8,32}$";

    public static final String PASSWORD =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

    public static final String CODE_POMPE =
            "^[A-Z0-9_-]{3,15}$";
}
