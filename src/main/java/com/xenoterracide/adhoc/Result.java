package com.xenoterracide.adhoc;

import org.immutables.value.Value;
import org.javamoney.moneta.Money;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
interface Result {
  Money totalCredit();

  Money totalDebit();

  long autopaysStartedCount();

  long autopaysEndedCount();

  Money specificUserBalance(); // misdesigned method, but it's based on requirements
}
