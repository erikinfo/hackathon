package io.swagger.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/** 
 * This class is used to test the FHIR parser for R4 for files that are under the specified path.
 * 
 * It also interacts as the starting point for evaluating CQL files for the CDS Hooks project using the built-in Evaluator.
*/
public class FhirCqlMain {
    private static final FhirContext fhirContext = FhirContext.forR4();
    private static final String FHIR_PATH_UNDER_RESOURCES_FOLDER = "data/tests/";

    public static void main(String[] args) {
        try {
            Group group = loadFhirResource("Group-TrialInclusionGroupExample.json", Group.class);
            System.out.println(group.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T extends IBaseResource> T loadFhirResource(String resourceName, Class<T> type) throws IOException {
        String resourceData = loadFileContent(FHIR_PATH_UNDER_RESOURCES_FOLDER + resourceName);
        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(type, resourceData);
    }



    private static String loadFileContent(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);
    }
}

