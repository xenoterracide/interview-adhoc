/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import org.immutables.value.Value;

import javax.money.MonetaryAmount;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
interface Result {
  MonetaryAmount totalCredit();

  MonetaryAmount totalDebit();

  long autopaysStartedCount();

  long autopaysEndedCount();

  MonetaryAmount specificUserBalance();
}
