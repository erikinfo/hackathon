package io.swagger.api;



import java.io.File;
import java.net.URI;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r5.model.SubscriptionTopic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Subscription;
import ca.uhn.fhir.rest.api.EncodingEnum;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class IGLoader {

    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IGLoader.class);

    public static void main(String[] args) {

        // ResearchStudy rs =;

        // rs.getEnrollment()

        IGLoader ig = new IGLoader();
    
        //ig.pushFhirResources("/home/erik/Downloads/test-ig2");

        try {
            ig.makeTestOnWebSocket();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void makeTestOnWebSocket() throws Exception {

        String SUBSCRIPTION_TOPIC_TEST_URL = "http://molit.eu/fhir/SubscriptionTopic/clinicaltrials-germany";


        FhirContext ctx = FhirContext.forR5();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir/");
        /*
        * Attach websocket
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

        TimeUnit.SECONDS.sleep(15);


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
        System.out.println("Ping count: " + mySocketImplementation.myPingCount);


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
