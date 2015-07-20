import groovy.util.logging.Log

/**
 * A great starship.
 */
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
        assert !OFFICER_MCCOY.equals(officer.name):
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
            trainees << new Officer("${ -> newOfficerTitle} $i", newOfficerTitle)
        }

        trainees
    }
}
