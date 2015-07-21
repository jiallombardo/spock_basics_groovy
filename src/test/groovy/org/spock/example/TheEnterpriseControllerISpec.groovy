package org.spock.example

import groovyx.net.http.RESTClient
import org.spock.example.configuration.TheEnterpriseConfiguration
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification

import javax.annotation.Resource

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.mock.web.MockHttpServletRequest.DEFAULT_PROTOCOL
import static org.springframework.mock.web.MockHttpServletRequest.DEFAULT_REMOTE_HOST

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = TheEnterpriseConfiguration)
@WebAppConfiguration
@IntegrationTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener)
class TheEnterpriseControllerISpec extends Specification
{
    private static final String THE_ENTERPRISE_IS_DOCKED = 'enterprise.docked'

    @Resource(name = "defaultNumberOfOfficers")
    private int defaulNumberOfOfficers

    @Shared
    private RESTClient restClient

    def setup() {
        restClient = new RESTClient("${DEFAULT_PROTOCOL}://${DEFAULT_REMOTE_HOST}:8080")
        restClient.setHeaders(Accept: APPLICATION_JSON_VALUE)
    }

    @IgnoreIf({ System.getProperty(THE_ENTERPRISE_IS_DOCKED) == 'false' })
    def 'it is possible to train officers via a web endpoint invocation'()
    {
        when:
        def response = restClient.post(path: "/officers",
                query: [number: 3],
                contentType: APPLICATION_JSON_VALUE)

        then:
        checkResponse(response, 3)
    }

    @IgnoreIf({ System.getProperty(THE_ENTERPRISE_IS_DOCKED) == 'false' })
    def 'it is possible to train a default number of officers'() {
        when:
        def response = restClient.post(path: "/officers",
                contentType: APPLICATION_JSON_VALUE)

        then:
        checkResponse(response, defaulNumberOfOfficers)
    }

    private def checkResponse(def response, int numberOfOfficers) {

        response.status == OK.value()
        response.data.size == numberOfOfficers
        response.data.eachWithIndex { val, index ->
            val.with {
                assert title == 'trainee'
                assert name == "trainee $index".toString()
            }
        }
    }
}
