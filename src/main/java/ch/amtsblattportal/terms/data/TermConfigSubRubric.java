package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude( JsonInclude.Include.NON_DEFAULT )
@JsonPropertyOrder({ "key", "valueType", "term", "primaryElement", "mandatoryIn", "notEditableIn", "excludeDisplayTitle", "delimiterTitle" })
public class TermConfigSubRubric implements TermConfig {

  @JsonProperty @JsonInclude( JsonInclude.Include.NON_DEFAULT ) @Getter boolean primaryElement, mandatoryIn, notEditableIn;

  private final TermConfigBase termConfigBase;
  private final List<String> parents;

  // Constructor
  public TermConfigSubRubric( String subRubricCode, TermConfigBase termConfigBase, SubRubricTerm subRubricTerm ) {
    // Store fields in the form they are queried
    if( subRubricCode != null && termConfigBase != null && subRubricTerm != null ) {
      if( subRubricTerm.key != null ) {
        SubRubricTerm rootSubRubricTerm = Root.getSubRubricTerm( subRubricCode, subRubricTerm.key );
        if( rootSubRubricTerm != null ) {
          this.primaryElement = Boolean.TRUE.equals( rootSubRubricTerm.primary );
          this.mandatoryIn = subRubricTerm.required != null ? subRubricTerm.required : Boolean.TRUE.equals( rootSubRubricTerm.required );
          this.notEditableIn = !( subRubricTerm.editable != null ? subRubricTerm.editable : Boolean.TRUE.equals( rootSubRubricTerm.editable ));
        }
      }
    }
    this.termConfigBase = Objects.requireNonNullElseGet( termConfigBase, TermConfigBase::new );
    this.parents = subRubricTerm != null ? subRubricTerm.parents : new ArrayList<>();
  }

  // Get key from base root term
  @JsonProperty
  public String getKey() {
    return this.termConfigBase.rootTerm.key;
  }

  // Get type from base root term
  @JsonIgnore
  public String getType() {
    return this.termConfigBase.rootTerm.type;
  }

  // Get value type from base root term
  @JsonProperty
  public String getValueType() {
    return this.termConfigBase.rootTerm.valueType;
  }

  // Get term from base term
  @JsonProperty
  public MultiLang getTerm() {
    return this.termConfigBase.term;
  }

  // Return true if default title should not be displayed as specified in base root term
  @JsonProperty @JsonInclude( JsonInclude.Include.NON_DEFAULT )
  public boolean isExcludeDisplayTitle() {
    return Boolean.TRUE.equals( this.termConfigBase.rootTerm.excludeDisplayTitle );
  }

  // Get title delimiter from base root term or default delimiter if not specified
  @JsonProperty
  public String getDelimiterTitle() {
    return this.termConfigBase.rootTerm.delimiterTitle != null ? this.termConfigBase.rootTerm.delimiterTitle : TITLE_DELIMITER;
  }

  // Get list of all parent term keys
  @JsonIgnore
  public List<String> getParents() {
    return this.parents != null && !this.parents.isEmpty() ? this.parents : this.termConfigBase.rootTerm.parents;
  }

  // Get term configuration from base term
  @JsonIgnore
  public TermConfig getTermConfig() {
    return this.termConfigBase;
  }

  // Get order from root term
  @JsonIgnore
  public int getOrder() {
    return this.termConfigBase.rootTerm.getOrder();
  }

  // Implement comparable
  @Override
  public int compareTo( TermConfig other ) {
    return Integer.compare( this.getOrder(), other.getOrder() );
  }

}