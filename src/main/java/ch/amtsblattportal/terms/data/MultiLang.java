package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "de", "fr", "it", "en" })
public class MultiLang implements Comparable<MultiLang> {

  @JsonProperty @Getter @Setter String de, fr, it, en;

  @JsonCreator
  public MultiLang() {}

  public MultiLang( MultiLang multiLang, String[] languages ) {
    for( String language : languages ) {
      this.set( language, multiLang.get( language ));
    }
  }

  // Return true if value in selected language starts with specified prefix
  public boolean startsWith( String language, String prefix ) {
    if( language == null || prefix == null || prefix.isEmpty() )
      return true;
    String text;
    switch( language ) {
      case "de" -> text = this.de;
      case "fr" -> text = this.fr;
      case "it" -> text = this.it;
      case "en" -> text = this.en;
      default -> {
        return false;
      }
    }
    if( text == null || text.isEmpty() )
      return false;
    return text.toLowerCase().startsWith( prefix.toLowerCase() );
  }

  // Get value for specified language
  public String get( String language ) {
    switch( language ) {
      case "de" -> { return getDeSubstitute(); }
      case "fr" -> { return getFrSubstitute(); }
      case "it" -> { return getItSubstitute(); }
      case "en" -> { return getEnSubstitute(); }
    }
    return null;
  }

  // Set value for specified language
  public void set( String language, String value ) {
    switch( language ) {
      case "de" -> this.de = value;
      case "fr" -> this.fr = value;
      case "it" -> this.it = value;
      case "en" -> this.en = value;
    }
  }

  // Implement comparable
  @Override
  public int compareTo( MultiLang other ) {
    String thisValue = getDeSubstitute();
    String otherValue = other.getDeSubstitute();
    if( thisValue == null && otherValue == null )
      return 0;
    if( thisValue == null )
      return 1;
    if( otherValue == null )
      return -1;
    return thisValue.compareTo( otherValue );
  }

  // Get german value or alternative if null or empty
  private String getDeSubstitute() {
    return firstOf( this.de, this.en, this.fr, this.it );
  }

  // Get french value or alternative if null or empty
  private String getFrSubstitute() {
    return firstOf( this.fr, this.en, this.de, this.it );
  }

  // Get italian value or alternative if null or empty
  private String getItSubstitute() {
    return firstOf( this.it, this.en, this.de, this.fr );
  }

  // Get english value or alternative if null or empty
  private String getEnSubstitute() {
    return firstOf( this.en, this.de, this.fr, this.it );
  }

  // Return first non-(null/empty) string specified
  private String firstOf( String lang1, String lang2, String lang3, String lang4 ) {
    if( lang1 != null && !lang1.isEmpty() ) return lang1;
    if( lang2 != null && !lang2.isEmpty() ) return lang2;
    if( lang3 != null && !lang3.isEmpty() ) return lang3;
    if( lang4 != null && !lang4.isEmpty() ) return lang4;
    return null;
  }

}
