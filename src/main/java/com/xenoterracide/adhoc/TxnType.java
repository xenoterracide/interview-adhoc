/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import java.util.stream.Stream;

public enum TxnType {
  DEBIT( (byte) 0x00 ),
  CREDIT( (byte) 0x01 ),
  START_AUTOPAY( (byte) 0x02 ),
  END_AUTOPAY( (byte) 0x03 );

  private final byte byteValue;

  TxnType( byte byteValue ) {
    this.byteValue = byteValue;
  }

  public static TxnType getByByte( byte byteValue ) {
    return Stream.of( TxnType.values() )
      .filter( type -> Byte.valueOf( type.byteValue ).equals( byteValue ) )
      .findFirst()
      .orElseThrow( () -> new IllegalArgumentException(
        String.format( "%b is not valid", byteValue )
      ) );
  }
}
