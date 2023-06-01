package io.swagger.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.r5.model.Group;
import org.hl7.fhir.r5.model.ResearchStudy;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class LeoDerBoss {


    public static void main(String[] args)  {

        FhirContext ctx = FhirContext.forR5();
        //String directoryPath = "/other examples/ResearchStudy.json";
        //File file = new File(directoryPath);

        String directoryPath2 = "./Group-TrialInclusionGroupExample.json";
        File file = new File(directoryPath2);
        Group group = null;
        String input1;
        try {
            input1 = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            // Instantiate a new parser
            IParser parser = ctx.newJsonParser();

            // Parse it
            group = parser.parseResource(Group.class, input1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        ResearchStudy researchstudy = null;
        String input;
        try {
            input = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            // Instantiate a new parser
            IParser parser2 = ctx.newJsonParser();

            // Parse it
            researchstudy = parser2.parseResource(ResearchStudy.class, input);

            // Prints the full resource: 
            //System.out.println(parser.setPrettyPrint(true).encodeResourceToString(researchstudy));


            //researchstudy.getId();

            // TO DO: Prints the Fevir Link along with some other info 
            System.out.println(researchstudy.getRecruitment());


            // Create an instance of RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        
            // Create request body
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("functionid", "getresourcejson"); // Don't change!
            map.add("resourceid", "12345"); // Input the ID you get from the Recruitment Object from the Research Study
            map.add("apiToken", "████████████"); // Your code 
        
            // Build the request
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
            // Make the request
            ResponseEntity<String> response = restTemplate.exchange("https://api.fevir.net", HttpMethod.GET, request, String.class);

            // Print the response
            System.out.println(response.getBody());




        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        

        
    }
    
}
