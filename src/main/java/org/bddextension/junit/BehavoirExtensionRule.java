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
package org.bddextension.junit;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * JUnit {@link TestRule} which supports {@link Given @Given} and {@link Context @Context}
 * annotations.
 * <p>
 * Methods annotated with {@code @Context} will run before both {@code @Before} and {@code @Test} method.
 * <p>
 * @author Balazs Berkes
 */
public class BehavoirExtensionRule implements MethodRule {

    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new ContextCollector(findContexts(target)).getContext(method, target, base);
    }

    private List<FrameworkMethod> findContexts(Object target) {
        List<FrameworkMethod> contextMethods = new LinkedList<FrameworkMethod>();

        for (Method method : target.getClass().getMethods()) {
            if (notParametric(method) && method.isAnnotationPresent(Context.class)) {
                contextMethods.add(new FrameworkMethod(method));
            }
        }
        return contextMethods;
    }

    private boolean notParametric(Method method) {
        return method.getParameterTypes().length == 0;
    }
}
