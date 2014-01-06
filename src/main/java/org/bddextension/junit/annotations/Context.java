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
package org.bddextension.junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test methods annotated with {@code @Context} will be a pre-step of the test method. These methods will be called
 * between the setup and the test if they are referred from {@link org.bddextension.junit.Given @Given} annotation.
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
public @interface Context {

    /**
     * Name of the context which can be referred in {@code @Given} annotation.
     * <p>
     * @return name of the context
     */
    String value() default "";

}
