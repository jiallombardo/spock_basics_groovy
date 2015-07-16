/**
 * A possible member of a starship's crew. Officers are immutable - their title
 * doesn't change, as we are not considering long periods of service right now.
 */
class Officer {
    private String name
    private String title

    Officer(String name, String title) {
        this.name = name
        this.title = title
    }

    String getName() {
        return name
    }

    String getTitle() {
        return title
    }
}
