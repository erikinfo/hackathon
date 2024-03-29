package io.swagger.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.ResearchStudy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.BundleUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.model.Action;
import io.swagger.model.Action.TypeEnum;
import io.swagger.model.CDSRequest;
import io.swagger.model.CDSResponse;
import io.swagger.model.CDSService;
import io.swagger.model.CDSServiceInformation;
import io.swagger.model.Card;
import io.swagger.model.Card.IndicatorEnum;
import io.swagger.model.Card.SelectionBehaviorEnum;
import io.swagger.model.Link;
import io.swagger.model.Resource;
import io.swagger.model.Source;
import io.swagger.model.Suggestion;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-01-27T10:43:47.246Z")

@Controller
public class CdsServicesApiController implements CdsServicesApi {

    private static final Logger log = LoggerFactory.getLogger(CdsServicesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private Map<String, Card> storedData = new HashMap<>();

    private final FhirContext ctx;
    private final IGenericClient client;
    
    @Autowired
    private CDSServiceRepository cdsServiceRepository;

    @Autowired
    public CdsServicesApiController(ObjectMapper objectMapper, HttpServletRequest request, IGenericClient fhirClient, FhirContext ctx) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.client = fhirClient;
        this.ctx = ctx;

         ResearchStudy rs = new ResearchStudy();
         rs.setRecruitment(new ResearchStudy.ResearchStudyRecruitmentComponent().setEligibility(null));
    }



