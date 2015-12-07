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
package de.qaware.chronix.converter;

import de.qaware.chronix.Schema;

import java.util.HashMap;
import java.util.Map;

/**
 * The binary time series contains at least the required fields (id, start,end,data)
 * and an arbitrary list of fields.
 * The data field is a binary large object.
 *
 * @author f.lautenschlager
 */
public class BinaryTimeSeries {

    /**
     * The fields of the time series
     */
    private Map<String, Object> fields = new HashMap<>();

    /**
     * Default constructor
     */
    private BinaryTimeSeries() {
    }

    /**
     * Adds an arbitrary field to the time series
     *
     * @param field - the field key
     * @param value - the field value
     */
    private void put(String field, Object value) {
        fields.put(field, value);
    }

    /**
     * Gets the field for the given field name
     *
     * @param field - the field name
     * @return the field value as object
     */
    public Object get(String field) {
        return fields.get(field);
    }

    /**
     * @return - the GUID as string
     */
    public String getId() {
        return String.valueOf(fields.getOrDefault(Schema.ID, null));
    }

    /**
     * Sets the user defined id. Solr sets it's own id if no id is set
     *
     * @param id - the id as guid string
     */
    private void setId(String id) {
        fields.put(Schema.ID, id);
    }

    /**
     * @return the fields and values of the time series
     */
    public Map<String, Object> getFields() {
        return new HashMap<>(fields);
    }

    /**
     * @return the binary encoded points
     */
    public byte[] getPoints() {
        return (byte[]) fields.getOrDefault(Schema.DATA, new byte[]{});
    }

    /**
     * @param blob - the binary large object containing the points
     */
    private void setPoints(byte[] blob) {
        fields.put(Schema.DATA, blob);
    }

    /**
     * @param start the start of the data blob
     */
    public void setStart(long start) {
        fields.put(Schema.START, start);
    }

    /**
     * @return the stat of the data blob
     */
    public long getStart() {
        return (long) fields.get(Schema.START);
    }

    /**
     * @param end the end of the data blob
     */
    public void setEnd(long end) {
        fields.put(Schema.END, end);
    }

    /**
     * @return the end of the data blob
     */
    public long getEnd() {
        return (long) fields.get(Schema.END);
    }


    /**
     * The Builder class
     */
    public static final class Builder {

        /**
         * The time series object
         */
        private BinaryTimeSeries binaryTimeSeries;

        /**
         * Constructs a new Builder
         */
        public Builder() {
            binaryTimeSeries = new BinaryTimeSeries();
        }


        /**
         * @return the filled time series
         */
        public BinaryTimeSeries build() {
            return binaryTimeSeries;
        }

        /**
         * Objects are mapped to the available data types of solr.
         *
         * @param field - the field name
         * @param value - the field value
         * @return the builder
         */
        public Builder field(String field, Object value) {
            binaryTimeSeries.put(field, value);
            return this;
        }


        /**
         * Sets the points of this time series
         *
         * @param blob - the binary large object
         * @return the builder
         */
        public Builder data(byte[] blob) {
            binaryTimeSeries.setPoints(blob);
            return this;
        }

        /**
         * Sets the start of the time series
         *
         * @param start - the start time stamp
         * @return the builder
         */
        public Builder start(long start) {
            binaryTimeSeries.setStart(start);
            return this;
        }

        /**
         * Sets the end of the time series
         *
         * @param end - the end time stamp
         * @return the builder
         */
        public Builder end(long end) {
            binaryTimeSeries.setEnd(end);
            return this;
        }

        /**
         * Sets the id of this time series. If no id is set, Solr sets the id on the commit
         *
         * @param guid - the guid as string representation
         * @return the builder
         */
        public Builder id(String guid) {
            binaryTimeSeries.setId(guid);
            return this;
        }
    }

}
