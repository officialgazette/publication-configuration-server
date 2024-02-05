package ch.amtsblattportal.terms.db;

import ch.amtsblattportal.terms.config.OracleConfig;
import io.micronaut.context.ApplicationContext;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oracle.net.ns.SQLnetDef;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Oracle {

  static final Logger LOG = LoggerFactory.getLogger( Oracle.class.getName() );
  private final static PoolDataSource PDS = PoolDataSourceFactory.getPoolDataSource();

  public static void connect() throws SQLException {

    ApplicationContext ctx = ApplicationContext.run();
    OracleConfig oracle = ctx.getBean( OracleConfig.class );

    if( oracle.getOns() == null )
      LOG.info( "Connecting to Oracle Standalone Database" );
    else
      LOG.info( "Connecting to Oracle Real Application Cluster" );
    PDS.setConnectionProperty( "oracle.net.ssl_version", "1.2" );
    PDS.setConnectionProperty( "oracle.net.ssl_cipher_suites", "(TLS_RSA_WITH_AES_128_GCM_SHA256)" );
    PDS.setConnectionProperty( "oracle.net.ssl_server_dn_match", "true" );
    OracleConfig.TruststoreConfig truststore = oracle.getTruststore();
    if( truststore.getPath() != null ) {
      PDS.setConnectionProperty( "javax.net.ssl.trustStore", truststore.getPath() );
      PDS.setConnectionProperty( "javax.net.ssl.trustStoreType", "JKS" );
      PDS.setConnectionProperty( "javax.net.ssl.trustStorePassword", truststore.getPassword() );
    }
    PDS.setConnectionFactoryClassName( "oracle.jdbc.pool.OracleDataSource" );
    PDS.setUser( oracle.getUsername() );
    PDS.setPassword( oracle.getPassword() );
    String url = "jdbc:oracle:thin:@" + oracle.getDatabase();
    PDS.setURL( url );
    Properties properties = new Properties();
    properties.put( SQLnetDef.TCP_CONNTIMEOUT_STR, "5000" );
    PDS.setConnectionProperties( properties );
    PDS.setConnectionPoolName( "OraclePool" );
    PDS.setValidateConnectionOnBorrow( true );
    PDS.setMinPoolSize( 1 );
    PDS.setMaxPoolSize( 3 );
    // RAC
    if( oracle.getOns() != null ) {
      String config = "nodes=" + oracle.getOns();
      OracleConfig.WalletConfig wallet = oracle.getWallet();
      if( wallet != null ) {
        config += "\nwalletfile=" + wallet.getPath();
        config += "\nwalletpassword=" + wallet.getPassword();
      }
      PDS.setONSConfiguration( config );
      PDS.setFastConnectionFailoverEnabled( true );
    }
    // END RAC
    PDS.setInitialPoolSize( 1 );
    try(
      Connection connection = PDS.getConnection();
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery( "SELECT 'test' FROM DUAL" );
    ) {
      rs.next();
    } catch( SQLException ex ) {
      LOG.error( "Could not connect to database", ex );
      System.exit( 1 );
    }
    LOG.info( "Connection established" );
  }

  public static List<String> getTenants() throws SQLException {
    List<String> tenants = new ArrayList<>();
    try(
      Connection connection = PDS.getConnection();
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery( "SELECT id FROM config WHERE id != 'ROOT'" );
    ) {
      while( rs.next() )
        tenants.add( rs.getString( 1 ));
    }
    return tenants;
  }

  public static String getJson( String id ) throws SQLException {
    try(
      Connection connection = PDS.getConnection();
      PreparedStatement psql = connection.prepareStatement( "SELECT json FROM config WHERE id = ?" );
    ) {
      psql.closeOnCompletion();
      psql.setString( 1, id );
      try( ResultSet rs = psql.executeQuery() ) {
        if( rs.next() ) {
          Clob clob = rs.getClob( 1 );
          return clob.getSubString( 1, (int) clob.length() );
        }
      }
    }
    return null;
  }

  public static void setJson( String json ) throws SQLException {
    setJson( "ROOT", json );
  }

  public static void setJson( String id, String json ) throws SQLException {
    try( Connection connection = PDS.getConnection() ) {
      boolean existing = false;
      try( PreparedStatement psql = connection.prepareStatement( "SELECT COUNT(*) FROM config WHERE id = ?" )) {
        psql.closeOnCompletion();
        psql.setString( 1, id );
        try( ResultSet rs = psql.executeQuery() ) {
          rs.next();
          if( rs.getInt( 1 ) > 0 )
            existing = true;
        }
      }
      String sql;
      if( existing )
        sql = "UPDATE config SET json = ? WHERE id = ?";
      else
        sql = "INSERT INTO config ( json, id ) VALUES ( ?, ? )";
      try( PreparedStatement psql = connection.prepareStatement( sql )) {
        psql.closeOnCompletion();
        Clob clob = connection.createClob();
        clob.setString(1, json );
        psql.setClob( 1, clob );
        psql.setString( 2, id );
        psql.executeUpdate();
        clob.free();
      }
    }
  }

}
