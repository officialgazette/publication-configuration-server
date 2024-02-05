package ch.amtsblattportal.terms.api.v1;

import ch.amtsblattportal.terms.data.Root;
import ch.amtsblattportal.terms.data.Tenant;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Controller( value = "/api/v1", produces = "application/json;charset=utf-8" )

public class Api {

  private static final String OK = "{\"status\":\"OK\"}";
  private static final Logger LOG = LoggerFactory.getLogger( Api.class );
  private static final List<String> RESERVED_TENANTS = Arrays.asList( "ROOT", Tenant.EXAMPLE_TENANT );

  @Get( uri = "/public/" )
  public HttpResponse<?> root() {
    return HttpResponse.ok( Root.getConfig() );
  }

  // Query tenant specific terms
  @Get( uri = "/public/{tenant}" )
  public HttpResponse<?> tenant(
    @PathVariable String tenant,
    @Nullable @QueryValue String parent,
    @Nullable @QueryValue String type,
    @Nullable @QueryValue String key,
    @Nullable @QueryValue String language,
    @Nullable @QueryValue String typedText
  ) {
    Tenant tenantObject = Tenant.getTenant( tenant );
    if( tenantObject == null )
      return HttpResponse.notFound();
    return HttpResponse.ok( tenantObject.getTerms( key, type, parent, language, typedText ));
  }

  // Get tenant specific configuration
  @Get( uri = "/public/{tenant}/config" )
  public HttpResponse<?> tenantConfig(
    @PathVariable String tenant
  ) {
    Tenant tenantObject = Tenant.getTenant( tenant );
    if( tenantObject == null )
      return HttpResponse.notFound();
    return HttpResponse.ok( tenantObject.getConfig() );
  }

  // Get tenant and subrubric specific terms
  @Get( uri = "/public/{tenant}/{subrubric}" )
  public HttpResponse<?> term(
    @PathVariable String tenant,
    @PathVariable String subrubric,
    @Nullable @QueryValue String parent,
    @Nullable @QueryValue String type,
    @Nullable @QueryValue String key,
    @Nullable @QueryValue String language,
    @Nullable @QueryValue String typedText
  ) {
    Tenant tenantObject = Tenant.getTenant( tenant );
    if( tenantObject == null )
      return HttpResponse.notFound();
    return HttpResponse.ok( tenantObject.getTerms( null, subrubric, key, type, parent, language, typedText));
  }

  // Get tenant and subrubric specific terms for specified list
  @Get( uri = "/public/{tenant}/{subrubric}/{list}" )
  public HttpResponse<?> termOfList(
    @PathVariable String tenant,
    @PathVariable String subrubric,
    @PathVariable String list,
    @Nullable @QueryValue String parent,
    @Nullable @QueryValue String type,
    @Nullable @QueryValue String key,
    @Nullable @QueryValue String language,
    @Nullable @QueryValue String typedText
  ) {
    Tenant tenantObject = Tenant.getTenant( tenant );
    if( tenantObject == null )
      return HttpResponse.notFound();
    return HttpResponse.ok( tenantObject.getTerms( list, subrubric, key, type, parent, language, typedText ));
  }

  // Simple ping
  @Get( uri = "/admin/ping" )
  public String ping() {
    return OK;
  }

  // Get root config
  @Get( uri = "/admin/config" )
  public HttpResponse<?> adminGetConfig() {
    return HttpResponse.ok( Root.getConfig() );
  }

  // Upload root config
  @Post( uri = "/admin/config" )
  public HttpResponse<?> adminSetConfig(
    @Body String json
  ) {
    try {
      Root.update( json );
      Tenant.reload();
      Root.save();
      LOG.info( "Root configuration updated" );
      return HttpResponse.ok( OK );
    } catch( JsonProcessingException | SQLException e ) {
      LOG.error( "Failed to update root configuration!" );
      return HttpResponse.serverError( e.toString() );
    }
  }

  // Get tenant specific config
  @Get( uri = "/admin/config/{tenant}" )
  public HttpResponse<?> adminGetConfigTenant(
    @PathVariable String tenant
  ) {
    Tenant tenantObject = Tenant.getTenant( tenant );
    if( tenantObject == null )
      return HttpResponse.notFound();
    return HttpResponse.ok( tenantObject.getConfig() );
  }

  // Upload tenant specific config
  @Post( uri = "/admin/config/{tenant}" )
  public HttpResponse<?> adminSetConfigTenant(
    @PathVariable String tenant,
    @Body String json
  ) {
    if( RESERVED_TENANTS.contains( tenant ))
      return HttpResponse.notModified();
    try {
      Tenant.update( tenant, json );
      Tenant.save( tenant );
      LOG.info( "Tenant configuration for " + tenant + " updated" );
      return HttpResponse.ok( OK );
    } catch( JsonProcessingException | SQLException e ) {
      LOG.error( "Failed to update tenant configuration for " + tenant +"!" );
      return HttpResponse.serverError( e.toString() );
    }
  }

  // Get list of tenants
  @Get( uri = "/admin/tenants" )
  public HttpResponse<?> adminTenants() {
    return HttpResponse.ok( Tenant.getTenants() );
  }

}
