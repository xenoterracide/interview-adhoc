/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import java.io.IOException;

interface InputStreamReaderBiConsumer<T, U> {
  void consume( T t, U u ) throws IOException;
}
