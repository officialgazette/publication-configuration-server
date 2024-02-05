package ch.amtsblattportal.terms.data;

import java.util.List;

public class Filter {

  final boolean exclude;
  final String value;

  // Return true if given string matches filter
  public static boolean matches( Filter filter, String value ) {
    return filter.value == null || ( !filter.exclude && filter.value.equals( value )) || ( filter.exclude && !filter.value.equals( value ));
  }

  // Return true if given list of strings matches filter
  public static boolean matches( Filter filter, List<String> values ) {
    return filter.value == null || ( !filter.exclude && values.contains( filter.value )) || ( filter.exclude && !values.contains( filter.value ));
  }

  // Construct filter
  public Filter( String value ) {
    this.exclude = value != null && value.length() > 1 && value.startsWith( "!" );
    this.value = this.exclude ? value.substring( 1 ) : value;
  }

}
