package viper.gobra.tags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// We define a custom tag to selectively enable / disable GobraTests from SBT.
// According to the documentation of org.scalatest.Tag, this annotation must be defined in Java
// as Scala annotations are not available at runtime, which is confirmed by our own experiments.

@org.scalatest.TagAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface GobraTestsTag {}
