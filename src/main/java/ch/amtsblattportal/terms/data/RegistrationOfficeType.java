package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "code", "name" })
public class RegistrationOfficeType {

  @JsonProperty @Getter @Setter String code;
  @JsonProperty @Getter @Setter MultiLang name;

}
