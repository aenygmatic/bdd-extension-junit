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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

/**
 * Test template for {@link Context @Context} and {@link Given @Given} annotations.
 *
 * @author Balazs Berkes
 */
abstract public class BehaviorExtensionTest {

    private static final String CONTEXT_1 = "context1";
    private static final String CONTEXT_2 = "context2";

    private List<String> calledContexts = new ArrayList<String>();

    @After
    public void cleanUpRecords() {
        calledContexts.clear();
    }

    @Context(CONTEXT_1)
    public void givenContext1() {
        calledContexts.add(CONTEXT_1);
    }

    @Context(CONTEXT_2)
    public void givenContext2() {
        calledContexts.add(CONTEXT_2);
    }

    @Given(CONTEXT_1)
    public void testGivenContextCalled() {
        assertSequence(CONTEXT_1);
    }

    @Test
    @Given({CONTEXT_1, CONTEXT_2})
    public void testGivenContextsWithMultipleContexts() {
        assertSequence(CONTEXT_1, CONTEXT_2);
    }

    @Test
    @Given({CONTEXT_1, CONTEXT_2, CONTEXT_1})
    public void testGivenContextsWithMultipleRedundantContexts() {
        assertSequence(CONTEXT_1, CONTEXT_2, CONTEXT_1);
    }

    private void assertSequence(String... contextSequence) {
        for (int i = 0; i < contextSequence.length; i++) {
            assertEquals(contextSequence[i], calledContexts.get(i));
        }
        assertEquals(contextSequence.length, calledContexts.size());
    }
}
