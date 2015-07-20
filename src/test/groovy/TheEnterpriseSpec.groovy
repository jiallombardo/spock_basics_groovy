import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static Clearance.*

class TheEnterpriseSpec extends Specification {

    private static final String TEST_TRAINEE_TITLE = "Test Trainee"
    private static final String THE_ENTERPRISE_IS_DOCKED = "enterprise.docked"
    static {
        System.setProperty(THE_ENTERPRISE_IS_DOCKED, "true")
    }

    @Shared
    private Officer officerKirk
    @Shared
    private Officer officerSpock
    @Shared
    private Officer officerMccoy

    private TheEnterprise ourShip
    private def testProvider = Mock(ClearanceProvider)

    def setupSpec() {
        officerKirk = new Officer("Kirk", "Captain")
        officerSpock = new Officer("Spock", "First Officer")
        officerMccoy = new Officer("McCoy", "Lieutenant Commander")
    }

    def setup() {
        ourShip = new TheEnterprise(TEST_TRAINEE_TITLE, testProvider)
    }

    @Unroll
    def '#title #name has proper access to the Enterprise'() {
        setup: 'a parameterized Officer'
        def officer = new Officer(name, title)
        (officer.name.equals("Spock") ? 0 : 1) * testProvider.getClearance(title) >> testClearanceBehavior(title)

        when: 'get clearance for this officer'
        def result = ourShip.clearanceToSystems(officer)

        then: 'the clearance level is correct'
        result == expectedResult

        where:
        name              | title              || expectedResult
        officerKirk.name  | officerKirk.title  || UNRESTRICTED
        officerSpock.name | "Intruder"         || UNRESTRICTED
        officerSpock.name | officerSpock.title || UNRESTRICTED
        officerKirk.name  | "Intruder"         || UNAUTHORIZED
        "Scotty"          | "First Officer"    || LIMITED
        "Nyota"           | "Lieutenant"       || null
    }

    def 'Officer McCoy has a special greeting'() {
        given: 'officer McCoy'
        def mccoy = officerMccoy

        when: 'McCoy requests clearance'
        ourShip.clearanceToSystems(mccoy)

        then: 'He gets an exception with a special greeting'
        AssertionError greeting = thrown()
        greeting.message.startsWith("Really, Dr. McCoy. You must learn to govern your passions; they will be your undoing. Logic suggests...")
    }

    @IgnoreIf({ System.getProperty(THE_ENTERPRISE_IS_DOCKED).equals("false") })
    def 'officers are trained at the enterprise'() {
        given: 'we have a number of men to train'
        int numberOfMenToTrain = 3

        when: 'we train these men'
        def officers = ourShip.trainOfficers(numberOfMenToTrain)

        then: 'we receive proper officers with proper titles and names'
        officers.size() == numberOfMenToTrain
        officers.eachWithIndex { entry, index -> verifyNewOfficer(index, entry)}

        when: 'we have no men to train'
        def noOfficers = ourShip.trainOfficers(0)

        then: 'we receive no officers'
        noOfficers.isEmpty()
    }

    private def verifyNewOfficer(int i, Officer officer) {
        assert officer.name.equals(TEST_TRAINEE_TITLE + " " + i)
        assert officer.title.equals(TEST_TRAINEE_TITLE)
    }

    def testClearanceBehavior(String officerTitle) {
        switch (officerTitle)
        {
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