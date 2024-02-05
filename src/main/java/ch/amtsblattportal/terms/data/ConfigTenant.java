package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@JsonPropertyOrder({ "tenant", "languages", "subrubrics" })
public class ConfigTenant {

  // Load tenant configuration from JSON string
  public static ConfigTenant load( String id, String json ) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    ConfigTenant config = mapper.readValue( json, ConfigTenant.class );
    config.setCanton( id );
    return config;
  }

  // Generate runtime configuration containing all root terms (used for example configuration)
  public static ConfigTenant generate( String id ) {
    List<SubRubricMapping> subRubricMappings = new ArrayList<>();
    // Map terms for each subrubric
    for( SubRubric subRubric : Root.getConfig().subrubrics ) {
      SubRubricMapping subRubricMapping = new SubRubricMapping( subRubric.code );
      for( SubRubricTerm subrubricTerm : subRubric.terms )
        subRubricMapping.terms.add( new SubRubricTerm( subrubricTerm.key ) );
      subRubricMappings.add( subRubricMapping );
    }
    ConfigTenant config = new ConfigTenant();
    config.setTenant( id );
    config.setCanton( id );
    config.setSubRubricMappings( subRubricMappings );
    return config;
  }

  @JsonProperty( "tenant" ) @Getter @Setter String tenant;
  @JsonProperty( "languages" ) @Getter @Setter List<String> languages = new ArrayList<>();
  @JsonProperty( "subrubrics" ) @Getter @Setter List<SubRubricMapping> subRubricMappings = new ArrayList<>();

  // Let's use XX as canton in case it is not specified
  String canton = "XX";

  // Set canton and make sure it is two characters uppercase
  private void setCanton( String id ) {
    if( id.length() > 1 )
      this.canton = id.substring( id.length() - 2 ).toUpperCase();
  }

}
