package io.swagger.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@Configuration
public class FhirClient {

    @Value("${fhir.server.url}")
    private String fhirServerUrl;
    
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR5();
    }

    @Bean
    public IGenericClient fhirContextClient(FhirContext ctx) {
        return ctx.newRestfulGenericClient(fhirServerUrl);
    }


}
