/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import picocli.CommandLine;

import java.nio.file.Path;

@SuppressWarnings("initialization.fields.uninitialized")
public class Application implements Runnable {

  @SuppressWarnings("NullAway.Init")
  @CommandLine.Parameters
  private Path file;

  public static void main( String... args ) {
    configureLog4j();
    var cli = new CommandLine( new Application() );
    System.exit( cli.execute( args ) );
  }

  @Override public void run() {
    var p = new FileProcessor();
    p.accept( file );
  }

  static void configureLog4j() {
    var console = "console";
    var builder = ConfigurationBuilderFactory.newConfigurationBuilder();

    var defaultAppender = builder.newAppender( console, "CONSOLE" )
      .addAttribute( "target", ConsoleAppender.Target.SYSTEM_OUT );
    defaultAppender.add( builder.newLayout( "PatternLayout" )
      .addAttribute( "pattern", "%highlight{%-5level} - %msg%n      - Context %MDC%n%throwable" ) );

    builder.add( defaultAppender );
    builder.add( builder.newRootLogger( Level.ERROR )
      .add( builder.newAppenderRef( console ) ) );
    builder.add( builder.newLogger( "com.xenoterracide", Level.DEBUG ) );
    Configurator.initialize( builder.build() );
  }
}
