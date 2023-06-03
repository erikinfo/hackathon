package io.swagger.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CDSResponse
 */
//@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-01-27T10:43:47.246Z")

//@JsonRootName(value = "") // Empty string to remove the root name
public class CDSResponse   {
  //@Valid
  private List<Card> cards = new ArrayList<Card>();

  public CDSResponse cards(List<Card> cards) {
    this.cards = cards;
    return this;
  }

  public CDSResponse addCardsItem(Card cardsItem) {
    this.cards.add(cardsItem);
    return this;
  }

  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> cards) {
    this.cards = cards;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CDSResponse cdSResponse = (CDSResponse) o;
    return Objects.equals(this.cards, cdSResponse.cards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CDSResponse {\n");
    
    sb.append("    cards: ").append(toIndentedString(cards)).append("\n");
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

