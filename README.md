Younicos Programming Task
=========================

This Java project implements a Younicos programming task.  

Requirements
------------

* Java 7 or higher
* Maven 3

Quickstart
-----------

    $> mvn package
    $> java -jar target/Younicos-<...>-jar-with-dependencies.jar -h
    $> java -jar target/Younicos-<...>-jar-with-dependencies.jar -m 6 -f src/test/resources/Profile1.xml
    $> java -jar target/Younicos-<...>-jar-with-dependencies.jar -m 6 -f src/test/resources/Profile2.xml

Future Improvements
-------------------

1. Reconsider exception handling
2. Use logging output instead of `System.out` or `System.err` with customizable level, file and/or STDERR
3. Better error messages when validation of power profile fails: give reason and position in file
4. Validate again `.xsd` schema (either from URL or as additional option of command line)
5. Integration tests