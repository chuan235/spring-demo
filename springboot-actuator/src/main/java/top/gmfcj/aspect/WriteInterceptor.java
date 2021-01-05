package top.gmfcj.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import top.gmfcj.datasource.DynamicDataSourceContextHolder;

@Aspect
@Component
public class WriteInterceptor implements Ordered {

    private static final Logger log = LoggerFactory.getLogger(WriteInterceptor.class);

    @Pointcut("@annotation(top.gmfcj.anno.Write)")
    public void WritePointCut() {

    }

    @Around("WritePointCut()")
    public Object setRead(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
             DynamicDataSourceContextHolder.master();
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSoureType();
            log.info("清除threadLocal");
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
