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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for {@link BehaviorExtensionRunner BehaviorExtensionRunner.class}.
 *
 * @author Balazs Berkes
 */
@RunWith(BehaviorExtensionRunner.class)
public class BehaviorExtensionRunnerTest {

    private static final String NEW_GUY = "new guy";
    private static final String FRIDAY = "friday";

    private String employee;
    private String day;

    @Before
    public void assertBeforeRunsBeforeContexts() {
        assertNull(employee);
        assertNull(day);
    }

    @Context("new employee")
    public void givenNewEmployee() {
        employee = NEW_GUY;
    }

    @Context("today is friday")
    public void givenFriday() {
        day = FRIDAY;
    }

    @Test
    @Given("new employee")
    public void testNewEmployee() {
        assertEquals(employee, NEW_GUY);
        assertNull(day);
    }

    @Test
    @Given({"new employee", "today is friday"})
    public void testNewEmployeeOnFriday() {
        assertEquals(employee, NEW_GUY);
        assertEquals(day, FRIDAY);
    }
}
