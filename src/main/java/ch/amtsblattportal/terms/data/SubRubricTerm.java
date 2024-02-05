package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({ "key", "primary", "required", "editable" })
public class SubRubricTerm {

  @JsonProperty @Getter @Setter String key;
  @JsonProperty @Getter @Setter Boolean primary, required, editable;
  @JsonProperty @Getter @Setter List<String> parents;

  // Constructor used by JSON object mapper
  @JsonCreator
  public SubRubricTerm() {}

  // Constructor
  public SubRubricTerm( String key ) {
    this.key = key;
  }

}
