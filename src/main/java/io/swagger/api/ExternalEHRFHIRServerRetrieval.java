package io.swagger.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

public class ExternalEHRFHIRServerRetrieval {

    private Patient patient;
    public static void main(String[] args) throws Exception {
        ExternalEHRFHIRServerRetrieval ehr = new ExternalEHRFHIRServerRetrieval();
        Patient p = ehr.retrievePatientFromServer();

    }

    public Patient getPatientFromFHIR() {
        return patient;
    }

    public ExternalEHRFHIRServerRetrieval() {
        retrievePatientFromServer();
    }


    /**
     * Looks up Patients from an online Server and uses the first Person in the list. The link is:
     * https://hapi.fhir.org/baseR4/Patient?_format=json&_pretty=true.
     * @return one Patient.
     */
    private Patient retrievePatientFromServer() {
        // Create a FHIR context and a RESTful client for the HAPI FHIR Test server
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR4");

        String requestURI = "Patient?_format=json&_pretty=true";

        // Perform the GET request to retrieve the patient data
        Bundle response = client.search().byUrl(requestURI).returnBundle(Bundle.class).execute();

        Patient patient = null;
        // Process the response
        List<Bundle.BundleEntryComponent> entries = response.getEntry();
        if (entries != null && !entries.isEmpty()) {
            // Patient resource is the first entry in the bundle
            patient = (Patient) entries.get(0).getResource();

            // Instantiate a new JSON parser
            IParser parser = ctx.newJsonParser();
            // Serialize it
            String serialized = parser.encodeResourceToString(patient);

            System.out.println(serialized);
        } else {
            System.out.println("No resource got back from Test server!");
            // Handle the case where no patient data was returned
        }

        return patient;

    }
}
