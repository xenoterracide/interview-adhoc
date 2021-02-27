/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.function.MonetaryOperators;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FileProcessorTest {

  private static final CurrencyUnit USD = Monetary.getCurrency( Locale.US );

  @BeforeAll
  static void configureLog4j() {
    Application.configureLog4j( Level.DEBUG );
  }

  @Test
  void flow() throws Exception {
    var resource = FileProcessor.class.getResource( "/txnlog.dat" );
    var path = Path.of( resource.toURI() );
    var hp = new HeaderValidator();
    var rp = new RecordAssembler();

    try (
      var is = new BufferedInputStream( Files.newInputStream( path ) )
    ) {
      hp.consume( is, path );
      var dto = rp.read( is );
      LogManager.getLogger().debug( "{}", dto );
      assertThat( dto )
        .extracting(
          TxnLog::type,
          TxnLog::timestamp,
          TxnLog::userId,
          TxnLog::amount
        )
        .containsExactly(
          TxnType.DEBIT,
          Instant.ofEpochSecond( 1393108945 ),
          4136353673894269217L,
          Optional.of( Money.of( 604.274335557087, USD ) )
        );
    }
  }

  @Test
  void fileProcessor() throws Exception {
    var resource = FileProcessor.class.getResource( "/txnlog.dat" );
    var path = Path.of( resource.toURI() );
    var result = new FileProcessor().process( path );
    assertThat( result )
      .extracting(
        res -> res.totalCredit().with( MonetaryOperators.rounding( 2 ) ),
        res -> res.totalDebit().with( MonetaryOperators.rounding( 2 ) ),
        res -> res.specificUserBalance().with( MonetaryOperators.rounding( 2 ) ),
        Result::autopaysStartedCount,
        Result::autopaysEndedCount
      )
      .containsExactly(
        Money.of( 10073.36, USD ),
        Money.of( 18203.70, USD ),
        Money.of( 497.17, USD ),
        11L,
        8L
      );

    var vm = new TxnLogReportView();
    var report = vm.apply( result );
    assertThat( report ).isEqualTo(
      String.format( "total credit amount=10,073.36%n" +
        "total debit amount=18,203.70%n" +
        "autopays started=11%n" +
        "autopays ended=8%n" +
        "balance for user 2456938384156277127=497.17%n" )
    );
  }

}
