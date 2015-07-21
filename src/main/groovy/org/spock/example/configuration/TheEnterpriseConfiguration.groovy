package org.spock.example.configuration

import groovy.transform.PackageScope
import org.spock.example.TheEnterprise
import org.spock.example.beans.Clearance
import org.spock.example.provider.ClearanceProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = 'org.spock.example.controller')
@SpringBootApplication(exclude = [DataSourceAutoConfiguration, HibernateJpaAutoConfiguration,
    DataSourceTransactionManagerAutoConfiguration])
class TheEnterpriseConfiguration
{

    /**
     * A context {@link org.spock.example.TheEnterprise} bean with a Proxy {@link org.spock.example.provider.ClearanceProvider} implementation.
     * We avoid writing the implementation of {@link org.spock.example.provider.ClearanceProvider}.
     */
    @PackageScope
    @Bean
    TheEnterprise theShip() {
        new TheEnterprise('trainee', [ getClearance : { title -> 'UNAUTHORIZED' as Clearance } ] as ClearanceProvider)
    }
}
