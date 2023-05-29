package io.swagger.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.ResearchStudy;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class LeoDerBoss {


    public static void main(String[] args)  {

        FhirContext ctx = FhirContext.forR5();
        String directoryPath = "ResearchStudy.json";
        File file = new File(directoryPath);

        ResearchStudy researchstudy = null;
        String input;
        try {
            input = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            // Instantiate a new parser
            IParser parser = ctx.newJsonParser();

            // Parse it
            researchstudy = parser.parseResource(ResearchStudy.class, input);

            // Prints the full resource: 
            //System.out.println(parser.setPrettyPrint(true).encodeResourceToString(researchstudy));


            //researchstudy.getId();

            // TO DO: Prints the Fevir Link along with some other info 
            System.out.println(researchstudy.getRecruitment());


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        

        
    }
    
}
