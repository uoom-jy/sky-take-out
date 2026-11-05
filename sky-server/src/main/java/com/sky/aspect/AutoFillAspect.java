package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/*
自定义切面类，用于实现公共字段的自动填充功能
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    //切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    //直接使用注解标识的切入点会扫描全包影响效率，这里搭配使用execution(* com.sky.mapper.*.*(..))来限制切入点的范围提升效率
    public void autoFillPointCut() {}

    //通知
    //前置通知，用于在方法执行前进行自动填充
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("执行了公共字段的自动填充功能");
        //获取当前拦截方法的操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationType operationType = signature.getMethod().getAnnotation(AutoFill.class).value();
        //获取当前拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前操作类型，利用反射进行赋值
        if (operationType == OperationType.INSERT) {
            //插入
            try {
                //获取entity类类型
                Class<?> entityClass = entity.getClass();
                //获取entity类的setCreateTime方法
                Method setCreateTimeMethod = entityClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                //调用setCreateTime方法
                setCreateTimeMethod.invoke(entity, now);
                //获取entity类的setCreateUser方法
                Method setCreateUserMethod = entityClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                //调用setCreateUser方法
                setCreateUserMethod.invoke(entity, currentId);
                //获取entity类的setUpdateTime方法
                Method setUpdateTimeMethod = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                //调用setUpdateTime方法
                setUpdateTimeMethod.invoke(entity, now);
                //获取entity类的setUpdateUser方法
                Method setUpdateUserMethod = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //调用setUpdateUser方法
                setUpdateUserMethod.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("自动填充失败", e);
            }
        }
        if (operationType == OperationType.UPDATE) {
            //更新
            try {
                //获取entity类类型
                Class<?> entityClass = entity.getClass();
                //获取entity类的setUpdateTime方法
                Method setUpdateTimeMethod = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                //调用setUpdateTime方法
                setUpdateTimeMethod.invoke(entity, now);
                //获取entity类的setUpdateUser方法
                Method setUpdateUserMethod = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //调用setUpdateUser方法
                setUpdateUserMethod.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("自动填充失败", e);
            }
        }

    }


}
