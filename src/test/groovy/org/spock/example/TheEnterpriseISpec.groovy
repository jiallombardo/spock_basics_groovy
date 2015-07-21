package org.spock.example

import org.spock.example.beans.Clearance
import org.spock.example.configuration.TheEnterpriseConfiguration
import org.spock.example.provider.ClearanceProvider
import org.spock.example.beans.Officer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(classes = TheEnterpriseConfiguration)
@TestExecutionListeners(DependencyInjectionTestExecutionListener)
class TheEnterpriseISpec extends Specification {

    @Autowired
    private TheEnterprise testShip

    private def testProvider = Mock(ClearanceProvider)

    def setup() {
        testShip.clearanceProvider = testProvider //use reflection to insert new field value
    }

    @Unroll
    def '#title #name has proper access to the Enterprise'() {
        setup: 'a parameterized Officer'
            def officer = new Officer(name, title)
            (officer.name == 'Spock' ? 0 : 1) * testProvider.getClearance(title) >> testClearanceBehavior(title)

        when: 'get clearance for this officer'
            def result = testShip.clearanceToSystems(officer)

        then: 'the clearance level is correct'
            result == expectedResult

        where:
            name     | title           || expectedResult
            'Kirk'   | 'Captain'       || Clearance.UNRESTRICTED
            'Spock'  | 'Intruder'      || Clearance.UNRESTRICTED
            'Spock'  | 'First Officer' || Clearance.UNRESTRICTED
            'Kirk'   | 'Intruder'      || Clearance.UNAUTHORIZED
            'Scotty' | 'First Officer' || Clearance.LIMITED
            'Nyota'  | 'Lieutenant'    || null
    }

    def testClearanceBehavior(String officerTitle) {
        switch (officerTitle) {
            case 'Captain':
                return Clearance.UNRESTRICTED

            case 'First Officer':
                return Clearance.LIMITED

            case 'LieutenantCommander':
                return Clearance.LIMITED

            case 'Intruder':
                return Clearance.UNAUTHORIZED
        }

        null
    }
}
