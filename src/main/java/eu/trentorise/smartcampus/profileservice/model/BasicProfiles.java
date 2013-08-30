/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.profileservice.model;

import java.util.List;

/**
 * 
 * Wrapper for a list of {@link BasicProfile}
 */
public class BasicProfiles {

	/**
	 * A list of basic profiles
	 */
	private List<BasicProfile> profiles;

	public List<BasicProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<BasicProfile> profiles) {
		this.profiles = profiles;
	}
	
}
