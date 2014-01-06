/*
 * Copyright 2014 Balazs Berkes.
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
package org.bddextension.junit;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import org.bddextension.junit.annotations.Context;
import org.bddextension.junit.annotations.Given;

/**
 * Collects the methods into a {@link Statement} which belongs to the current {@code @Test} method.
 * <p>
 * @author Balazs Berkes
 */
class ContextCollector {

    private final List<FrameworkMethod> availableContexts;
    private List<FrameworkMethod> givenContexts = Collections.emptyList();

    protected ContextCollector(List<FrameworkMethod> availableContexts) {
        this.availableContexts = availableContexts;
    }

    protected Statement getContext(FrameworkMethod method, Object target, Statement statement) {
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
