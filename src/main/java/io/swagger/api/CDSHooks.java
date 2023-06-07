package io.swagger.api;

import java.text.SimpleDateFormat;
import java.util.*;

import org.hl7.fhir.r5.model.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import io.swagger.model.CDSRequest;
import io.swagger.model.CDSResponse;
import io.swagger.model.FHIRAuthorization;
import io.swagger.model.FHIRAuthorization.TokenTypeEnum;

public class CDSHooks {

    public static void main(String[] args) {
        // Test the CDSHooks class

        CDSHooks hooks = new CDSHooks();
        ResearchStudy researchStudy = new ResearchStudy();

        //following lines creates a condition for test purposes. Keep an eye when loading a research study direct.
        // Create a CodeableConcept representing the condition being studied
        CodeableConcept condition = new CodeableConcept();
        // Create a Coding representing the SNOMED code for the condition
        Coding coding = new Coding();
        coding.setSystem("http://snomed.info/sct");
        coding.setCode("363358000"); // fictitious code created for demonstration purposes
        coding.setDisplay("Solid Tumors with Oncogenic Alterations and High Tumor Mutational Burden");
        // Add the Coding to the CodeableConcept
        condition.addCoding(coding);
        // Create a list of CodeableConcepts and add the condition
        List<CodeableConcept> conditions = new ArrayList<>();
        conditions.add(condition);
        //finished condition

        Period period = new Period();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Set the start date
            String startDateString = "2023-01-01";
            Date startDate = dateFormat.parse(startDateString);
            period.setStart(startDate);

            // Set the end date
            String endDateString = "2023-09-09 ";
            Date endDate = dateFormat.parse(endDateString);
            period.setEnd(endDate);

            researchStudy.setPeriod(period);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CodeableConcept> regions = new ArrayList<>();
        // Create a CodeableConcept for a specific region
        CodeableConcept region1 = new CodeableConcept();
        Coding coding1 = new Coding();
        coding1.setSystem("http://example.com/geographic-areas");
        coding1.setCode("US");
        coding1.setDisplay("United States");
        region1.addCoding(coding1);
        regions.add(region1);
        // Create another CodeableConcept for a different region
        CodeableConcept region2 = new CodeableConcept();
        Coding coding2 = new Coding();
        coding2.setSystem("http://example.com/geographic-areas");
        coding2.setCode("CA");
        coding2.setDisplay("Canada");
        region2.addCoding(coding2);
        regions.add(region2);

        researchStudy.setId("123");
        researchStudy.setTitle("Example from CDSHooks: Tumor-Agnostic Precision Immuno-Oncology and Somatic Targeting Rational for You (TAPISTRY) Platform Study");
        researchStudy.setName("Example-Test-purpose");
        researchStudy.setCondition(conditions);
        researchStudy.setRegion(regions);
        researchStudy.setDescriptionSummary("This study investigates the associations of malignant pancreatic neoplasms in connection with adenocarcinomas.");
        //researchStudy.setStatus(Enumerations.PublicationStatus.valueOf("active"));//prob. not necessary here, instead: researchStudy.getStatus();
        //researchStudy.setRecruitment();  //inclusion and exclusion criterias
        hooks.sendRequest(researchStudy);
    }

    private final String URI = "http://localhost:8082/cds-services/trials";

    public void sendRequest(ResearchStudy researchStudy) {
        // Create a FHIR parser
        FhirContext ctx = FhirContext.forR5Cached();
        IParser parser = ctx.newJsonParser();

        // Convert the ResearchStudy to a JSON string
        String researchStudyJson = parser.encodeResourceToString(researchStudy);

        // Create a CDSRequest
        CDSRequest request = new CDSRequest();
        request.setHook("suggestResearchStudies");
        request.setHookInstance(UUID.fromString("88f0dc37-e374-4bbd-8927-124b287ca092"));
        request.setFhirServer("https://vitu.intern.zvpm.de/fhir");

        FHIRAuthorization auth = new FHIRAuthorization();
        auth.setAccessToken("researchStudyJson");
        auth.setTokenType(TokenTypeEnum.BEARER);
        auth.setExpiresIn(null);

        // Set userId in context
        Map<String, String> context = Map.of("userId", "Practitioner/123");
        request.setContext(context);

        // Insert the ResearchStudy JSON into the prefetch
        request.setPrefetch(researchStudyJson);

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity from the request and headers
        HttpEntity<CDSRequest> entity = new HttpEntity<>(request, headers);

        // Send the HTTP POST request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CDSResponse> response = restTemplate.exchange(URI, HttpMethod.POST, entity, CDSResponse.class);

        // Print the response
        System.out.println("Response: " + response);
    }
    
}
