import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static Clearance.*

class TheEnterpriseSpec extends Specification
{

    private static final String TEST_TRAINEE_TITLE = "Test Trainee"
    private static final String THE_ENTERPRISE_IS_DOCKED = "enterprise.docked"
    static {
        System.setProperty(THE_ENTERPRISE_IS_DOCKED, "false")
    }

    @Shared
    private Officer officerKirk
    @Shared
    private Officer officerSpock
    @Shared
    private Officer officerMccoy

    private TheEnterprise ourShip
    private ClearanceProvider testProvider = Mock()

    void setupSpec() {
        officerKirk = new Officer("Kirk", "Captain")
        officerSpock = new Officer("Spock", "First Officer")
        officerMccoy = new Officer("McCoy", "Lieutenant Commander")
    }

    void setup() {
        ourShip = new TheEnterprise(TEST_TRAINEE_TITLE, testProvider)
    }

    @Unroll
    void '#title #name has proper access to the Enterprise'() {
        setup: 'a parameterized Officer'
        Officer officer = new Officer(name, title)
        (officer.getName().equals("Spock") ? 0 : 1) * testProvider.getClearance(title) >> testClearanceBehavior(title)


        when: 'get clearance for this officer'
        Clearance result = ourShip.clearanceToSystems(officer)

        then: 'the clearance level is correct'
        result == expectedResult

        where:
        name                   | title                   || expectedResult
        officerKirk.getName()  | officerKirk.getTitle()  || UNRESTRICTED
        officerSpock.getName() | "Intruder"              || UNRESTRICTED
        officerSpock.getName() | officerSpock.getTitle() || UNRESTRICTED
        officerKirk.getName()  | "Intruder"              || UNAUTHORIZED
        "Scotty"               | "First Officer"         || LIMITED
        "Nyota"                | "Lieutenant"            || null
    }

    void 'Officer McCoy has a special greeting'() {
        given: 'officer McCoy'
        Officer mccoy = officerMccoy

        when: 'McCoy requests clearance'
        ourShip.clearanceToSystems(mccoy)

        then: 'He gets an exception with a special greeting'
        AssertionError greeting = thrown()
        println greeting.message
        greeting.getMessage().startsWith("Really, Dr. McCoy. You must learn to govern your passions; they will be your undoing. Logic suggests...")
    }

    @IgnoreIf({ System.getProperty(THE_ENTERPRISE_IS_DOCKED).equals("false") })
    void 'officers are trained at the enterprise'() {
        given: 'we have a number of men to train'
        int numberOfMenToTrain = 3

        when: 'we train these men'
        List<Officer> officers = ourShip.trainOfficers(numberOfMenToTrain)

        then: 'we receive proper officers with proper titles and names'
        officers.size() == numberOfMenToTrain
        for (int i = 0; i < numberOfMenToTrain; i++) {
            verifyNewOfficer(i, officers)
        }

        when: 'we have no men to train'
        List<Officer> noOfficers = ourShip.trainOfficers(0)

        then: 'we receive no officers'
        noOfficers.isEmpty()
    }

    private void verifyNewOfficer(int i, List<Officer> officers) {
        Officer toCheck = officers.get(i)
        assert toCheck.getName().equals(TEST_TRAINEE_TITLE + " " + i)
        assert toCheck.getTitle().equals(TEST_TRAINEE_TITLE)
    }

    Clearance testClearanceBehavior(String officerTitle) {
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

        return null
    }
}
