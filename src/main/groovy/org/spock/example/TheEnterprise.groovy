package org.spock.example

import groovy.util.logging.Slf4j
import org.spock.example.beans.Clearance
import org.spock.example.provider.ClearanceProvider
import org.spock.example.beans.Officer

/**
 * A great starship.
 */
@Slf4j
class TheEnterprise {

    private static final String OUR_HERO_SPOCK = 'Spock'
    private static final String OFFICER_MCCOY = 'McCoy'

    private String newOfficerTitle
    private ClearanceProvider clearanceProvider

    TheEnterprise(String newOfficerTitle, ClearanceProvider clearanceProvider) {
        this.newOfficerTitle = newOfficerTitle
        this.clearanceProvider = clearanceProvider
    }

    Clearance clearanceToSystems(Officer officer) {
        if (OUR_HERO_SPOCK == officer.name) {
            return Clearance.UNRESTRICTED
        }
        assert OFFICER_MCCOY != officer.name :
'''\
Really, Dr. McCoy. You must learn to govern your passions; they will be your undoing.
Logic suggests...
'''

        clearanceProvider.getClearance(officer.title)
    }

    List<Officer> trainOfficers(int numberOfOfficers) {
        if (numberOfOfficers < 0)
            return []
        def trainees = []

        for (i in (0..<numberOfOfficers)) {
            def officer = new Officer("${ -> newOfficerTitle} $i", newOfficerTitle)
            trainees << officer
            log.info("Adding trainee with title $officer.title and name $officer.name")
        }

        trainees
    }
}
