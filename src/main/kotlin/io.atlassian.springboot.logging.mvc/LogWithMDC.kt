package io.atlassian.springboot.logging.mvc

/**
 * Annotation used to mark method parameters which should be included into MDC context and cleared after method invocation.
 */

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogWithMDC
