package io.swagger.api;



import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r5.model.SubscriptionTopic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;


public class IGLoader {

    public static void main(String[] args) {

        // ResearchStudy rs =;

        // rs.getEnrollment()

        IGLoader ig = new IGLoader();
    
        //ig.pushFhirResources("/home/erik/Downloads/test-ig2");

        ig.searchSubscriptionTopics();
        
    }

    public void searchSubscriptionTopics() {
        
        FhirContext ctx = FhirContext.forR5();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir/");
        // Search for all Subscription resources
        org.hl7.fhir.r5.model.Bundle response = null;
        try {
            response = client.search()
                    .forResource(SubscriptionTopic.class)
                    .returnBundle(org.hl7.fhir.r5.model.Bundle.class)
                    .execute();
        } catch (BaseServerResponseException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        // Print the results
        if (response != null) {
            for (org.hl7.fhir.r5.model.Bundle.BundleEntryComponent entry : response.getEntry()) {
                SubscriptionTopic subscription = (SubscriptionTopic) entry.getResource();
                System.out.println(subscription.toString());
                System.out.println(subscription.getId());
            }
        }

    }


    public void pushFhirResources(String directoryPath) {


        try {
            File folder = new File(directoryPath);
            File[] files = folder.listFiles();

            FhirContext ctx = FhirContext.forR4();
            IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir/");
    
            // Create a new Bundle resource
            Bundle bundle = new Bundle();
            bundle.setType(Bundle.BundleType.TRANSACTION);
    
            for (File file : files) {
                if (file.isFile()) {
                    // Parse the resource
                    String resourceString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    IBaseResource resource = ctx.newJsonParser().parseResource(resourceString);
                    // Add the resource to the bundle
                    Bundle.BundleEntryComponent bundleEntry = new Bundle.BundleEntryComponent();
                    bundleEntry.setResource((Resource) resource)
                            .getRequest()
                            .setMethod(Bundle.HTTPVerb.POST)
                            .setUrl(resource.fhirType());
                    bundle.addEntry(bundleEntry);
                }
            }
    
            // Submit the transaction to the server
            Bundle resp = client.transaction().withBundle(bundle).execute();
    
            // Log the response
            System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
