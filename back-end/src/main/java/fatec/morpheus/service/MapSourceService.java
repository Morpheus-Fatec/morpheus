package fatec.morpheus.service;

import org.springframework.beans.factory.annotation.Autowired;

import fatec.morpheus.entity.MapSource;
import fatec.morpheus.repository.MapSourceRepository;

public class MapSourceService {

    @Autowired
    private MapSourceRepository mapSourceRepository;

    public void saveMapSource(MapSource mapSource) {
        mapSourceRepository.save(mapSource);
    }

    public void findHTMLTags(MapSource mapSource) {
    }

}
