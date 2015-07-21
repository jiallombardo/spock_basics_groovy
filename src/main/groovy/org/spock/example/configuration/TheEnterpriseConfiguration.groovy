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
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

import java.nio.charset.Charset

import static org.springframework.http.MediaType.APPLICATION_JSON

@Configuration
@ComponentScan(basePackages = 'org.spock.example.controller')
@EnableWebMvc
@SpringBootApplication(exclude = [DataSourceAutoConfiguration, HibernateJpaAutoConfiguration,
    DataSourceTransactionManagerAutoConfiguration])
class TheEnterpriseConfiguration extends WebMvcConfigurerAdapter
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

    @Override
    void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        stringConverter.setSupportedMediaTypes(Arrays.asList(APPLICATION_JSON));
        converters.add(stringConverter);
    }
}
