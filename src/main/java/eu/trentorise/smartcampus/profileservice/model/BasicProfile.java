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

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A basic profile
 *
 */
public class BasicProfile {
	
	/**
	 * Name of the user
	 */
	private String name;
	
	/**
	 * Surname of the user
	 */
	private String surname;
	
	/**
	 * Social id of the user
	 */
	private long socialId;
	
	/**
	 * Id of the user
	 */
	private String userId;
	
	private long version;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public long getSocialId() {
		return socialId;
	}

	public void setSocialId(long socialId) {
		this.socialId = socialId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static BasicProfile valueOf(String json)  {
		try {
			JSONObject o = new JSONObject(json);
			BasicProfile profile = new BasicProfile();
			profile.setName(o.getString("name"));
			profile.setSocialId(o.getLong("socialId"));
			profile.setSurname(o.getString("surname"));
			profile.setUserId(o.getString("userId"));
			profile.setVersion(o.getLong("version"));
			return profile;
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "BasicProfile [name=" + name + ", surname=" + surname
				+ ", socialId=" + socialId + ", userId=" + userId + "]";
	}
	
	
}
