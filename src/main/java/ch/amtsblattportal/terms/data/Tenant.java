package ch.amtsblattportal.terms.data;

import ch.amtsblattportal.terms.db.Oracle;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

public class Tenant {

  public static final String EXAMPLE_CANTON = "GR";
  public static final String EXAMPLE_TENANT = "kabgr";

  private static final Map<String,Tenant> TENANTS = new HashMap<>();
  private static final Logger LOG = LoggerFactory.getLogger( Tenant.class );

  // Load empty tenant which will force generation of example configuration
  public static void load( String id ) throws JsonProcessingException {
    load( id, null );
  }

  // Load tenant configuration from JSON string or generate example configuration if null
  public static void load( String id, String json ) throws JsonProcessingException {
    if( json == null ) {
      LOG.info( "Generating tenant configuration for " + id );
      generate( id );
    } else {
      LOG.info( "Loading tenant configuration for " + id );
      loadJson( id, json );
    }
  }

  // Update tenant configuration
  public static void update( String id, String json ) throws JsonProcessingException {
    LOG.info( "Updating tenant configuration for " + id );
    loadJson( id, json );
  }

  // Reload all tenant configurations
  public static void reload() {
    for( var entry : TENANTS.entrySet() ) {
      String id = entry.getKey();
      Tenant tenant = entry.getValue();
      if( tenant.generated ) {
        LOG.info( "Regenerating tenant configuration for " + id );
        generate( id );
      } else {
        LOG.info( "Reloading tenant configuration for " + id );
        TENANTS.put( id, new Tenant( tenant.config ) );
      }
    }
  }

