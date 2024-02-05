package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "key", "valueType", "term", "excludeDisplayTitle", "delimiterTitle" })
public class TermConfigMunicipality implements TermConfig {

  private static final String TYPE = "municipalityId";
  private static final String VALUE_TYPE = "int";

  final MultiLang term;

  @JsonIgnore @Getter boolean primaryElement, mandatoryIn, notEditableIn;
  final Municipality municipality;

  // Constructor
  public TermConfigMunicipality( Municipality municipality, String[] languages ) {
    this.municipality = municipality;
    this.term = new MultiLang( municipality.term, languages );
  }

  // Get key of municipality
  @JsonProperty
  public String getKey() {
    return this.municipality.key;
  }

  // Get type which is static for municipalities
  @JsonIgnore
  public String getType() {
    return TYPE;
  }

  // Get value type which is static for municipalities
  @JsonProperty
  public String getValueType() {
    return VALUE_TYPE;
  }

  // Get term
  @JsonProperty
  public MultiLang getTerm() {
    return this.term;
  }

  // Always return false for municipalities
  @JsonIgnore
  public boolean isExcludeDisplayTitle() {
    return false;
  }

  // Get title delimiter which is always the default delimiter as it is not configurable for municipalities
  @JsonIgnore
  public String getDelimiterTitle() {
    return TITLE_DELIMITER;
  }

  // Get list of all parent term keys which is always empty for municipalities
  @JsonIgnore
  public List<String> getParents() {
    return new ArrayList<>();
  }

  // Get term config which is always null for municipalities
  @JsonIgnore
  public TermConfig getTermConfig() {
    return null;
  }

  // Get order which is always 0 for municipalities to enforce alphabetical ordering
  @JsonIgnore
  public int getOrder() {
    return 0;
  }

  // Implement comparable
  @Override
  public int compareTo( TermConfig other ) {
    return this.municipality.term.compareTo( other.getTerm() );
  }

}
