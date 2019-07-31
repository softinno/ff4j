package org.ff4j.aop;

public class GreetingService {

    @ToggledFeature // valid but do nothinh
    public void done() {}
    
    @ToggledFeature("sayHello") // valid but only usage tracking
    public void doSomething() {}
    
    // Default behaviour
    @ToggledFeature(feature = "french", methodName="greetingsFR")
    public String greetings(String people) {
        return "Hello " + people;
    }
    
    // You can also provide name of external classes
    @ToggledFeature(feature = "french", methodName="greetingsFR", className = GreetingService.class) 
    public String extraClassSample(String people) {
        return "Hello " + people;
    }
    
    public String greetingsFR(String people) {
        return "Bonjour " + people;
    }

}


