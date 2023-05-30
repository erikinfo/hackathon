package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.FHIRAuthorization;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CDSRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-01-27T10:43:47.246Z")


public class CDSRequest   {
  //@JsonProperty("hook")
  //private String hook = null;

  //@JsonProperty("hookInstance")
  //private UUID hookInstance = null;

  //@JsonProperty("fhirServer")
 // private String fhirServer = null;

  //@JsonProperty("fhirAuthorization")
  //private FHIRAuthorization fhirAuthorization = null;

  //@JsonProperty("context")
 // private Object context = null;

  @JsonProperty("prefetch")
  private Object prefetch = null;




  //public CDSRequest fhirAuthorization(FHIRAuthorization fhirAuthorization) {
  //  this.fhirAuthorization = fhirAuthorization;
  //  return this;
  //}

  /**
   * Get fhirAuthorization
   * @return fhirAuthorization
  **/
  //@ApiModelProperty(value = "")

  //@Valid

  //public FHIRAuthorization getFhirAuthorization() {
 //   return fhirAuthorization;
  //}

  //public void setFhirAuthorization(FHIRAuthorization fhirAuthorization) {
  //  this.fhirAuthorization = fhirAuthorization;
  //}




  
}

