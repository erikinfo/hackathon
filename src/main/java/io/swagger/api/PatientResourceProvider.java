package io.swagger.api;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

@RestController
@RequestMapping("/fhir/Patient")
public class PatientResourceProvider {


    private Map<Integer, LinkedList<Patient>> myIdToPatientVersions;
    private int myNextId;

    public PatientResourceProvider() {
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public Patient read(@PathVariable("id") Long id) {
        Deque<Patient> patients = myIdToPatientVersions.get(id);
        if (patients == null || patients.isEmpty()) {
            throw new ResourceNotFoundException("Patient with ID " + id + " not found");
        }
        return patients.getLast();
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public Patient create(@RequestBody Patient patient) {
        // Assign an ID
        int resourceId = myNextId++;
        patient.setId(Long.toString(resourceId));

        // Add to the resource map
        LinkedList<Patient> list = new LinkedList<>();
        list.add(patient);
        myIdToPatientVersions.put(resourceId, list);

        return patient;
    }
}