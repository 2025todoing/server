package hongik.Todoing.global.throttle;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Throttle {
    int seconds() default 1;
}
