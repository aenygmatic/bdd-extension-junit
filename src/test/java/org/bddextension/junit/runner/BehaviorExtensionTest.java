/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bddextension.junit.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.bddextension.junit.Context;
import org.bddextension.junit.Given;

/**
 *
 * @author Balazs Berkes
 */
@RunWith(BehaviorExtension.class)
public class BehaviorExtensionTest {

    public static final String NEW_GUY = "new guy";
    private static final String FRIDAY = "friday";

    private String employee;
    private String day;

    @Before
    public void assertBeforeRunsBeforeContexts() {
        assertNull(employee);
        assertNull(day);
    }

    @After
    public void cleanUpFields() {
        employee = null;
        day = null;
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
    public void testRunChild() {
        assertEquals(employee, NEW_GUY);
        assertNull(day);
    }

    @Test
    @Given({"new employee", "today is friday"})
    public void testNewEmpoyeeOnFriday() {
        assertEquals(employee, NEW_GUY);
        assertEquals(day, FRIDAY);
    }

}
