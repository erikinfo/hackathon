package io.swagger.api;

import org.springframework.stereotype.Repository;

import io.swagger.model.CDSService;

import java.util.*;

@Repository
public class CDSServiceRepository {
    
    private Map<String, CDSService> cdsServices;

    public CDSServiceRepository() {
        this.cdsServices = new HashMap<>();
    }

    public CDSService getService(String id) {
        return cdsServices.get(id);
    }

    public void saveService(CDSService service) {
        cdsServices.put(service.getId(), service);
    }

    public void deleteService(String id) {
        cdsServices.remove(id);
    }

    public List<CDSService> getAllServices() {
        return new ArrayList<>(cdsServices.values());
    }
}
