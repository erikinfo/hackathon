package io.swagger.api;

import io.swagger.model.CDSRequest;
import io.swagger.model.CDSResponse;
import io.swagger.model.CDSService;
import io.swagger.model.CDSServiceInformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.model.Card;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-01-27T10:43:47.246Z")

@Controller
public class CdsServicesApiController implements CdsServicesApi {

    private static final Logger log = LoggerFactory.getLogger(CdsServicesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private CDSServiceRepository cdsServiceRepository;

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
        logger.info("CDS Hook: template is triggered");                                                     // 
        logger.info("CDS Hook: request is " + request.toString());

        // Look at presentation and build Bundle 

        System.out.println(request.toString());
      


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


    @RequestMapping(value = "/cds-services", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity cdsServicesGet() {
        List<CDSService> allServices = cdsServiceRepository.getAllServices();
        CDSServiceInformation serviceInformation = new CDSServiceInformation();
        serviceInformation.setServices(allServices);
        return new ResponseEntity<>(serviceInformation.toString(),HttpStatus.OK);
    }

    @RequestMapping(value = "/cds-services/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<CDSResponse> cdsServicesIdPost(@PathVariable("id") String id,
                                                     @Valid @RequestBody CDSService service) {

        
        cdsServiceRepository.saveService(service);
        return new ResponseEntity<CDSResponse>(HttpStatus.OK);
    }
        

}
