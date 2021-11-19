package com.team13.junction.config

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
@Aspect
class MethodLoggingAspect(props: MethodLoggingProps) {

    private val objectSizeLimit = props.objectSizeLimit
    private val collectionSizeLimit = props.collectionSizeLimit

    @Pointcut("@annotation(com.team13.junction.config.EnableLogging)")
    fun hasAnnotation() = Unit

    @Pointcut("@within(com.team13.junction.config.EnableLogging)")
    fun classHasAnnotation() = Unit

    @Around("hasAnnotation() || classHasAnnotation()")
    fun logMethodCall(joinPoint: ProceedingJoinPoint): Any? {
        val annotation = getAnnotation(joinPoint)
        val methodWithArgs = getMethodWithArgs(joinPoint)

        if (annotation.logEntry) {
            logger.info("-> $methodWithArgs")
        }

        val (result, duration) = measureTimedValue { joinPoint.proceed() }

        if (annotation.logReturn) {
            logger.info("<- $methodWithArgs in $duration${createReturnMessage(result, joinPoint)}")
        }

        return result
    }

    private fun createReturnMessage(result: Any?, joinPoint: ProceedingJoinPoint): String =
        if (!isUnitMethod(joinPoint)) " returned '${objToString(result)}'" else ""

    private fun isUnitMethod(joinPoint: ProceedingJoinPoint): Boolean =
        (joinPoint
            .signature as? MethodSignature)
            ?.returnType == Void.TYPE

    private fun getMethodWithArgs(joinPoint: ProceedingJoinPoint): String {
        val methodName = createMethodName(joinPoint)
        val args = getArgs(joinPoint)
        return "$methodName($args)"
    }

    private fun createMethodName(joinPoint: ProceedingJoinPoint) =
        "${joinPoint.signature.declaringType.simpleName}.${joinPoint.signature.name}"

    private fun getArgs(joinPoint: ProceedingJoinPoint): String =
        joinPoint.args.joinToString { objToString(it) }

    private fun getAnnotation(joinPoint: ProceedingJoinPoint): EnableLogging {
        val methodAnnotation = if (joinPoint.signature is MethodSignature) {
            (joinPoint.signature as MethodSignature)
                .method
                .getAnnotation(EnableLogging::class.java)
        } else {
            null
        }

        val classAnnotation =
            joinPoint.signature.declaringType.getAnnotation<EnableLogging>(EnableLogging::class.java) as? EnableLogging

        return methodAnnotation
            ?: classAnnotation
            ?: error("No annotation found")
    }

    private fun objToString(obj: Any?): String =
        when (obj) {
            null -> "null"
            logger.isDebugEnabled -> obj.toString()
            is Collection<*> -> {
                val sizeString = "${obj.size} elements"
                if (obj.size > collectionSizeLimit) {
                    sizeString
                } else "$sizeString: ${printString(obj.toString())}"
            }
            else -> printString(obj.toString())
        }

    private fun printString(input: String): String =
        if (input.length > objectSizeLimit) {
            "${input.take(objectSizeLimit)}..."
        } else input

    init {
        logger.info("Aspect logging loaded")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MethodLoggingAspect::class.java)
    }
}
