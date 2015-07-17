import groovy.transform.PackageScope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

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
        def handler = new InvocationHandler() {
            @Override
            Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                null
            }
        }
        def proxyProvider = Proxy.newProxyInstance(ClearanceProvider.classLoader,
                [ClearanceProvider] as Class[],
                handler)

        new TheEnterprise("trainee", proxyProvider)
    }
}
