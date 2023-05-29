package io.swagger.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.model.CDSResponse;
import io.swagger.model.CDSServiceInformation;
import io.swagger.model.CDSRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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


    

    @CrossOrigin // cross-domain-communication
    @ApiOperation(value = "", nickname = "suggestionToPatientAStudy", notes = "Invoke a CDS service offered by this CDS Provider", response = CDSResponse.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success (includes CDS Cards)", response = CDSResponse.class)})
    // RequestMapping = does this method when POST
    @RequestMapping(value = "/cds-services/trials", produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST) // @RequestHeader(name = "Authorization") String token,
    public ResponseEntity<CDSResponse> cdsTestService(@ApiParam(value = "Body of CDS service request", required = true) @Valid @RequestBody CDSRequest request, @RequestHeader Map<String, String> headers) {
        logger.info("CDS Hook: template is triggered");
        System.out.println(request.toString());
        LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<>();
        jsonObject = (LinkedHashMap<String, Object>) request.getPrefetch();
        System.out.println(jsonObject);
        LinkedHashMap<String, Object> bs = (LinkedHashMap<String, Object>) jsonObject.get("studies");
        System.out.println(bs.get("resourceType"));


        //InternalEHRFHIRServerRetrieval iehr = new InternalEHRFHIRServerRetrieval();
        //Patient patient = iehr.getPatientFromFHIR();
        //patient.getBirthDate();

        // INSERT: Our basic logic that for example when a Patient is old then a specific research study is good (must not be perfect at this point)

        Card c = new Card();
        c.setSummary("Example Card");
        // ...

        CDSResponse cdsR = new CDSResponse();
        List<Card> l = new ArrayList<Card>();
        l.add(c);
        cdsR.setCards(l);


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