    @CrossOrigin // cross-domain-communication
    @ApiOperation(value = "", nickname = "suggestionToPatientAStudy", notes = "Invoke a CDS service offered by this CDS Provider", response = CDSResponse.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success (includes CDS Cards)", response = CDSResponse.class)})
    // RequestMapping = does this method when POST
    @RequestMapping(value = "/cds-services/trials", produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST) // @RequestHeader(name = "Authorization") String token,
    public ResponseEntity<Card> cdsTestService(@ApiParam(value = "Body of CDS service request", required = true) @Valid @RequestBody CDSRequest request, @RequestHeader Map<String, String> headers) {
        
        logger.info("CDS Hook: template is triggered");
        logger.info("CDS Hook: request is " + request.toString());

        // Instantiate a new parser
        IParser parser = ctx.newJsonParser();

        // Parse it
        ResearchStudy researchStudy = parser.parseResource(ResearchStudy.class, request.getPrefetch().toString());
        logger.info(researchStudy.toString());


        // PSEUDO CODE: if any of: then OR if all then AND

        // Lets pretend we get these values since HAPI 6.6 currently does not support the eligibility criteria
        String observationCodes = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl|C49164,http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl|C114879,http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl|C102869";
        String conditionCode = "http://snomed.info/sct|443493003";

        List<Patient> patients = searchForPatient(observationCodes, conditionCode);



        // Look at presentation and build Bundle

        // Request to JSON => In dem JSON Prefetch lesen =>

        //ctx.

        //ResearchStudy

        //  search() => Patient...

        System.out.println(request.toString());

        Card c = new Card();
        c.setSummary(researchStudy.getTitle());//"Example: Info Card"
        c.setDetail("Delivers an info card containing the most important information and a list of eligible patients");
        c.setIndicator(IndicatorEnum.INFO);

        Source s = new Source();
        s.setIcon("https://molit.eu/wp-content/uploads/2017/01/favicon.png");
        s.setUrl("https://molit.eu/");
        s.setLabel("Molit Institute");

        ArrayList<Link> links = new ArrayList<Link>();
        Link link = new Link();
        s.setLabel("Link for the Research Study:");
        link.setLabel("Adjuvant Aspirin Treatment in PIK3CA Mutated Colon Cancer Patients. A Randomized, Double-blinded, Placebo-controlled, Phase III Trial");
        link.setUrl("https://clinicaltrials.gov/ct2/show/NCT02467582");
        link.setType("Clinical Trial");
        //Alternative for smartlink app card
        Link smartlink = new Link();
        s.setLabel("Link for the App:");
        smartlink.setLabel("HealthGPT APP");
        smartlink.setUrl("https://www.healthgptapp.com/");//TODO: change link with the one were the interface of the app can be shown (prototype)

        links.add(0, link);
        links.add(1,smartlink);

        c.setLinks(links);
        c.setSelectionBehavior(SelectionBehaviorEnum.AT_MOST_ONE);



        //If the UI used in this project could show other cards as well, it would be able to show this smart app link card.
        //Smartlink app card : provides link for the health gpt Appc(interactive way of providing information about a study)
        //for patient and doctor.
        /*
        Card smartlinkapp = new Card();
        smartlinkapp.setDetail("Link for the HealthGPT APP");
        Link smartlink = new Link();
        smartlink.setLabel("HealthGPT APP");
        link.setUrl("https://www.healthgptapp.com/");
        */


        Suggestion suggestions = new Suggestion();
        //suggestions.setLabel("##### Title: Radio-Immunotherapy Before Cystectomy in Locally Advanced Urothelial Carcinoma of the Bladder\r\n* Status: **Active**, \r\n* Intervention: **Folfiri**,\r\n* Study Sites: \r\n   * Klinikum rechts der Isar der Technischen Universit\u00E4t M\u00FCnchen, \r\n   * Universit\u00E4tsklinikum W\u00FCrzburg,\r\n",);
        suggestions.setLabel("##### Title: " + researchStudy.getTitle() + "\r\n" +
                "* Condition: "+ researchStudy.getCondition() + ",\r\n" +
                "* Date range: \r\n" +
                "  **Start: " + researchStudy.getPeriod().getStart() + "**  \r\n" +
                "  **End: " + researchStudy.getPeriod().getEnd() + "**,\r\n" +
                "* Region:" + "\r\n" + "**" + researchStudy.getRegion().get(1).toString() + "** \r\n" +
                researchStudy.getRegion().get(2).getText() + "**,\r\n" +
                "* Brief Summary: \r\n" + researchStudy.getDescriptionSummary() + "\r\n" + "Patient selected: \r\n" +
                    "Name: " + searchForPatient(observationCodes, researchStudy.getCondition().toString()));

        suggestions.setUuid(new UUID(1, 0));
        //researchStudy.getRegion();//where is it being realised
        //researchStudy.getRecruitment().getEligibility();//criterias
        //researchStudy.getProtocol();//steps to follow
        //researchStudy.getDescriptionSummary();//brief description of the study
        //researchStudy.getCondition();//condition being study
        //researchStudy.getPeriod();//of the study availability


        ArrayList<Suggestion> suggestionsList = new ArrayList<Suggestion>();

        List<Action> actions = new ArrayList<Action>();
        Action action = new Action();
        action.setDescription("Based on condition and subtype, the patient could be enrolled to a clinical trial");
        action.setType(TypeEnum.CREATE);
        Resource resource = new Resource();
        resource.setResourceType("Patient");
        action.setResource(resource);
        actions.add(action);

        Action action2 = new Action();
        action2.setDescription("Based on condition and subtype, the patient could be enrolled to a clinical trial 2");
        action2.setType(TypeEnum.DELETE);
        Resource resource2 = new Resource();
        resource2.setResourceType("Task");
        action2.setResource(resource);
        actions.add(action2);

        //TODO: here another action for the criterias
        Action action3 = new Action();
        suggestions.setActions(actions);
        suggestionsList.add(suggestions);
        c.setSuggestions(suggestionsList);
        c.setSource(s);

        storedData.put("trials", c);
      


        //InternalEHRFHIRServerRetrieval iehr = new InternalEHRFHIRServerRetrieval();
        //Patient patient = iehr.getPatientFromFHIR();
        //patient.getBirthDate();

        // INSERT: Our basic logic that for example when a Patient is old then a specific research study is good (must not be perfect at this point)

        


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

    @CrossOrigin // cross-domain-communication
    @RequestMapping(value = "/cds-services/trials/1", 
                produces = {"application/json"}, 
                method = RequestMethod.GET) 
    public ResponseEntity<Card> getCDSTestService() {

        // Retrieve the CDSResponse object from in-memory storage using "trials" as the key
        Card data = this.storedData.get("trials");
        
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
    }


    /**
     * Search for a patient on the FHIR server using the given search parameters which are inclusion and exclusion criteria.
     * @param searchParameters
     * @return
     */
    public List<Patient> searchForPatient(String observationCodes, String conditionCode) {
       

        // Build the search criteria
        //String observationCodes = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl|C49164,http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl|C114879,http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl|C102869";
        //String conditionCode = "http://snomed.info/sct|443493003";

        // Perform the search
        Bundle response = client.search()
        .forResource(Patient.class)
        .where(new StringClientParam("_has").matches().value("Observation:subject:code=" + observationCodes))
        .and(new StringClientParam("_has").matches().value("Condition:subject:code=" + conditionCode))
        .returnBundle(Bundle.class)
        .execute();

        // Extract patients from the bundle
        List<Patient> resources = BundleUtil.toListOfResourcesOfType(client.getFhirContext(), response, Patient.class);

        return resources;
        
    }

    

        

}
