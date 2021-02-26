/* Copyright © 2021 Caleb Cushing. All rights reserved */
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

  // misdesigned method, but it's based on requirements,
  // would be better maybe to allow fetching a specific user by id
  Money specificUserBalance();
}
