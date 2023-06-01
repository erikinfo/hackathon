package io.swagger.api;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import org.hl7.fhir.r5.model.ResearchStudy;

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
        researchStudy.setId("123");
        researchStudy.setTitle("Test");
        researchStudy.setName("Hallo");
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
