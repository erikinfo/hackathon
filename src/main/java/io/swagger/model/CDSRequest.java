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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-06-01T06:38:14.072Z")


public class CDSRequest   {
  @JsonProperty("hook")
  private String hook = null;

  @JsonProperty("hookInstance")
  private UUID hookInstance = null;

  @JsonProperty("fhirServer")
  private String fhirServer = null;

  @JsonProperty("fhirAuthorization")
  private FHIRAuthorization fhirAuthorization = null;

  @JsonProperty("context")
  private Object context = null;

  @JsonProperty("prefetch")
  private Object prefetch = null;

  public CDSRequest hook(String hook) {
    this.hook = hook;
    return this;
  }

  /**
   * The hook that triggered this CDS Service call.
   * @return hook
  **/
  @ApiModelProperty(required = true, value = "The hook that triggered this CDS Service call.")
  @NotNull


  public String getHook() {
    return hook;
  }

  public void setHook(String hook) {
    this.hook = hook;
  }

  public CDSRequest hookInstance(UUID hookInstance) {
    this.hookInstance = hookInstance;
    return this;
  }

  /**
   * Get hookInstance
   * @return hookInstance
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public UUID getHookInstance() {
    return hookInstance;
  }

  public void setHookInstance(UUID hookInstance) {
    this.hookInstance = hookInstance;
  }

  public CDSRequest fhirServer(String fhirServer) {
    this.fhirServer = fhirServer;
    return this;
  }

  /**
   * Get fhirServer
   * @return fhirServer
  **/
  @ApiModelProperty(value = "")


  public String getFhirServer() {
    return fhirServer;
  }

  public void setFhirServer(String fhirServer) {
    this.fhirServer = fhirServer;
  }

  public CDSRequest fhirAuthorization(FHIRAuthorization fhirAuthorization) {
    this.fhirAuthorization = fhirAuthorization;
    return this;
  }

  /**
   * Get fhirAuthorization
   * @return fhirAuthorization
  **/
  @ApiModelProperty(value = "")

  @Valid

  public FHIRAuthorization getFhirAuthorization() {
    return fhirAuthorization;
  }

  public void setFhirAuthorization(FHIRAuthorization fhirAuthorization) {
    this.fhirAuthorization = fhirAuthorization;
  }

  public CDSRequest context(Object context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Object getContext() {
    return context;
  }

  public void setContext(Object context) {
    this.context = context;
  }

  public CDSRequest prefetch(Object prefetch) {
    this.prefetch = prefetch;
    return this;
  }

  /**
   * Get prefetch
   * @return prefetch
  **/
  @ApiModelProperty(value = "")


  public Object getPrefetch() {
    return prefetch;
  }

  public void setPrefetch(Object prefetch) {
    this.prefetch = prefetch;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CDSRequest cdSRequest = (CDSRequest) o;
    return Objects.equals(this.hook, cdSRequest.hook) &&
        Objects.equals(this.hookInstance, cdSRequest.hookInstance) &&
        Objects.equals(this.fhirServer, cdSRequest.fhirServer) &&
        Objects.equals(this.fhirAuthorization, cdSRequest.fhirAuthorization) &&
        Objects.equals(this.context, cdSRequest.context) &&
        Objects.equals(this.prefetch, cdSRequest.prefetch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hook, hookInstance, fhirServer, fhirAuthorization, context, prefetch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CDSRequest {\n");
    
    sb.append("    hook: ").append(toIndentedString(hook)).append("\n");
    sb.append("    hookInstance: ").append(toIndentedString(hookInstance)).append("\n");
    sb.append("    fhirServer: ").append(toIndentedString(fhirServer)).append("\n");
    sb.append("    fhirAuthorization: ").append(toIndentedString(fhirAuthorization)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    prefetch: ").append(toIndentedString(prefetch)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
