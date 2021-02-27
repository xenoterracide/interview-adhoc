/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import java.util.function.BiFunction;

class ResultViewModel implements BiFunction<Result, TxnLog, Result> {

  private final long userId;

  ResultViewModel( long userId ) {
    this.userId = userId;
  }

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

    if ( this.userId == txnLog.userId() ) {
      if ( TxnType.CREDIT.equals( txnLog.type() ) ) {
        txnLog.amount()
          .map( amount -> amount.add( result.specificUserBalance() ) )
          .ifPresent( builder::specificUserBalance );
      }
      if ( TxnType.DEBIT.equals( txnLog.type() ) ) {
        txnLog.amount()
          .map( amount -> amount.subtract( result.specificUserBalance() ) )
          .ifPresent( builder::specificUserBalance );
      }
    }

    return builder.build();
  }
}
