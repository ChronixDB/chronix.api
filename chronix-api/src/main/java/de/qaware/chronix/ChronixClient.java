/*
 *    Copyright (C) 2015 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.chronix;


import de.qaware.chronix.converter.DocumentConverter;
import de.qaware.chronix.streaming.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The Chronix client to stream and add time series
 *
 * @param <T> the time series type
 * @param <C> the connection class
 * @param <Q> the query class
 * @author f.lautenschlager
 */
public class ChronixClient<T, C, Q> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChronixClient.class);

    private final DocumentConverter<T> converter;
    private final StorageService<T, C, Q> service;


    /**
     * Creates a chronix client.
     *
     * @param converter - the converter to handle the documents
     * @param service   - the service for accessing the storage
     */
    public ChronixClient(DocumentConverter<T> converter, StorageService<T, C, Q> service) {
        this.converter = converter;
        this.service = service;
        LOGGER.debug("Creating ChronixClient with Converter {} for Storage {}", converter, service);
    }

    /**
     * Creates a stream of time series for the given query context and the connection
     *
     * @param connection            - the connection to the storage
     * @param query                 - the query used by the connection
     * @param queryStart            - the start time range of the query
     * @param queryEnd              - the end time range of the query
     * @param nrOfDocumentsPerBatch - number of documents that are processed at once
     * @return a stream of time series
     */
    public Stream<T> stream(C connection, Q query, long queryStart, long queryEnd, int nrOfDocumentsPerBatch) {
        LOGGER.debug("Streaming documents from {} with query {}, starting at {}, stopping at {}, number of documents per batch {]", connection, query, queryStart, queryEnd, nrOfDocumentsPerBatch);
        return service.stream(converter, connection, query, queryStart, queryEnd, nrOfDocumentsPerBatch);
    }

    /**
     * Add the given documents to the given connection.
     * Note that the connection is responsible for the commit.
     *
     * @param timeSeries - the time series of type <T> that should stored
     * @param connection - the connection to the server
     * @return the server status of the add command.
     */
    public boolean add(Collection<T> timeSeries, C connection) {
        LOGGER.debug("Adding {} to {}", timeSeries, connection);
        return service.add(converter, timeSeries, connection);
    }
}
