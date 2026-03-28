package com.example.station_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@org.springframework.web.bind.annotation.RestController
@SpringBootApplication
@EnableCaching
public class StationServiceApplication {
	public static void main(String[] args) {
        log.info("!!!!! STATION SERVICE STARTING - VERSION: ROBUST_V3_FINAL_CHECK !!!!!");
		SpringApplication.run(StationServiceApplication.class, args);
	}
    @org.springframework.web.bind.annotation.GetMapping("/api/test-robust")
    public String test() {
        log.info(">>> RELIABILITY TEST REACHED - VERSION: V3");
        return "ROBUST_V3_OK";
    }
}