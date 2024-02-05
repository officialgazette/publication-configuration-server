package ch.amtsblattportal.terms.api.v1;

import ch.amtsblattportal.terms.data.ConfigRoot;
import ch.amtsblattportal.terms.data.Root;
import ch.amtsblattportal.terms.data.ui.Resource;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

@Controller( value = "/api/v1", produces = "application/json;charset=utf-8" )

public class Ui {

  // Get index for tenant and root config UI
  @Get( uris = { "/public/ui/", "/public/ui/root/" }, produces = "text/html;charset=utf-8" )
  public HttpResponse<?> getIndex() {
    return getFile( "index", "html" );
  }

  // Get root config for tenant config UI
  @Get( uri = "/public/ui/config" )
  public ConfigRoot getConfig() {
    return Root.getConfig();
  }

  // Get localization data
  @Get( uri = "/public/ui/assets/i18n/{file}.json", produces = "application/json;charset=utf-8" )
  public HttpResponse<?> getFileAssetsI18nJSON(
          @PathVariable String file
  ) {
    return getFile( "assets/i18n/" + file, "json" );
  }

  // Dynamic content type specification seems buggy in micronaut currently, so we create content type specific getters for now

  // Get CSS files
  @Get( uri = "/public/ui/{file}.css", produces = "text/css;charset=utf-8" )
  public HttpResponse<?> getFileCSS(
    @PathVariable String file
  ) {
    return getFile( file, "css" );
  }

  // Get ICO files
  @Get( uri = "/public/ui/{file}.ico", produces = "image/x-icon" )
  public HttpResponse<?> getFileICO(
    @PathVariable String file
  ) {
    return getFile( file, "ico" );
  }

  // Get JS files
  @Get( uri = "/public/ui/{file}.js", produces = "text/javascript;charset=utf-8" )
  public HttpResponse<?> getFileJS(
    @PathVariable String file
  ) {
    return getFile( file, "js" );
  }

  // Get PNG files
  @Get( uri = "/public/ui/{file}.png", produces = "image/png" )
  public HttpResponse<?> getFilePNG(
    @PathVariable String file
  ) {
    return getFile( file, "png" );
  }

  // Get SVG files
  @Get( uri = "/public/ui/{file}.svg", produces = "image/svg+xml" )
  public HttpResponse<?> getFileSVG(
    @PathVariable String file
  ) {
    return getFile( file, "svg" );
  }

  // Get TTF files
  @Get( uri = "/public/ui/{file}.ttf", produces = "font/ttf" )
  public HttpResponse<?> getFileTTF(
    @PathVariable String file
  ) {
    return getFile( file, "ttf" );
  }

  // Get WOFF files
  @Get( uri = "/public/ui/{file}.woff", produces = "font/woff" )
  public HttpResponse<?> getFileWOFF(
    @PathVariable String file
  ) {
    return getFile( file, "woff" );
  }

  // Get WOFF2 files
  @Get( uri = "/public/ui/{file}.woff2", produces = "font/woff2" )
  public HttpResponse<?> getFileWOFF2(
    @PathVariable String file
  ) {
    return getFile( file, "woff2" );
  }

  // Get EOT files
  @Get( uri = "/public/ui/{file}.eot", produces = "application/vnd.ms-fontobject" )
  public HttpResponse<?> getFileEOT(
    @PathVariable String file
  ) {
    return getFile( file, "eot" );
  }

  // Generic file fetcher
  private HttpResponse<?> getFile(
    String file,
    String extension
  ) {
    Resource resource = Resource.get( file + "." + extension );
    if( resource == null )
      return HttpResponse.notFound();
    else
      return HttpResponse.ok( resource.data );
  }

}
