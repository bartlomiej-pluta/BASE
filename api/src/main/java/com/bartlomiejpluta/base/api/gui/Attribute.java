package com.bartlomiejpluta.base.api.gui;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {
   String value();
   String separator() default "|";
}
