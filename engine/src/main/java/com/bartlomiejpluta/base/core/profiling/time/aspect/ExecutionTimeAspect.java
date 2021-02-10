package com.bartlomiejpluta.base.core.profiling.time.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@ConditionalOnExpression("${app.profiling.aspects:false}")
public class ExecutionTimeAspect {

   @Around("@annotation(com.bartlomiejpluta.base.core.stat.metrics.annotation.MeasureExecutionTime)")
   public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
      long start = 0;
      long end = 0;
      long elapsed;
      Object result;
      try {
         start = System.nanoTime();
         result = joinPoint.proceed();
      } finally {
         end = System.nanoTime();
      }

      elapsed = end - start;
      log.debug("[{}.{}] = [{}s] [{}ms] [{}ns]",
              joinPoint.getTarget().getClass().getSimpleName(),
              joinPoint.getSignature().getName(),
              elapsed / 1000000.0, elapsed / 1000.0, elapsed
      );

      return result;
   }
}
