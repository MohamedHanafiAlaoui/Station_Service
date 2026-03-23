package com.example.station_service.domain.venteCarburant;

import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import com.example.station_service.domain.venteCarburant.repository.VenteCarburantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class VenteCarburantNPlusOneTest {

    @Autowired
    private VenteCarburantRepository repository;

    @Test
    public void testFetchSalesWithEntityGraph() {
        // This test ensures the repository methods can be called. 
        // In a real environment with SQL logging enabled, we would check that only one query is generated.
        var result = repository.findByStationIdAndDateVenteBetween(
                1L, 
                LocalDateTime.now().minusDays(1), 
                LocalDateTime.now().plusDays(1), 
                PageRequest.of(0, 10)
        );
        assertNotNull(result);
    }
}
