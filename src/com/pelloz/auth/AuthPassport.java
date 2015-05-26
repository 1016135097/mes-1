package com.pelloz.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Documented
@Inherited //注解能够被继承
@Target(ElementType.METHOD) //注解能修饰类中的Method
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
public @interface AuthPassport {

	boolean validate() default true;
	String[] department();
	
	
	
}
