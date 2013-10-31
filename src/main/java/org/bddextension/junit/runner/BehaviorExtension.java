/*
 * Copyright 2013 Balazs Berkes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bddextension.junit.runner;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
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
public class BehaviorExtension extends Runner {

    private BlockJUnit4ClassRunner runner;

    public BehaviorExtension(Class<?> testClass) throws InitializationError {
        runner = new BDDJUnit4ClassRunner(testClass);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        runner.run(notifier);
    }

    private class BDDJUnit4ClassRunner extends BlockJUnit4ClassRunner {

        private final List<FrameworkMethod> allContexts;

        private BDDJUnit4ClassRunner(Class<?> testClass) throws InitializationError {
            super(testClass);
            allContexts = getTestClass().getAnnotatedMethods(Context.class);
        }

        @Override
        protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
            Statement withContext = new ContextCollector(allContexts).getContext(method, target, statement);
            return super.withBefores(method, target, withContext);
        }

    }

    private class ContextCollector {

        private final List<FrameworkMethod> availableContexts;
        private List<FrameworkMethod> givenContexts = Collections.emptyList();

        private ContextCollector(List<FrameworkMethod> availableContexts) {
            this.availableContexts = availableContexts;
        }

        private Statement getContext(FrameworkMethod method, Object target, Statement statement) {
            Given given = method.getAnnotation(Given.class);
            if (isAnnotationPresented(given)) {
                collectAssociatedContexts(given);
            }
            return givenContexts.isEmpty() ? statement : new RunBefores(statement, givenContexts, target);
        }

        private boolean isAnnotationPresented(Given contextNames) {
            return contextNames != null;
        }

        private void collectAssociatedContexts(Given given) {
            List<String> associatedContexts = Arrays.asList(given.value());
            if (!associatedContexts.isEmpty()) {
                addAssociatedContexts(associatedContexts);
            }
        }

        private void addAssociatedContexts(List<String> givens) {
            givenContexts = new LinkedList<FrameworkMethod>();
            for (String given : givens) {
                findAndAttachContext(given);
            }
        }

        private void findAndAttachContext(String given) {
            for (FrameworkMethod m : availableContexts) {
                Context annotation = m.getAnnotation(Context.class);
                if (annotation.value().equals(given)) {
                    givenContexts.add(m);
                }
            }
        }
    }
}
