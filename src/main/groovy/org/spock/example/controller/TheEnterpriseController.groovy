package org.spock.example.controller

import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j
import org.spock.example.TheEnterprise
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE
import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * A controller for accessing {@link org.spock.example.TheEnterprise} functionality via a web endpoint.
 */
@Controller
@Slf4j
class TheEnterpriseController {
    @Autowired
    private TheEnterprise theShip

    @RequestMapping(value = 'officers', method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    String trainOfficers(@RequestParam("number") int numberOfOfficers) {
        def officers = theShip.trainOfficers(numberOfOfficers)

        log.info("Requested $numberOfOfficers officers")
        return new JsonBuilder(officers).toPrettyString()
    }
}
