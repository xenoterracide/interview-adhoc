# INSTALL

This will download and install it's own copy of JDK 11 if one is not already on the path.

```
./gradlew build
```

# RUN

to get command help you can run, but gradle does complain because the exit code is an error.

```
./gradlew run --quiet
```

path to `.dat` file can be relative or absolute.

```
./gradlew run --quiet --args="src/test/resources/txnlog.dat"
```

You can also enable `debug` logging

```
./gradlew run --quiet --args="--log-level debug src/test/resources/txnlog.dat"
```

# Code Commentary

Gradle is configured to use errorprone, and checker framework. These two frameworks can, and are checking
that `NullPointerException`'s can not occur, that a `switch`/`case` is exhaustive in its use of an enum, and that
variables are effectively `final`. This means that some code that you might normally want is not required at runtime.

The code could still use some naming improvements, and I'm not super happy about how the `specificUserId` thing works.
