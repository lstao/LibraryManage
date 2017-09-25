package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)//注解只在运行时期有效
public @interface Clazs {
	public String value();
}
