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
package de.qaware.chronix.streaming;

import de.qaware.chronix.converter.DocumentConverter;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * The storage access to stream and add documents
 *
 * @param <T> - the type of the document returned or added
 * @param <C> - the connection type
 * @param <Q> - the query type used by the connection
 * @author f.lautenschlager
 */
public interface StorageService<T, C, Q> {
    /**
     * Streams documents of type <T> from the given connection using the given query.
     *
     * @param converter             - defines how the documents of type <T> are created
     * @param connection            - the connection to the storage
     * @param query                 - the query that describe the result
     * @param queryStart            - the start of the query range
     * @param queryEnd              - the end of the query range
     * @param nrOfDocumentsPerBatch - number of documents that are processed in a batch
     * @return an iterator on the result set
     */
    Stream<T> stream(DocumentConverter<T> converter, C connection, Q query, long queryStart, long queryEnd, int nrOfDocumentsPerBatch);

    /**
     * @param converter  - the converter for the documents
     * @param documents  - the documents added to the connection
     * @param connection - the connection to the data source
     * @return true if the documents are added correctly, otherwise false
     */
    boolean add(DocumentConverter<T> converter, Collection<T> documents, C connection);


}
