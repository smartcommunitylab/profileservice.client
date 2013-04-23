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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wrapper for a list of extended profiles
 *
 */
public class ExtendedProfiles {

	/**
	 * A list of extended profiles
	 */
	private List<ExtendedProfile> profiles;

	public List<ExtendedProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<ExtendedProfile> profiles) {
		this.profiles = profiles;
	}

	/**
	 * @param json
	 * @return
	 */
	public static ExtendedProfiles valueOf(String json) {
		try {
			JSONObject o = new JSONObject(json);
			ExtendedProfiles profile = new ExtendedProfiles();
			profile.setProfiles(new ArrayList<ExtendedProfile>());
			JSONArray arr = o.getJSONArray("profiles");
			if (arr != null)
				for (int i = 0; i < arr.length(); i++) {
					profile.getProfiles().add(ExtendedProfile.valueOf(arr.getJSONObject(i).toString()));
				}
			return profile;
		} catch (JSONException e) {
			return null;
		}
	}
	
	
	
}
