package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "publicDuration", "authorization", "legalNotice" })
public class SubRubricInfo {

  @JsonProperty @Getter @Setter PublicDuration publicDuration;
  @JsonProperty @Getter @Setter List<String> authorization = new ArrayList<>();
  @JsonProperty @Getter @Setter MultiLang legalNotice;

}
