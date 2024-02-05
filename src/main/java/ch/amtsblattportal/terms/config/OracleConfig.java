package ch.amtsblattportal.terms.config;

import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties( "oracle" )
public class OracleConfig {

  @NotBlank private String database, username, password;
  private String ons;
  @ConfigurationBuilder( configurationPrefix = "truststore" ) private final TruststoreConfig truststore = new TruststoreConfig();
  @ConfigurationBuilder( configurationPrefix = "wallet" ) private final WalletConfig wallet = new WalletConfig();

  // Get Oracle Database (TNS) connection string
  public String getDatabase() {
    return database;
  }

  // Set Oracle Database (TNS) connection string
  public void setDatabase( String database ) {
    this.database = database;
  }

  // Get Oracle Notification Service hosts
  public String getOns() {
    return ons;
  }

  // Set Oracle Notification Service hosts
  public void setOns( String ons ) {
    if( ons != null && ons.isEmpty() )
      this.ons = null;
    else
      this.ons = ons;
  }

  // Get Oracle username
  public String getUsername() {
    return username;
  }

  // Set Oracle username
  public void setUsername( String username ) {
    this.username = username;
  }

  // Get Oracle password
  public String getPassword() {
    return password;
  }

  // Set Oracle password
  public void setPassword( String password ) {
    this.password = password;
  }

  // Get Oracle truststore configuration
  public TruststoreConfig getTruststore() {
    return truststore;
  }

  // Oracle truststore configuration
  public static class TruststoreConfig {

    @NotBlank private String path, password;

    // Get path to truststore
    public String getPath() {
      return path;
    }

    // Set path to truststore
    public void setPath( String path ) {
      if( path == null || path.isEmpty() )
        this.path = null;
      else
        this.path = path.replaceAll( "^file:", "" );
    }

    // Get password for truststore
    public String getPassword() {
      return password;
    }

    // Set password for truststore
    public void setPassword( String password ) {
      if( password == null || password.isEmpty() )
        this.password = null;
      else
        this.password = password;
    }

  }

  // Get Oracle Wallet configuration
  public WalletConfig getWallet() {
    return wallet;
  }

  // Oracle Wallet configuration
  public static class WalletConfig {

    @NotBlank private String path, password;

    // Get path to wallet
    public String getPath() {
      return path;
    }

    // Set path to wallet
    public void setPath( String path ) {
      if( path == null || path.isEmpty() )
        this.path = null;
      else
        this.path = path.replaceAll( "^file:", "" );
    }

    // Get password for wallet
    public String getPassword() {
      return password;
    }

    // Set password for wallet
    public void setPassword( String password ) {
      if( password == null || password.isEmpty() )
        this.password = null;
      else
        this.password = password;
    }

  }

}
