/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;

class HeaderValidator implements InputStreamReaderBiConsumer<InputStream, Path> {

  @SuppressWarnings("MagicNumber")
  @Override public void consume( InputStream is, Path fPath ) throws IOException {
    var ft = is.readNBytes( 4 );
    if ( !Arrays.equals( ft, new byte[]{'M', 'P', 'S', '7'} ) ) {
      throw new IllegalArgumentException( String.format( "%s is not of correct format", fPath ) );
    }

    if ( is.skip( 1 + 4 ) != 5 ) { // skip version and record count
      throw new IllegalArgumentException( String.format( "%s is incomplete", fPath ) );
    }
  }
}
