/**
 * A great starship.
 */
class TheEnterprise {

    private static final String OUR_HERO_SPOCK = "Spock"
    private static final String OFFICER_MCCOY = "McCoy"

    private String newOfficerTitle
    private ClearanceProvider clearanceProvider

    TheEnterprise(String newOfficerTitle, ClearanceProvider clearanceProvider) {
        this.newOfficerTitle = newOfficerTitle
        this.clearanceProvider = clearanceProvider
    }

    Clearance clearanceToSystems(Officer officer) {
        if (OUR_HERO_SPOCK.equals(officer.getName())) {
            return Clearance.UNRESTRICTED
        }
        assert !OFFICER_MCCOY.equals(officer.getName()): "Really, Dr. McCoy. You must learn to govern your passions; they will be your undoing. Logic suggests..."

        clearanceProvider.getClearance(officer.getTitle())
    }

    List<Officer> trainOfficers(int numberOfOfficers) {
        if (numberOfOfficers < 0)
            return Collections.emptyList()
        def trainees = new LinkedList<Officer>()

        for (int i = 0; i < numberOfOfficers; i++) {
            def officer = new Officer(newOfficerTitle + " " + i, newOfficerTitle)
            trainees.add(officer)
        }

        trainees
    }
}
