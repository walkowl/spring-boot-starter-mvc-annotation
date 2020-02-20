package io.atlassian.springboot.logging.mvc

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
class MDCLoggerAspect {

    @Around("execution(* *(.., @LogWithMDC (*), ..))")
    fun logWithMDC(joinPoint: ProceedingJoinPoint): Any? {
        return setMdc(joinPoint)
    }

    private fun setMdc(joinPoint: ProceedingJoinPoint): Any? {
        val selectedArgumentWithValues =
            getAnnotatedMethodArguments(joinPoint.signature as MethodSignature, joinPoint.args)
        val tagsCurrentlyNotPresentInMDC = selectedArgumentWithValues.filter { MDC.get(it.first) == null }
        tagsCurrentlyNotPresentInMDC.forEach { MDC.put(it.first, it.second) }
        val obj = joinPoint.proceed()
        tagsCurrentlyNotPresentInMDC.forEach { MDC.remove(it.first) }
        return obj
    }

    private fun getAnnotatedMethodArguments(
        signature: MethodSignature,
        values: Array<Any>
    ): List<Pair<String, String>> {
        return signature.parameterNames
            .zip(values)
            .zip(signature.method.parameterAnnotations, createMethodParameter)
            .filter { isAnnotatedWithMDC(it.annotations) }
            .map { Pair(it.name, it.value) }
    }

    internal val createMethodParameter: (Pair<String, Any>, Array<Annotation>) -> MethodParameter =
        { nameValue, annotations -> MethodParameter(nameValue.first, nameValue.second.toString(), annotations) }

    private fun isAnnotatedWithMDC(it: Array<Annotation>) =
        it.map { it.annotationClass.javaObjectType == LogWithMDC::class.java }.any { it }

    internal data class MethodParameter(val name: String, val value: String, val annotations: Array<Annotation>)
}
