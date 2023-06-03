package io.swagger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.model.Action;
import io.swagger.model.Action.TypeEnum;
import io.swagger.model.CDSResponse;
import io.swagger.model.Card;
import io.swagger.model.Card.IndicatorEnum;
import io.swagger.model.Card.SelectionBehaviorEnum;
import io.swagger.model.Link;
import io.swagger.model.Resource;
import io.swagger.model.Source;
import io.swagger.model.Suggestion;

public class CDSResponseJsonTest {

    public static ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(CDSResponseJsonTest.class);

    @BeforeAll
    public static void setup () {
        mapper = new ObjectMapper();
    }


    @Test
    public void testJsonProperty() throws IOException {
        // Create a sample JSON string
        String json = "{\"cards\":[{\"summary\":\"Example Card\",\"detail\":\"This is an example card\"}]}";
        // Deserialize the JSON string to CDSResponse object
        CDSResponse cdsResponse = mapper.readValue(json, CDSResponse.class);
        
        // Test the getter method annotated with @JsonProperty("cards")
        assertEquals(1, cdsResponse.getCards().size());
        assertEquals("Example Card", cdsResponse.getCards().get(0).getSummary());
        

        // Serialize the CDSResponse object back to JSON
        String serializedJson = mapper.writeValueAsString(cdsResponse);

    }

    @Test
    public void testJsonAgainstFrontend() throws IOException {
        // Create the CDSResponse object
        Card c = new Card();
        c.setSummary("Example Card");
        c.setDetail("This is an example card");
        c.setIndicator(IndicatorEnum.INFO);

        Source s = new Source();
        s.setIcon("https://molit.eu/wp-content/uploads/2017/01/favicon.png");
        s.setUrl("https://molit.eu/");
        s.setLabel("Molit Institute");

        ArrayList<Link> links = new ArrayList<Link>();
        Link link = new Link();
        link.setLabel("Adjuvant Aspirin Treatment in PIK3CA Mutated Colon Cancer Patients. A Randomized, Double-blinded, Placebo-controlled, Phase III Trial");
        link.setUrl("https://clinicaltrials.gov/ct2/show/NCT02467582");
        link.setType("Clinical Trial");
        
        links.add(0, link);
        c.setLinks(links);
        c.setSelectionBehavior(SelectionBehaviorEnum.AT_MOST_ONE);

        Suggestion suggestions = new Suggestion();
        suggestions.setLabel("##### Title: Radio-Immunotherapy");
        suggestions.setUuid(new UUID(1, 0));

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

        suggestions.setActions(actions);
        suggestionsList.add(suggestions);
        c.setSuggestions(suggestionsList);
        c.setSource(s);

      
        // Convert the CDSResponse object to JSON string
        String json = mapper.writeValueAsString(c);

        // Print the JSON string
        System.out.println(json);

        logger.info(json);

        assertEquals("Example Card", c.getSummary());
        assertEquals("This is an example card", c.getDetail());
        assertEquals(IndicatorEnum.INFO, c.getIndicator());

        assertEquals("https://molit.eu/", c.getSource().getUrl());


    }
}
