/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bddextension.junit.runner;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import org.bddextension.junit.Context;
import org.bddextension.junit.Given;

/**
 * Extension of {@link BlockJUnit4ClassRunner} which supports {@link Given @Given} and {@link Context @Context}
 * annotations.
 * <p>
 * @author Balazs Berkes
 */
public class BehaviorExtension extends BlockJUnit4ClassRunner {

    public BehaviorExtension(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Object test;
        try {
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return createTest();
                }
            }.run();
        } catch (Throwable e) {
            return new Fail(e);
        }

        Statement statement = methodInvoker(method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withContext(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = withAfters(method, test, statement);
        statement = withRules(method, test, statement);
        return statement;
    }

    private Statement withContext(FrameworkMethod method, Object target, Statement statement) {

        List<FrameworkMethod> givenContexts = Collections.emptyList();
        Given contextNames = method.getAnnotation(Given.class);
        if (contextNames != null) {
            givenContexts = collectContexts(contextNames, givenContexts);
        }
        return givenContexts.isEmpty() ? statement : new RunBefores(statement, givenContexts, target);
    }

    private List<FrameworkMethod> collectContexts(Given contextNames, List<FrameworkMethod> givenContexts) {
        List<String> givens = Arrays.asList(contextNames.value());
        if (!givens.isEmpty()) {
            givenContexts = buildGivenContexts(givens);
        }
        return givenContexts;
    }

    private List<FrameworkMethod> buildGivenContexts(List<String> givens) {
        List<FrameworkMethod> givenContexts = new LinkedList<FrameworkMethod>();
        List<FrameworkMethod> allContexts = getTestClass().getAnnotatedMethods(Context.class);
        for (String given : givens) {
            findAndAttachContext(allContexts, given, givenContexts);

        }
        return givenContexts;
    }

    private void findAndAttachContext(List<FrameworkMethod> allContexts, String given, List<FrameworkMethod> givenContexts) {
        for (FrameworkMethod m : allContexts) {
            Context annotation = m.getAnnotation(Context.class);
            if (annotation.value().equals(given)) {
                givenContexts.add(m);
            }
        }
    }

    private Statement withRules(FrameworkMethod method, Object target,
            Statement statement) {
        List<TestRule> testRules = getTestRules(target);
        Statement result = statement;
        result = withMethodRules(method, testRules, target, result);
        result = withTestRules(method, testRules, result);

        return result;
    }

    private Statement withMethodRules(FrameworkMethod method, List<TestRule> testRules,
            Object target, Statement result) {
        for (org.junit.rules.MethodRule each : getMethodRules(target)) {
            if (!testRules.contains(each)) {
                result = each.apply(result, method, target);
            }
        }
        return result;
    }

    private List<org.junit.rules.MethodRule> getMethodRules(Object target) {
        return rules(target);
    }

    /**
     * @param target the test case instance
     * @return a list of MethodRules that should be applied when executing this
     * test
     */
    @Override
    protected List<org.junit.rules.MethodRule> rules(Object target) {
        return getTestClass().getAnnotatedFieldValues(target, Rule.class,
                org.junit.rules.MethodRule.class);
    }

    /**
     * Returns a {@link Statement}: apply all non-static {@link Value} fields
     * annotated with {@link Rule}.
     *
     * @param statement The base statement
     * @return a RunRules statement if any class-level {@link Rule}s are
     * found, or the base statement
     */
    private Statement withTestRules(FrameworkMethod method, List<TestRule> testRules,
            Statement statement) {
        return testRules.isEmpty() ? statement
                : new RunRules(statement, testRules, describeChild(method));
    }

    /**
     * @param target the test case instance
     * @return a list of TestRules that should be applied when executing this
     * test
     */
    @Override
    protected List<TestRule> getTestRules(Object target) {
        List<TestRule> result = getTestClass().getAnnotatedMethodValues(target,
                Rule.class, TestRule.class);
        result.addAll(getTestClass().getAnnotatedFieldValues(target,
                Rule.class, TestRule.class));

        return result;
    }
}
