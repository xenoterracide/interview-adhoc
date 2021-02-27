/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import java.util.function.BiFunction;

public class ResultViewModel implements BiFunction<Result, TxnLog, Result> {

  @Override public Result apply( Result result, TxnLog txnLog ) {
    var builder = new ResultBuilder().from( result );
    switch ( txnLog.type() ) {
      case DEBIT: {
        txnLog.amount()
          .map( amount -> amount.add( result.totalDebit() ) )
          .ifPresent( builder::totalDebit );
        break;
      }
      case CREDIT: {
        txnLog.amount()
          .map( amount -> amount.add( result.totalCredit() ) )
          .ifPresent( builder::totalCredit );
        break;
      }
      case END_AUTOPAY: {
        builder.autopaysEndedCount( result.autopaysEndedCount() + 1 );
        break;
      }
      case START_AUTOPAY: {
        builder.autopaysStartedCount( result.autopaysStartedCount() + 1 );
        break;
      }
    }

    return builder.build();
  }
}
