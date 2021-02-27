/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
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

  @SuppressWarnings("NullAway.Init")
  @CommandLine.Option(
    names = {"--log-level"},
    defaultValue = "error",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "change logging level"
  )
  private final Level logLevel = Level.ERROR;

  public static void main( String... args ) {
    var cli = new CommandLine( new Application() );
    cli.setExecutionExceptionHandler( new ExceptionHandler() );
    cli.registerConverter( Level.class, Level::valueOf );
    System.exit( cli.execute( args ) );
  }

  @Override public void run() {
    configureLog4j( logLevel );
    var p = new FileProcessor();
    var res = p.process( file );
    var view = new TxnLogReportView();
    System.out.println( view.apply( res ) );
  }

  static void configureLog4j( Level level ) {
    var console = "console";
    var builder = ConfigurationBuilderFactory.newConfigurationBuilder();

    var defaultAppender = builder.newAppender( console, "CONSOLE" )
      .addAttribute( "target", ConsoleAppender.Target.SYSTEM_OUT );
    defaultAppender.add( builder.newLayout( "PatternLayout" )
      .addAttribute( "pattern", "%highlight{%-5level} - %msg%n      - Context %MDC%n%throwable" ) );

    builder.add( defaultAppender );
    builder.add( builder.newRootLogger( Level.ERROR )
      .add( builder.newAppenderRef( console ) ) );
    builder.add( builder.newLogger( "com.xenoterracide", level ) );
    Configurator.initialize( builder.build() );
  }

  static class ExceptionHandler implements CommandLine.IExecutionExceptionHandler {

    @Override
    public int handleExecutionException(
      Exception ex,
      CommandLine commandLine,
      CommandLine.ParseResult parseResult
    ) {
      var log = LogManager.getLogger( this.getClass() );
      log.atError().withThrowable( ex ).log( "" );
      log.atDebug().withThrowable( ex ).log( "" );
      return 1;
    }
  }
}
