# PjBL01-MapReduce-1

This project is now configured as a Maven Java project with Hadoop dependencies.

## Requirements

- Java 11+
- Maven 3.9+

## Build

```bash
mvn -DskipTests compile
```

## Run the sample `Main`

```bash
mvn -q exec:java -Dexec.mainClass=Main
```

## Hadoop dependencies

Configured in `pom.xml`:

- `org.apache.hadoop:hadoop-common:3.3.6`
- `org.apache.hadoop:hadoop-mapreduce-client-core:3.3.6`

