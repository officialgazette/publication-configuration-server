package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;

import java.util.List;

@JsonPropertyOrder({ "key", "valueType", "term", "excludeDisplayTitle", "delimiterTitle" })
public class TermConfigBase implements TermConfig {

  @JsonIgnore @Getter boolean primaryElement, mandatoryIn, notEditableIn;
  final Term rootTerm;

  final MultiLang term;

  // Constructor for empty object
  public TermConfigBase() {
    rootTerm = new Term();
    this.term = null;
  }

  // Constructor
  public TermConfigBase( Term term, String[] languages ) {
    rootTerm = term;
    this.term = new MultiLang( this.rootTerm.term, languages );
  }

  // Get key from root term
  @JsonProperty
  public String getKey() {
    return this.rootTerm.key;
  }

  // Get type from root term
  @JsonIgnore
  public String getType() {
    return this.rootTerm.type;
  }

  // Get value type from root term
  @JsonProperty
  public String getValueType() {
    return this.rootTerm.valueType;
  }

  // Get term
  @JsonProperty
  public MultiLang getTerm() {
    return this.term;
  }

  // Return true if default title should not be displayed as specified in root term
  @JsonProperty @JsonInclude( JsonInclude.Include.NON_DEFAULT )
  public boolean isExcludeDisplayTitle() {
    return Boolean.TRUE.equals( this.rootTerm.excludeDisplayTitle );
  }

  // Get title delimiter from root term or default delimiter if not specified
  @JsonProperty
  public String getDelimiterTitle() {
    return this.rootTerm.delimiterTitle != null ? this.rootTerm.delimiterTitle : TITLE_DELIMITER;
  }

  // Get list of all parent term keys
  @JsonIgnore
  public List<String> getParents() {
    return this.rootTerm.parents;
  }

  // Get term config which is always null for base configurations
  @JsonIgnore
  public TermConfig getTermConfig() {
    return null;
  }

  // Get order from root term
  @JsonIgnore
  public int getOrder() {
    return this.rootTerm.getOrder();
  }

  // Implement comparable
  @Override
  public int compareTo( TermConfig other ) {
    return Integer.compare( this.getOrder(), other.getOrder() );
  }

}
