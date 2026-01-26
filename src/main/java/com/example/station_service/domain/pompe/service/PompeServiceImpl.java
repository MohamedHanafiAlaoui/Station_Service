package com.example.station_service.domain.pompe.service;

import com.example.station_service.domain.pompe.dto.PompeDto;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.pompe.mapper.PompeMapper;
import com.example.station_service.domain.pompe.repository.PompeRepository;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PompeServiceImpl implements  PompeService{

    private final PompeRepository pompeRepository;
    private final PompeMapper pompeMapper;
    private final StationRepository stationRepository;

    @Override
    public void createPompe(PompeDto dto) {
        Pompe pompe = pompeMapper.toEntity(dto);
        pompeRepository.save(pompe);
    }


    @Override
    public PompeDto getPompeById(Long id) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));
        return pompeMapper.toDto(pompe);
    }

    @Override
    public List<PompeDto> getAllPompes() {
        return pompeRepository.findAll()
                .stream()
                .map(pompeMapper::toDto)
                .toList();
    }


    @Override
    public List<PompeDto> getActivePompes() {
        return pompeRepository.findByEnServiceTrue()
                .stream()
                .map(pompeMapper::toDto)
                .toList();
    }


    @Override
    public List<PompeDto> getPompesByStation(Long stationId) {
        return pompeRepository.findByStation_Id(stationId)
                .stream()
                .map(pompeMapper::toDto)
                .toList();
    }

    @Override
    public List<PompeDto> searchPompes(String keyword) {
        return pompeRepository.findAll().stream()
                .filter(p -> p.getCodePompe().toLowerCase().contains(keyword.toLowerCase()))
                .map(pompeMapper::toDto)
                .toList();
    }


    @Override
    public PompeDto updatePompe(Long id, PompeDto dto) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));

        pompe.setCodePompe(dto.getCodePompe());
        pompe.setTypeCarburant(dto.getTypeCarburant());
        pompe.setCapaciteMax(dto.getCapaciteMax());
        pompe.setNiveauActuel(dto.getNiveauActuel());
        pompe.setPrixParLitre(dto.getPrixParLitre());
        pompe.setEnService(dto.getEnService());

        if (dto.getStationId() != null) {
            var station = stationRepository.findById(dto.getStationId())
                    .orElseThrow(() -> new IllegalArgumentException("Station not found: " + dto.getStationId()));
            pompe.setStation(station);
        } else {
            pompe.setStation(null);
        }

        return pompeMapper.toDto(pompeRepository.save(pompe));

    }

    @Override
    public List<PompeDto> filterByNiveau(double niveau) {
        return pompeRepository.findByNiveauActuelGreaterThan(niveau)
                .stream()
                .map(pompeMapper::toDto)
                .toList();
    }





@Override
public void deletePompe(Long id, boolean enService) {
    Pompe pompe = pompeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));
    pompe.setEnService(enService);
    pompeRepository.save(pompe);
}

@Override
public long countActivePompes() {
    return pompeRepository.findByEnServiceTrue().size();
}

 @Override
 public  PompeDto updatePompesell(Long id, double sele)
 {
     Pompe pompe = pompeRepository.findById(id)
             .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));

     if (pompe.getNiveauActuel() < sele) {
         throw new IllegalArgumentException("Pompe " + id + " has insufficient level: " + pompe.getNiveauActuel());
     }

     pompe.setNiveauActuel(pompe.getNiveauActuel() - sele);


     return pompeMapper.toDto(pompeRepository.save(pompe));


 }

 @Override
 public      PompeDto updatePompeAddNive(Long id, double nive)
 {
     Pompe pompe = pompeRepository.findById(id)
             .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));
     if (pompe.getCapaciteMax()  >= pompe.getNiveauActuel() + nive)
     {
         throw new IllegalArgumentException("Pompe " + id + " cannot exceed maximum capacity: " + pompe.getCapaciteMax());
     }

     pompe.setNiveauActuel(pompe.getNiveauActuel() + nive);

     return pompeMapper.toDto(pompeRepository.save(pompe));


 }




}
