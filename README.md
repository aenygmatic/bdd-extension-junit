bdd-extension-junit
===================

JUnit 4.11 extension which adds custom and setup steps for each ```@Test``` method.


```@Context```  methods annotated with this can be called before @Test methods by mentioning the context name in the parameter list of @Given annotation
```@Given``` this annotation should be used along with JUnit's ```@Test``` annotation. When this annotation is presented the mentioned @Context methods will be called in the same order as they were mentioned.


Example codes:
```java
public class TestClass extends BehaviorExtensionAwareTest {
    ... test content ...
}
```
```java
@RunWith(BehaviorExtensionRunner.class)
public class TestClass {
    ... test content ...
}
```
```java
public class TestClass {
    @Rule
    public BehavoirExtensionRule behavoirExtensionRule = new BehavoirExtensionRule();
    ... test content ...
}
```

```java

    @Context("john as employee")
    public void givenNewEmployee() {
        employee = "";
    }

    @Context("today is friday")
    public void givenFriday() {
        day = Days.FRIDAY;
    }

    @Test
    @Given("john as employee")
    public void testNewEmployee() {
        ... test method ...
    }

    @Test
    @Given({"john as employee", "today is friday"})
    public void testNewEmployeeOnFriday() {
        ... test method ...
    }
```
