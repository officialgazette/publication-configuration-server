package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "default", "min", "max" })
public class PublicDuration {

  @JsonProperty( "default" ) @Getter @Setter int _default;
  @JsonProperty @Getter @Setter int min, max;

}
