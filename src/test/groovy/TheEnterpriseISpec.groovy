import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification
import spock.lang.Unroll

import static Clearance.*

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
            (officer.getName().equals("Spock") ? 0 : 1) * testProvider.getClearance(title) >> testClearanceBehavior(title)


        when: 'get clearance for this officer'
            def result = testShip.clearanceToSystems(officer)

        then: 'the clearance level is correct'
            result == expectedResult

        where:
            name     | title           || expectedResult
            "Kirk"   | "Captain"       || UNRESTRICTED
            "Spock"  | "Intruder"      || UNRESTRICTED
            "Spock"  | "First Officer" || UNRESTRICTED
            "Kirk"   | "Intruder"      || UNAUTHORIZED
            "Scotty" | "First Officer" || LIMITED
            "Nyota"  | "Lieutenant"    || null
    }

    def testClearanceBehavior(String officerTitle) {
        switch (officerTitle) {
            case "Captain":
                return UNRESTRICTED

            case "First Officer":
                return LIMITED

            case "LieutenantCommander":
                return LIMITED

            case "Intruder":
                return UNAUTHORIZED
        }

        null
    }
}
