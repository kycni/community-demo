package com.song.community.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Kycni
 * @date 2022/2/26 22:57
 */
@Component
@Aspect
public class AlphaAspect {
    
    @Pointcut("execution(* com.song.community.service.*.*(..))")
    public void pointCut() {
            
    }
}
