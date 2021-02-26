/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.ThreadContext;
import org.javamoney.moneta.Money;

import javax.money.Monetary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Locale;

class RecordAssembler implements InputStreamReaderFunction<InputStream, TxnLog> {

  @SuppressWarnings("MagicNumber")
  @Override public TxnLog read( InputStream is ) throws IOException {
    var builder = new TxnLogBuilder();

    var typeEnumByte = is.readNBytes( 1 );
    ThreadContext.put( "enum", "0x" + Hex.encodeHexString( typeEnumByte ) );
    var type = TxnType.getByByte( typeEnumByte[0] );
    builder.type( type );

    var epoch = ByteBuffer.wrap( is.readNBytes( 4 ) ).getInt();
    ThreadContext.put( "timestamp", String.valueOf( epoch ) );
    builder.timestamp( Instant.ofEpochSecond( epoch ) );

    var userId = ByteBuffer.wrap( is.readNBytes( 8 ) ).getLong();
    ThreadContext.put( "user id", String.valueOf( userId ) );
    builder.userId( userId );

    if ( EnumSet.of( TxnType.CREDIT, TxnType.DEBIT ).contains( type ) ) {
      var amount = ByteBuffer.wrap( is.readNBytes( 8 ) ).getDouble();
      ThreadContext.put( "amount", String.valueOf( amount ) );
      builder.amount( Money.of( amount, Monetary.getCurrency( Locale.US ) ) );
    }

    return builder.build();
  }
}
