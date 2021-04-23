package top.pippen.xncms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.hswebframework.web.authorization.basic.configuration.EnableAopAuthorize;
import org.hswebframework.web.crud.annotation.EnableEasyormRepository;
import org.hswebframework.web.logging.aop.EnableAccessLogger;
import org.springdoc.webflux.core.SpringDocWebFluxConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @author pippen
 */
@EnableAccessLogger
@EnableAopAuthorize
@SpringBootApplication
@EnableEasyormRepository("top.pippen.xncms.**.entity")
public class XnCMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(XnCMSApplication.class, args);
    }

    @Configuration(proxyBeanMethods = false)
    @OpenAPIDefinition(
            info = @Info(
                    title = "XN-CMS",
                    description = "XN-CMS 内容管理",
                    contact = @Contact(name = "pippen"),
                    version = "1.0.0-SNAPSHOT"
            )
    )
    @AutoConfigureBefore(SpringDocWebFluxConfiguration.class)
    static class SwaggerConfiguration {

    }
}
