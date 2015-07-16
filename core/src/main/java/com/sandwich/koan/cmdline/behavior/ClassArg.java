/**
 * Copyright 2015 Java Koans
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.path.PathToEnlightenment;

public class ClassArg extends AbstractArgumentBehavior {

	public void run(String...koanSuiteClassName) {
		if(koanSuiteClassName != null && 
				koanSuiteClassName.length > 0 &&
				koanSuiteClassName[0] != null && 
				koanSuiteClassName[0].trim().length() != 0){
			PathToEnlightenment.filterBySuite(koanSuiteClassName[0]);
		}
	}
	
}
