package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "key", "term", "cantons" })
public class Municipality {

  @JsonProperty @Getter @Setter String key;
  @JsonProperty @Getter @Setter MultiLang term;
  @JsonProperty @Getter @Setter List<String> cantons = new ArrayList<>();

}