  // Save tenant configuration to database
  public static void save( String id ) throws SQLException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure( SerializationFeature.INDENT_OUTPUT, false );
    mapper.setSerializationInclusion( JsonInclude.Include.NON_EMPTY );
    Oracle.setJson( id, mapper.writeValueAsString( TENANTS.get( id ).config ));
  }

  // Get tenant configuration for specified id
  public static Tenant getTenant( String id ) {
    return TENANTS.get( id );
  }

  // Get list of all tenants
  public static List<String> getTenants() {
    return TENANTS.keySet().stream().toList();
  }

  // Load tenant configuration from JSON string
  private static void loadJson( String id, String json ) throws JsonProcessingException {
    TENANTS.put( id, new Tenant( ConfigTenant.load( id, json )));
  }

  // Generate tenant configuration
  private static void generate( String id ) {
    TENANTS.put( id, new Tenant( ConfigTenant.generate( id ), true ));
  }

  @Getter final ConfigTenant config;
  @Getter final List<TermConfig> terms = new ArrayList<>();
  private final Map<String,List<TermConfig>> termsBySubRubric = new HashMap<>();
  private final Map<String,List<TermConfig>> termsByPrimaryElement = new HashMap<>();
  private final Map<String,List<TermConfig>> termsByMandatoryIn = new HashMap<>();
  private final Map<String,List<TermConfig>> termsByNotEditableIn = new HashMap<>();
  private final Map<String,List<TermConfig>> termsByParent = new HashMap<>();
  private final List<TermConfig> municipalities = new ArrayList<>();
  private final boolean generated;

  // Simple constructor
  public Tenant( ConfigTenant config ) {
    this( config, false );
  }

  // Main constructor
  public Tenant( ConfigTenant config, boolean generated ) {
    this.config = config;
    if( this.config.languages == null )
      this.config.languages = new ArrayList<>();
    // If configuration (e.g. old version) does not specify languages, enable all
    if( this.config.languages.isEmpty() )
      this.config.languages.addAll( Arrays.asList( "de", "fr", "it", "en" ));
    this.generated = generated;
    generateTerms();
  }

  // Get list of terms matching the query fields (subrubric independent)
  public List<TermConfig> getTerms( String key, String type, String parent, String language, String typedText ) {
    // If requested terms are municipalities, get them from the municipality list
    if( isMunicipalityRequest( type ))
      return filter( municipalities, key, type, parent, language, typedText );
    // If no parent is specified in the request or the parent is negated use the main terms list
    if( parent == null || parent.startsWith( "!" ))
      return filter( terms, key, type, parent, language, typedText );
    // Get terms from the by parent list
    return filter( termsByParent.get( parent ), key, type, null, language, typedText );
  }

  // Get list of terms matching the query fields (subrubric dependent)
  public List<TermConfig> getTerms( String list, String subRubric, String key, String type, String parent, String language, String typedText ) {
    List<TermConfig> terms;
    if( isMunicipalityRequest( type ))
      terms = municipalities;
    else {
      if( list == null )
        // If no specific list is defined, use the generic terms list by subrubric
        terms = termsBySubRubric.get( subRubric );
      else
        // Otherwise use the specified list to search for the requested terms
        switch( list ) {
          case "primaryElement" -> terms = termsByPrimaryElement.get( subRubric );
          case "mandatoryIn" -> terms = termsByMandatoryIn.get( subRubric );
          case "notEditableIn" -> terms = termsByNotEditableIn.get( subRubric );
          default -> terms = null;
        }
    }
    return filter( terms, key, type, parent, language, typedText );
  }

  // Filter the given list of terms using the specified fields
  private List<TermConfig> filter( List<TermConfig> terms, String key, String type, String parent, String language, String typedText ) {
    List<TermConfig> result = new ArrayList<>();
    if( terms == null || terms.isEmpty() )
      return result;
    if( key == null && type == null && parent == null && ( language == null || typedText == null ))
      return terms;
    Filter keyFilter = new Filter( key );
    Filter typeFilter = new Filter( type );
    Filter parentFilter = new Filter( parent );
    for( TermConfig term : terms ) {
      if( Filter.matches( keyFilter, term.getKey() )
        && Filter.matches( typeFilter, term.getType() )
        && Filter.matches( parentFilter, term.getParents() )
        && term.getTerm().startsWith( language, typedText )
      ) result.add( term );
    }
    return result;
  }

  // Generate terms based on tenant configuration and cache
  private void generateTerms() {
    String[] languages = this.config.languages.toArray( new String[0] );
    Map<String,TermConfigBase> termConfigBases = new HashMap<>();
    if( config.subRubricMappings != null ) {
      for( SubRubricMapping subRubricMapping : config.subRubricMappings ) {
        // Get the actual subrubric code from mapping and get terms
        String subRubricCode = subRubricMapping.getMap( config.canton );
        List<SubRubricTerm> subRubricTerms = subRubricMapping.getTerms();
        // We need a subrubric code and terms to proceed, otherwise ignore
        if( subRubricCode != null && subRubricTerms != null ) {
          // Loop trough all terms
          for( SubRubricTerm subRubricTerm : subRubricTerms ) {
            Term term = Root.getTerm( subRubricTerm.key );
            if( term != null ) {
              // Get term base configuration
              TermConfigBase termConfigBase = termConfigBases.get( subRubricTerm.key );
              if( termConfigBase == null ) {
                termConfigBase = new TermConfigBase( term, languages );
                termConfigBases.put( subRubricTerm.key, termConfigBase );
              }
              TermConfig termConfig = new TermConfigSubRubric( subRubricMapping.use, termConfigBase, subRubricTerm );
              List<TermConfig> list = this.termsBySubRubric.computeIfAbsent( subRubricCode, value -> new ArrayList<>() );
              // Make sure to only add enumValues once
              if( !termConfig.getType().equals( "enumValue" ) || list.stream().noneMatch( item -> item.getKey().equals( termConfig.getKey() )))
                list.add( termConfig );
              // If term is a primary element, add it to the corresponding list
              if( termConfig.isPrimaryElement() )
                this.termsByPrimaryElement.computeIfAbsent( subRubricCode, value -> new ArrayList<>() ).add( termConfig );
              // If term has "mandatory in" specified, add it to the corresponding list
              if( termConfig.isMandatoryIn() )
                this.termsByMandatoryIn.computeIfAbsent( subRubricCode, value -> new ArrayList<>() ).add( termConfig );
              // If term has "not editable in" specified, add it to the corresponding list
              if( termConfig.isNotEditableIn() )
                this.termsByNotEditableIn.computeIfAbsent( subRubricCode, value -> new ArrayList<>() ).add( termConfig );
              List<String> parents = termConfig.getParents();
              // If term has parents specified, add it to each parents list
              if( parents != null ) {
                for( String parent : parents )
                  this.termsByParent.computeIfAbsent( parent, value -> new ArrayList<>() ).add( termConfig );
              }
            }
          }
        }
      }
    }
    // Get all municipalities for the current tenant based on the specified canton
    List<Municipality> cantonMunicipalities = Root.getMunicipalities( config.canton );
    if( cantonMunicipalities != null ) {
      for( Municipality municipality : cantonMunicipalities )
        municipalities.add( new TermConfigMunicipality( municipality, languages ));
    }
    // Sort all the lists
    Collections.sort( municipalities );
    terms.addAll( termConfigBases.values() );
    Collections.sort( terms );
    sortTermsBy( termsBySubRubric );
    sortTermsBy( termsByPrimaryElement );
    sortTermsBy( termsByMandatoryIn );
    sortTermsBy( termsByNotEditableIn );
    sortTermsBy( termsByParent );
  }

  // Sort terms
  private void sortTermsBy( Map<String,List<TermConfig>> list ) {
    for( var entry : list.entrySet() )
      Collections.sort( entry.getValue() );
  }

  // Returns true if the request is for a municipality
  private boolean isMunicipalityRequest( String type ) {
    return type != null && type.equals( "municipalityId" );
  }

}
