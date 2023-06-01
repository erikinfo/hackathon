package io.swagger.api;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.SubscriptionTopic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.BundleUtil;
import io.swagger.configuration.SocketImplementation;
import io.swagger.exceptions.NotFoundException;

import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.ResearchStudy;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.ResourceType;
import org.hl7.fhir.r5.model.Subscription;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class SubscriptionTest {

    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionTest.class);

    public static void main(String[] args) {


        SubscriptionTest ig = new SubscriptionTest();
    
        try {
            ig.intermediaryHandler();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }


    public ResearchStudy getLatestResearchStudyByDate(IGenericClient client) {
        // Use the client to search for ResearchStudy resources sorted by date
        Bundle response = client.search()
            .forResource(ResearchStudy.class)
            .sort().descending(Constants.PARAM_LASTUPDATED)
            .returnBundle(Bundle.class)
            .execute();
    
        // Check if there are any entries in the response
        if (!response.getEntry().isEmpty() && response.getEntry().get(0).getResource().getResourceType() == ResourceType.ResearchStudy) {
            // Return the first ResearchStudy in the response
            return (ResearchStudy) response.getEntry().get(0).getResource();
        }
    
        // If there are no entries, return null
        return null;
    }


    public void intermediaryHandler() throws Exception {

        FhirContext ctx = FhirContext.forR5();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir/");
        ArrayList<String> myMessages = new ArrayList<String>();
        myMessages.add("ping 203");

        // Extract ID after 'ping '
        String pingId = null;
        for (String item : myMessages) {
            if (item.startsWith("ping ")) {
                pingId = item.substring(5);
                break;
            }

        }

        String topicID = null;
        if (pingId != null) {
            // Search for the Subscription
            Subscription subscription = client.read()
                                             .resource(Subscription.class)
                                             .withId(pingId)
                                             .execute();
        
            // Get the resource type
            System.out.println(subscription.getTopic());
            topicID = subscription.getTopic();
        } else {
            System.out.println("No ping ID found in the list.");
        }

        if (topicID != null) {

            String searchUrl = "SubscriptionTopic?url="+topicID;
            // Search for the Subscription
            Bundle bundle = client.search()
                                             .forResource(SubscriptionTopic.class)
                                             .where(SubscriptionTopic.URL.matches().value(topicID))
                                             .returnBundle(Bundle.class)
                                             .execute();

            // Convert the Bundle entries into a list of ResearchStudy
            List<IBaseResource> results = new ArrayList<>();
            results.addAll(BundleUtil.toListOfResources(ctx, bundle));

            // Load the subsequent pages
            while (bundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                bundle = client
                .loadPage()
                .next(bundle)
                .execute();
                results.addAll(BundleUtil.toListOfResources(ctx, bundle));
            }
 
            System.out.println("Loaded " + results.size() + " Topics!");

            if (results.size() > 0) {

                SubscriptionTopic st = (SubscriptionTopic) results.get(0);

                System.out.println(st.getResourceTrigger().get(0).getResource());

                if (st.getResourceTrigger().get(0).getResource().equals("http://hl7.org/fhir/StructureDefinition/ResearchStudy")) {
                    ResearchStudy rs = getLatestResearchStudyByDate(client);
                    System.out.println(rs.getId());
                } else {
                    throw new NotFoundException(404, "No ResearchStudy found in Topic!");
                }

            } else {
                throw new NotFoundException(404, "No SubscriptionTopic found!");
            }
        
            //System.out.println(results.getResourceTrigger());
        } else {
            System.out.println("No ping ID found in the list.");
        }
    }


    public void makeTestOnWebSocket() throws Exception {

        String SUBSCRIPTION_TOPIC_TEST_URL = "http://molit.eu/fhir/SubscriptionTopic/clinicaltrials-germany";


        FhirContext ctx = FhirContext.forR5();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir/");
        /*
        * Attach websocket WITH CORRECT CRITERIA
        */

        String endpoint = "ws://localhost:8888/websocket";

    
        WebSocketClient myWebSocketClient = new WebSocketClient();
        SocketImplementation mySocketImplementation = new SocketImplementation("203", EncodingEnum.JSON);

        myWebSocketClient.start();

        URI echoUri = new URI(endpoint);
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        ourLog.info("Connecting to : {}", echoUri);
        Future<Session> connection = myWebSocketClient.connect(mySocketImplementation, echoUri, request);
        Session session = connection.get(2, TimeUnit.SECONDS);

        ourLog.info("Connected to WS: {}", session.isOpen());

        //TimeUnit.SECONDS.sleep(15);


        /*
        * Create a matching resource
        */
        Observation obs = new Observation();
        obs.setStatus(Enumerations.ObservationStatus.FINAL);
        client.create().resource(obs).execute();

        TimeUnit.SECONDS.sleep(12);

        /*
        * Ensure that we receive a ping on the websocket
        */
        System.out.println("Ping count: " + mySocketImplementation.getPingCount());

        System.out.println(mySocketImplementation.getMessages());


        Subscription subscription = new Subscription();

        subscription.getResourceType();


    }

    public void makeNewSubscriptionTopicR5() throws Exception {

        FhirContext ctx = FhirContext.forR5();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir/");

        String endpoint = "ws://localhost:8888/websocket";

        String SUBSCRIPTION_TOPIC_TEST_URL = "http://molit.eu/fhir/SubscriptionTopic/clinicaltrials-germany";
        /*
        * Create topic
        */
        SubscriptionTopic topic = new SubscriptionTopic();

        topic.setUrl(SUBSCRIPTION_TOPIC_TEST_URL);
        topic.setStatus(Enumerations.PublicationStatus.ACTIVE);
        SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = topic.addResourceTrigger();
        trigger.setResource("Observation");
        trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.CREATE);
        trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);

        client.create().resource(topic).execute();

        /*
        * Create subscription
        */
        Subscription subscription = new Subscription();

        subscription.setTopic(SUBSCRIPTION_TOPIC_TEST_URL);
        subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
        subscription.setStatus(Enumerations.SubscriptionStatusCodes.REQUESTED);
        subscription.getChannelType()
        .setSystem("http://terminology.hl7.org/CodeSystem/subscription-channel-type")
        .setCode("websocket");
        subscription.setContentType("application/fhir+json");
        subscription.setEndpoint(endpoint);

        MethodOutcome methodOutcome = client.create().resource(subscription).execute();
        IIdType mySubscriptionId = methodOutcome.getId();

        TimeUnit.SECONDS.sleep(20);

        System.out.println("Subscription ID: " + mySubscriptionId);
        


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
