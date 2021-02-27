/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import com.google.errorprone.annotations.Var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.javamoney.moneta.Money;

import javax.money.Monetary;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class FileProcessor {
  private static final long USER_ID = 2456938384156277127L;

  private final Logger log = LogManager.getLogger( this.getClass() );

  private final HeaderValidator hp = new HeaderValidator();

  private final RecordAssembler rp = new RecordAssembler();

  public Result process( Path fPath ) {
    try (
      var is = new BufferedInputStream( Files.newInputStream( fPath ) )
    ) {
      hp.consume( is, fPath );
      var zeroDollars = Money.of( 0, Monetary.getCurrency( Locale.US ) );

      var vm = new ResultViewModel( USER_ID );

      @Var var currentResult = new ResultBuilder()
        .autopaysEndedCount( 0 )
        .autopaysStartedCount( 0 )
        .specificUserBalance( zeroDollars )
        .totalDebit( zeroDollars )
        .totalCredit( zeroDollars )
        .build();

      @Var var recordNum = 1;
      while ( is.available() > 0 ) {
        ThreadContext.put( "record", String.valueOf( recordNum ) );
        var record = rp.read( is );
        log.debug( "{}", record );
        currentResult = vm.apply( currentResult, record );
        recordNum += 1;
      }
      return currentResult;
    }
    catch ( IOException e ) {
      log.error( "", e );
      throw new UncheckedIOException( e );
    }
  }
}
