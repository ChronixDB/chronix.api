[![Build Status](https://travis-ci.org/ChronixDB/chronix.api.svg)](https://travis-ci.org/ChronixDB/chronix.api)
[![Coverage Status](https://coveralls.io/repos/ChronixDB/chronix.api/badge.svg?branch=master&service=github)](https://coveralls.io/github/ChronixDB/chronix.api?branch=master)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/ChronixDB/chronix.api)
[![Stories in Ready](https://badge.waffle.io/ChronixDB/chronix.api.png?label=ready&title=Ready)](https://waffle.io/ChronixDB/chronix.api)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/ChronixDB/chronix.api/blob/master/LICENSE)
[ ![Download](https://api.bintray.com/packages/chronix/maven/chronix-api/images/download.svg)](https://bintray.com/chronix/maven/chronix-api/_latestVersion)

# Chronix API
The Chronix API defines the Chronix Client class that allows one to stream and store data. 
The Chronix Server and the Chronix Storage provides an implementation that is used by the Chronix Client. 
In the following we are going to explain the important parts of the API.
## Chronix-Client
The Chronix-Client is used to stream and store time series from/to the underlying storage service.
It is a generic client that need some precise information.
The first parameter (`<T>`) describes the type of the time series class (e.g. Kassiopeia-Simple), the second one (`<C>`) defines the connection to the storage (e.g. SolrClient) and the third one (`<Q>`) the query matching to the connection (e.g. SolrQuery).

The following code examples shows the class, constructor and method signatures.
### Class and constructor defintion 
```java
/**
 * The Chronix client to stream and add time series
 *
 * @param <T> the time series type
 * @param <C> the connection class
 * @param <Q> the query class
 * @author f.lautenschlager
 */
public class ChronixClient<T, C, Q> {..}


 /**
* Creates a Chronix client.
*
* @param converter - the converter to handle the time series
* @param service   - the service for accessing the storage
*/
public ChronixClient(TimeSeriesConverter<T> converter, StorageService<T, C, Q> service) {
```
### Add time series to the storage
```java
/**
* Adds the given time series to the given connection.
* Note that the connection is responsible for the commit.
*
* @param timeSeries - the time series of type <T> that should stored
* @param connection - the connection to the server
* @return the server status of the add command.
*/
public boolean add(Collection<T> timeSeries, C connection) {
```

### Stream time series from the storage
```java
/**
* Creates a stream of time series for the given query context and the connection
*
* @param connection - the connection to the storage
* @param query      - the query used by the connection
* @return a stream of time series
*/
public Stream<T> stream(C connection, Q query) {..}
```

## Schema
The Chronix-API defines a schema with the minimum required fields.
```java
public final class Schema {
     /** The id of an document */
    public static final String ID = "id";
    /** The the data field */
    public static final String DATA = "data";
    /** The start as long milliseconds since 1970*/
    public static final String START = "start";
    /** The end as long milliseconds since 1970*/
    public static final String END = "end";
    /** The type of the serialized data field*/
    public static final String TYPE = "type";
    /** Each time series has a name*/
    public static final String NAME = "name";
```
The schema can be extended with arbitrary used-defined attributes.
Chronix can also handle collections of primitive data types.

**Note:** The schema of the underlying storage must match the defined schema.
## Time Series Converter
The constructor of the Chronix-Client needs an implementation of the TimeSeriesConverter interface.
This interface defines how a custom time series class is converted **to** a BinaryTimeSeries and how it is restored **from** a BinaryTimeSeries.
```java
public interface TimeSeriesConverter<T> {
  /**
  * Shall create an object of type T from the given time series document.
  * <p>
  * The time series contains all fields.
  * This method is executed in worker thread and should handle the transformation into
  * a user custom time series object.
  *
  * @param binaryTimeSeries - the time series document containing all stored fields and values
  * @param queryStart       - the start of the query
  * @param queryEnd         - the end of the query
  * @return a concrete object of type T
  */
  T from(BinaryTimeSeries binaryTimeSeries, long queryStart, long queryEnd);

   /**
   * Shall do the conversation of the custom time series T into the binary time series that is stored.
   *
   * @param document - the custom time series with all fields
   * @return the time series document that is stored
   */
   BinaryTimeSeries to(T document);
```
### BinaryTimeSeries
 A BinaryTimeSeries contains all user-defined attributes (host,process, source, thread, ...) and the minimum required fields(id,data,start,end) defined in the schema.
 The following code example shows how one can build a binary time series and access its data.
 ```java
 //Build a binary time series
BinaryTimeSeries binTS = new BinaryTimeSeries.Builder()
                .id("6525-9662-2342")
                .start(Instant.now().toEpochMilli())
                .end(start.plusSeconds(64000).toEpochMilli())
                .data("The-Binary-Large-Object".getBytes())
                .field("host", "production01")
                .field("size", 20)
                .build()
                
//The minimum required fields
binTS.getFields().size(); // 6
binTS.getId(); // "6525-9662-2342"
binTS.getStart(); // start
binTS.getEnd(); // end
binTS.getPoints(); // "The-Binary-Large-Object".getBytes()
//Access user-defined attributes
binTS.get("host"); // "production01"
binTS.get("size"); // 20
 ```
 
## Storage Service
The second argument in the constructor of the Chronix-Client is a storage service.
A storage service implements the StorageService interface.
The interface has the same parameters (`<T>,<C>,<Q>`) as the client.
```java
/**
 * The storage access to stream and add time series
 *
 * @param <T> - the type of the time series returned or added
 * @param <C> - the connection type
 * @param <Q> - the query type used by the connection
 * @author f.lautenschlager
 */
public interface StorageService<T, C, Q> {
    /**
     * Streams time series of type <T> from the given connection using the given query.
     *
     * @param converter  - defines how the time series of type <T> are created
     * @param connection - the connection to the storage
     * @param query      - the query that describe the result
     * @return an iterator on the result set
     */
    Stream<T> stream(TimeSeriesConverter<T> converter, C connection, Q query);

    /**
     * Adds the given collection of time series to the storage
     *
     * @param converter  - the converter for the time series
     * @param documents  - the time series added to the storage
     * @param connection - the connection to the storage
     * @return true if the time series are added correctly, otherwise false
     */
    boolean add(TimeSeriesConverter<T> converter, Collection<T> documents, C connection);
```

### Current implementations:
- Chronix-Server (based on Apache Solr)

## Usage
Build script snippet for use in all Gradle versions, using the Bintray Maven repository:

```groovy
repositories {
    mavenCentral()
    maven { 
        url "http://dl.bintray.com/chronix/maven" 
    }
}
dependencies {
   compile 'de.qaware.chronix:chronix-api:0.1'
}
```

## Contributing

Is there anything missing? Do you have ideas for new features or improvements? You are highly welcome to contribute
your improvements, to the Chronix projects. All you have to do is to fork this repository,
improve the code and issue a pull request.

## Maintainer

Florian Lautenschlager (@flolaut)

## License

This software is provided under the Apache License, Version 2.0 license.

See the `LICENSE` file for details.
