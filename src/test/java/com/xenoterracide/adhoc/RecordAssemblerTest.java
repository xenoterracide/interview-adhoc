/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;


import org.apache.logging.log4j.LogManager;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class RecordAssemblerTest {

  @BeforeAll
  static void configureLog4j() {
    Application.configureLog4j();
  }

  @Test
  void parsers() throws Exception {
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
          Money.of( 604.274335557087, Monetary.getCurrency( Locale.US ) )
        );
    }
  }

  @Test
  void fileProcessor() throws Exception {
    var resource = FileProcessor.class.getResource( "/txnlog.dat" );
    var path = Path.of( resource.toURI() );
    new FileProcessor().accept( path );
  }

}
