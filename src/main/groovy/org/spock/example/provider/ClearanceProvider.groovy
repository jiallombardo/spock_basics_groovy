package org.spock.example.provider

import org.spock.example.beans.Clearance

/**
 * A contract for providing clearance.
 */
interface ClearanceProvider {

    Clearance getClearance(String title)
}
