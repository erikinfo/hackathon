package io.swagger.api;

import io.swagger.model.CDSResponse;
import io.swagger.model.CDSServiceInformation;
import io.swagger.model.CDSRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-01-27T10:43:47.246Z")

@Controller
public class CdsServicesApiController implements CdsServicesApi {

    private static final Logger log = LoggerFactory.getLogger(CdsServicesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public CdsServicesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @CrossOrigin
    @ApiOperation(value = "", nickname = "suggestResearchStudies", notes = "Invoke a CDS service offered by this CDS Provider", response = CDSResponse.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success (includes CDS Cards)", response = CDSResponse.class)})
    @RequestMapping(value = "/cds-services/template", produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)
    public ResponseEntity<CDSResponse> cdsTestService(@ApiParam(value = "Body of CDS service request", required = true) @Valid @RequestBody CDSRequest request, @RequestHeader(name = "Authorization") String token, @RequestHeader Map<String, String> headers) {
        logger.info("CDS Hook: template is triggered");
        //ResponseEntity<CDSResponse> cdsResponseResponseEntity = new ResearchStudyQueryService(token).suggestResearchStudies(request, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public ResponseEntity<CDSServiceInformation> cdsServicesGet() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<CDSServiceInformation>(objectMapper.readValue("{  \"bytes\": [],  \"empty\": true}", CDSServiceInformation.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<CDSServiceInformation>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<CDSServiceInformation>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<CDSResponse> cdsServicesIdPost(@ApiParam(value = "The id of this CDS service",required=true) @PathVariable("id") String id, @ApiParam(value = "Body of CDS service request" ,required=true )  @Valid @RequestBody HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<CDSResponse>(objectMapper.readValue("{  \"bytes\": [],  \"empty\": true}", CDSResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<CDSResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<CDSResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
