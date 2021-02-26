/* Copyright © 2021 Caleb Cushing. All rights reserved */
package com.xenoterracide.adhoc;

import java.io.IOException;

public interface InputStreamReaderFunction<T, R> {
  R read( T t ) throws IOException;
}
