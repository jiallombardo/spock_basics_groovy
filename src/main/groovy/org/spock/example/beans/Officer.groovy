package org.spock.example.beans

import groovy.transform.Immutable

/**
 * A possible member of a starship's crew. Officers are immutable - their title
 * doesn't change, as we are not considering long periods of service right now.
 */
@Immutable
class Officer {
    String name
    String title
}
