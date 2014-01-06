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

import org.junit.Rule;

/**
 * Base test for JUnit test wants to use {@link Given @Given} and {@link Context @Context} annotations. This class uses
 * {@link BehavoirExtensionRule}.
 * <p>
 * @author Balazs Berkes
 */
abstract public class BehaviorExtensionAwareTest {

    @Rule
    public final BehavoirExtensionRule behavoirExtensionRule = new BehavoirExtensionRule();
}
