package org.spock.example

import org.spock.example.beans.Clearance
import org.spock.example.provider.ClearanceProvider
import org.spock.example.beans.Officer
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class TheEnterpriseSpec extends Specification {

    private static final String TEST_TRAINEE_TITLE = 'Test Trainee'
    private static final String THE_ENTERPRISE_IS_DOCKED = 'enterprise.docked'
    static {
        System.setProperty(THE_ENTERPRISE_IS_DOCKED, 'true')
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
        officerKirk = new Officer('Kirk', 'Captain')
        officerSpock = new Officer('Spock', 'First Officer')
        officerMccoy = new Officer('McCoy', 'Lieutenant Commander')
    }

    def setup() {
        ourShip = new TheEnterprise(TEST_TRAINEE_TITLE, testProvider)
    }

    @Unroll
    def '#title #name has proper access to the Enterprise'() {
        setup: 'a parameterized Officer'
        def officer = new Officer(name, title)
        (officer.name == 'Spock' ? 0 : 1) * testProvider.getClearance(title) >> testClearanceBehavior(title)

        when: 'get clearance for this officer'
        def result = ourShip.clearanceToSystems(officer)

        then: 'the clearance level is correct'
        result == expectedResult

        where:
        name              | title              || expectedResult
        officerKirk.name  | officerKirk.title  || Clearance.UNRESTRICTED
        officerSpock.name | 'Intruder'         || Clearance.UNRESTRICTED
        officerSpock.name | officerSpock.title || Clearance.UNRESTRICTED
        officerKirk.name  | 'Intruder'         || Clearance.UNAUTHORIZED
        'Scotty'          | 'First Officer'    || Clearance.LIMITED
        'Nyota'           | 'Lieutenant'       || null
    }

    def 'Officer McCoy has a special greeting'() {
        given: 'officer McCoy'
        def mccoy = officerMccoy

        when: 'McCoy requests clearance'
        ourShip.clearanceToSystems(mccoy)

        then: 'He gets an exception with a special greeting'
        AssertionError greeting = thrown()
        greeting.message.startsWith('''\
Really, Dr. McCoy. You must learn to govern your passions; they will be your undoing.
Logic suggests...'''
        )
    }

    @IgnoreIf({ System.getProperty(THE_ENTERPRISE_IS_DOCKED) == 'false' })
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
        assert officer.name == "$TEST_TRAINEE_TITLE $i".toString()
        assert officer.title == TEST_TRAINEE_TITLE
    }

    def testClearanceBehavior(String officerTitle) {
        switch (officerTitle)
        {
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