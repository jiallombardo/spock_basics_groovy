import groovy.transform.PackageScope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TheEnterpriseConfiguration
{

    /**
     * A context {@link TheEnterprise} bean with a Proxy {@link ClearanceProvider} implementation.
     * We avoid writing the implementation of {@link ClearanceProvider}.
     */
    @PackageScope
    @Bean
    TheEnterprise theShip() {
        new TheEnterprise("trainee", [ getClearance : { title -> 'UNAUTHORIZED' as Clearance } ] as ClearanceProvider)
    }
}
