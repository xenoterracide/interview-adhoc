/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import org.immutables.value.Value;

import javax.money.MonetaryAmount;

import java.time.Instant;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
interface TxnLog {

  TxnType type();

  Instant timestamp();

  long userId();

  Optional<MonetaryAmount> amount();
}
