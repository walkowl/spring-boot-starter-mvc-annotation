# Spring Boot MVC annotation starter #

This Spring Boot (2.x.x) starter provides annotation @LogWithMDC which can be used for marking Spring Bean method's fields 
and automatically puts them into MDC context at the beginning of the method and removes at the end.

# Installation #

## Maven ##
```xml
<dependency>
    <groupId>com.atlassian.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mdc-logging-annotation</artifactId>
    <version>INSERT VERSION HERE</version>
</dependency>
```

# Usage #

Just mark field(s) of a public method (please read Caveats below) with annotation `@LogWithMDC` and that's it!
From now on, all the logs added in that method (and deeper) will contain MDC tag with that field's name and its `toString` value.

## Example ##

```java

public Page<Team> getAllTeams(@LogWithMDC CloudId cloudId, PaginationParams paginationParams, ConsistencyCheck consistencyCheck) {
    log.info("some message");
}   
```

All log entries within getAllTeam method context will contain MDC field `cloudId` with its value.

# Caveats #

## AspectJ ##

This starter uses [AspectJ AOP](https://www.eclipse.org/aspectj/). It uses load-time weaving and should 
have minimal / close to none performance impact. However, please consider if it doesn't interfere with other
elements of your service.

## It works for Spring Bean's calls only ##

Since AOP aspects work only for calls going through the AspectJ proxy, not all calls can be intercepted.

[Understanding AOP Proxies](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aop-understanding-aop-proxies)

Example of not working pointcut:

```java
public void method1(String arg) {
   method2(arg);
}


public void method2(@LogWithMDC String arg) {
}
```

The aspect is applied to a proxy surrounding the bean. 
In above example `method1` is calling directly `method2` (not through proxy), whereas if that class instance is injected into another as a Spring bean, it's injected as its proxy, and hence method calls will be invoked on the proxy (and the aspects will be triggered).

If you can't mark the fields on the Spring Bean level, you can use below workaround:

```java
@Component
public class Foo {
    @Autowired
    private Foo foo;

    public void method1(){
        ..
        foo.method2();
        ..
    }
    public void method2(){
        ..
    }
}
```

## Logging complex objects and PII

Please be aware, that evaluation of the tag's value is made by calling `toString` method of the annotated method 
and currently there's no way to filter out not wanted fields. 

