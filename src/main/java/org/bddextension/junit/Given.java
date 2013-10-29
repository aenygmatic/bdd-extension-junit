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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test methods annotated with {@code @Given} will have custom setup steps which are defined in methods annotated with
 * {@link org.bddextension.junit.Context @Context}. The custom steps will run in the same order as they are given in the
 * parameter list.
 * <p>
 * Usage:
 * <pre>
 *     &#064;Context("weather is rainy")
 *     public void givenRainyWeather() {
 *         ... setup ...
 *     }
 *     &#064;Context("today is friday")
 *     public void givenFriday() {
 *         ... setup ...
 *     }
 *     &#064;Test
 *     &#064;Given({"weather is rainy", "today is friday"})
 *     public void testRainyWeatherOnFriday() {
 *         ... test code ...
 *     }
 * </pre>
 * <p>
 * @author Balazs Berkes
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Given {

    /**
     * Defines which steps should run before the test method.
     * <p>
     * @return list of the context names
     */
    String[] value() default {""};

}
