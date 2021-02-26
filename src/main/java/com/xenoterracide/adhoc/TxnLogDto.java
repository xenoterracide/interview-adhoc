/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;
import org.javamoney.moneta.Money;

import java.time.Instant;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
interface TxnLogDto {

  TxnType type();

  Instant timestamp();

  long userId();

  @Nullable Money amount();
}
