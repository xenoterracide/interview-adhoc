/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import com.google.errorprone.annotations.Var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileProcessor implements Consumer<Path> {

  private final Logger log = LogManager.getLogger( this.getClass() );
  private final HeaderValidator hp = new HeaderValidator();
  private final RecordAssembler rp = new RecordAssembler();

  @Override public void accept( Path fPath ) {
    try (
      var is = new BufferedInputStream( Files.newInputStream( fPath ) )
    ) {
      hp.consume( is, fPath );
      @Var var recordNum = 1;
      while ( is.available() > 0 ) {
        ThreadContext.put( "record", String.valueOf( recordNum ) );
        var record = rp.read( is );
        log.debug( "{}", record );
        recordNum += 1;
      }
    }
    catch ( IOException e ) {
      log.error( "", e );
    }
  }
}
