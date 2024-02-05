package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "key", "type", "valueType", "term", "excludeDisplayTitle", "delimiterTitle", "parents" })
public class Term {

  @JsonProperty @Getter @Setter String key, type, valueType, delimiterTitle;
  @JsonProperty @Getter @Setter MultiLang term;
  @JsonProperty @Getter @Setter Boolean excludeDisplayTitle;
  @JsonProperty @Getter @Setter List<String> parents = new ArrayList<>();
  private int order = 0;

  // Get order but make sure it is not returned as JSON
  @JsonIgnore
  public int getOrder() {
    return this.order;
  }

  // Set order but make sure it is not used by JSON object mapper
  @JsonIgnore
  public void setOrder( int order ) {
    this.order = order;
  }

}
