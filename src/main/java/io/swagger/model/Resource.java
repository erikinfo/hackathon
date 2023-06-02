package io.swagger.model;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;


@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-01-27T10:43:47.246Z")
public class Resource {
    @JsonProperty("resourceType")
    private String type = null;

    public void setResourceType(String type) {
        this.type = type;
    }
    
}
