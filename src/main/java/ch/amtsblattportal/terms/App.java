package ch.amtsblattportal.terms;

import ch.amtsblattportal.terms.api.v1.Api;
import ch.amtsblattportal.terms.data.Root;
import ch.amtsblattportal.terms.data.Tenant;
import ch.amtsblattportal.terms.data.ui.Resource;
import ch.amtsblattportal.terms.db.Oracle;
import io.micronaut.runtime.Micronaut;

public class App {

  public static void main( String[] args ) throws Exception {
    // Connect to Oracle
    Oracle.connect();
    // Load the root configuration from the database
    Root.load( Oracle.getJson( "ROOT" ));
    // Load the tenant hosting the example configuration
    Tenant.load( Tenant.EXAMPLE_TENANT );
    // Load all other tenants
    for( String tenant : Oracle.getTenants() )
      Tenant.load( tenant, Oracle.getJson( tenant ));
    // Load static resources for the UI
    Resource.load();
    // Start the application
    Micronaut.build( "" ).classes( Api.class ).environmentPropertySource( true ).banner( false ).start();
  }

}
