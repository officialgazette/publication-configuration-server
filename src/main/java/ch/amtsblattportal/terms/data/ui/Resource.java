package ch.amtsblattportal.terms.data.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {

  static final Logger LOG = LoggerFactory.getLogger( Resource.class.getName() );
  private static final HashMap<String, Resource> RESOURCES = new HashMap<>();
  private static final String PATH = "ui/";

  // Get named resource
  public static Resource get( String name ) {
    return RESOURCES.get( name );
  }

  // Load all resources
  public static void load() {
    LOG.info( "Loading user interface resources" );
    load( "assets/i18n/de.json" );
    load( "assets/i18n/en.json" );
    load( "assets/i18n/fr.json" );
    load( "assets/i18n/it.json" );
    load( "color.dae87a04d07ca92b.png" );
    load( "favicon.ico" );
    load( "hue.8b1818380241e6ac.png" );
    load( "index.html" );
    load( "main.f321d8902f026bff.js" );
    load( "polyfills.499bf1a0a255fd80.js" );
    load( "primeicons.943ab24c43224d29.svg" );
    load( "primeicons.0112589c5695a9ed.ttf" );
    load( "primeicons.ba3f916dfb64be8c.woff2" );
    load( "primeicons.f8b9e8a4e401b603.woff" );
    load( "primeicons.ffecb2549ad1765a.eot" );
    load( "runtime.89bfad0fe920d2c9.js" );
    load( "styles.6966db6fde476a5f.css" );
  }

  // Load specific resources
  private static void load( String name ) {
    RESOURCES.put( name, new Resource( name ));
  }

  public final byte[] data;

  // Constructor for loading resource from classpath
  public Resource( String name ) {
    ClassLoader cl = Resource.class.getClassLoader();
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    try( InputStream input = cl.getResourceAsStream( PATH + name )) {
      int i;
      byte[] block = new byte[16384];
      if( input != null ) {
        while(( i = input.read( block, 0, block.length )) != -1 )
          buffer.write( block, 0, i );
      } else {
        throw new NullPointerException();
      }
      buffer.flush();
    } catch( IOException|NullPointerException ex ) {
      LOG.error( "Error while loading resource " + name, ex );
    }
    data = buffer.toByteArray();
  }

}
