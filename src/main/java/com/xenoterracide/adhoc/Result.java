/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import org.immutables.value.Value;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
interface Result {
  MonetaryAmount totalCredit();

  MonetaryAmount totalDebit();

  long autopaysStartedCount();

  long autopaysEndedCount();

  // misdesigned method, but it's based on requirements,
  // would be better maybe to allow fetching a specific user by id
  Money specificUserBalance();
}
