package be.cegeka.android_mvc.binder;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static be.cegeka.android_mvc.binder.Visibility.NULL;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Bind {

    int value();

    Visibility visibilityToUse() default NULL;

}
