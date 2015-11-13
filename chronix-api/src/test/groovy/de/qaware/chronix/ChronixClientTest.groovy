package de.qaware.chronix

import de.qaware.chronix.converter.DocumentConverter
import de.qaware.chronix.streaming.StorageService
import spock.lang.Specification

/**
 * Created by f.lautenschlager on 13.11.2015.
 */
class ChronixClientTest extends Specification {
    def "test stream"() {
        given:
        def converter = Mock(DocumentConverter.class)
        def service = Mock(StorageService.class)

        def connection = Mock(Object.class)
        def query = Mock(Object.class)

        ChronixClient client = new ChronixClient(converter, service)
        when:

        client.stream(connection, query, 0, 1, 200)

        then:
        1 * service.stream(_, _, _, _, _, _)

    }

    def "test add"() {
        given:
        def converter = Mock(DocumentConverter.class)
        def service = Mock(StorageService.class)
        def connection = Mock(Object.class)

        ChronixClient client = new ChronixClient(converter, service)

        when:
        def someTimeSeries = new ArrayList()
        client.add(someTimeSeries, connection)

        then:
        1 * service.add(_, _, _)
    }
}
