package ch.amtsblattportal.terms.data;

import ch.amtsblattportal.terms.db.Oracle;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Root {

  private static final Logger LOG = LoggerFactory.getLogger( Root.class );
  private static ConfigRoot ROOT = new ConfigRoot();
  private static Map<String,Term> TERMS = new HashMap<>();
  private static Map<String,List<Municipality>> MUNICIPALITIES = new HashMap<>();
  private static Map<String,Map<String,SubRubricTerm>> SUBRUBRIC_TERMS = new HashMap<>();

  // Load root configuration from JSON string
  public static void load( String json )  {
    LOG.info( "Loading root configuration" );
    loadJson( json );
  }

  // Update root configuration from JSON string
  public static void update( String json )  {
    LOG.info( "Updating root configuration" );
    loadJson( json );
  }

  // Save root configuration
  public static void save() throws JsonProcessingException, SQLException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure( SerializationFeature.INDENT_OUTPUT, false );
    mapper.setSerializationInclusion( JsonInclude.Include.NON_EMPTY );
    Oracle.setJson( mapper.writeValueAsString( ROOT ));
  }

  // Get root configuration
  public static ConfigRoot getConfig() {
    return ROOT;
  }

  // Get subrubric specific term by key
  public static SubRubricTerm getSubRubricTerm( String subRubricCode, String key ) {
    Map<String, SubRubricTerm> subRubricTerm = SUBRUBRIC_TERMS.get( subRubricCode );
    if( subRubricTerm != null )
      return subRubricTerm.get( key );
    return null;
  }

  // Get term by key
  public static Term getTerm( String key ) {
    return TERMS.get( key );
  }

  // Get list of municipalities
  public static List<Municipality> getMunicipalities( String canton ) {
    return MUNICIPALITIES.get( canton );
  }

  // Load configuration from JSON string
  private static void loadJson( String json )  {
    if( json == null )
      return;
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    ConfigRoot config;
    try {
      config = mapper.readValue( json, ConfigRoot.class );
    } catch( JsonProcessingException ex ) {
      LOG.error( "Could not load root configuration, using empty configuration", ex );
      config = new ConfigRoot();
    }
    // Create map of all assigned terms per subrubric (subrubric specific configuration)
    Map<String,Map<String,SubRubricTerm>> subRubricTerms = new HashMap<>();
    for( SubRubric subRubric : config.subrubrics ) {
      subRubric.enforceExampleTenantCode();
      String subRubricCode = subRubric.code;
      List<SubRubricTerm> terms = subRubric.terms;
      if( terms != null ) {
        for( SubRubricTerm term : terms )
          subRubricTerms.computeIfAbsent( subRubricCode, value -> new HashMap<>() ).put( term.key, term );
      }
    }
    // Create map of all terms (generic configuration)
    Map<String,Term> terms = new HashMap<>();
    int i = 0;
    for( Term term : config.terms ) {
      // We only want true or null to make sure it only gets returned in JSON if it is true
      if( !Boolean.TRUE.equals( term.excludeDisplayTitle ))
        term.excludeDisplayTitle = null;
      term.setOrder( i++ );
      terms.put( term.key, term );
    }
    // Create map of all municipalities (special term type)
    Map<String,List<Municipality>> municipalities = new HashMap<>();
    for( Municipality municipality : config.municipalities ) {
      for( String canton : municipality.cantons )
        municipalities.computeIfAbsent( canton, value -> new ArrayList<>() ).add( municipality );
    }
    ROOT = config;
    TERMS = terms;
    MUNICIPALITIES = municipalities;
    SUBRUBRIC_TERMS = subRubricTerms;
  }

}
