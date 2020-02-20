package io.atlassian.springboot.logging.mvc

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(MDCLoggerAspect::class)
open class LoggingMvcAutoconfig {

    @Bean
    open fun mdcLoggerAspect(): MDCLoggerAspect {
        return MDCLoggerAspect()
    }
}
