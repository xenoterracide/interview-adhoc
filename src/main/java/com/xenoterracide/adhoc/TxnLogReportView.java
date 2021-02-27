/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import org.javamoney.moneta.format.AmountFormatParams;

import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryFormats;

import java.util.Locale;
import java.util.function.Function;

class TxnLogReportView implements Function<Result, String> {

  @Override public String apply( Result result ) {
    var afq = AmountFormatQueryBuilder.of( Locale.US )
      .set( AmountFormatParams.PATTERN, "#,##0.00" )
      .build();
    var mfmt = MonetaryFormats.getAmountFormat( afq );

    return String.format(
      "total credit amount=%s%n" +
        "total debit amount=%s%n" +
        "autopays started=%d%n" +
        "autopays ended=%d%n" +
        "balance for user 2456938384156277127=%s%n",
      mfmt.format( result.totalCredit() ),
      mfmt.format( result.totalDebit() ),
      result.autopaysStartedCount(),
      result.autopaysEndedCount(),
      mfmt.format( result.specificUserBalance() )
    );
  }
}
