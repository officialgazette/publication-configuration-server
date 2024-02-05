package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "use", "map", "name", "info", "terms" })
public class SubRubricMapping {

  @JsonProperty @Getter @Setter String use, map;
  @JsonProperty @Getter @Setter MultiLang name;
  @JsonProperty @Getter @Setter SubRubricInfo info;
  @JsonProperty @Getter @Setter List<SubRubricTerm> terms = new ArrayList<>();

  // Constructor used by JSON object mapper
  @JsonCreator
  public SubRubricMapping() {}

  // Constructor
  public SubRubricMapping( String use ) {
    this.use = use;
  }

  // Get subrubric mapping code and make sure it matches the specified canton
  @JsonIgnore public String getMap( String canton ) {
    if( map != null )
      return map;
    if( canton == null )
      return use;
    return use.replaceAll( "-.{2}", "-" + canton );
  }

}
