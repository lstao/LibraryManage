package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)//ע��ֻ������ʱ����Ч
public @interface Clazs {
	public String value();
}
