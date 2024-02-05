package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "authorizationRegistrationOfficeTypes", "rubrics", "subrubrics", "terms" })
public class ConfigRoot {

  @JsonProperty @Getter @Setter List<RegistrationOfficeType> authorizationRegistrationOfficeTypes = new ArrayList<>();
  @JsonProperty @Getter @Setter List<Rubric> rubrics = new ArrayList<>();
  @JsonProperty @Getter @Setter List<SubRubric> subrubrics = new ArrayList<>();
  @JsonProperty @Getter @Setter List<Term> terms = new ArrayList<>();
  @JsonProperty @Getter @Setter List<Municipality> municipalities = new ArrayList<>();

}
