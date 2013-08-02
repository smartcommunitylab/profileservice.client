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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An extended profile
 * 
 */
public class ExtendedProfile {

	/**
	 * Id of the profile
	 */
	private String profileId;

	/**
	 * Content
	 */
	private Map<String, Object> content;

	/**
	 * Id of the user
	 */
	private String userId;

	/**
	 * Social id
	 */
	private String socialId;

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param json
	 * @return
	 */
	public static ExtendedProfile valueOf(String json) {
		try {
			JSONObject o = new JSONObject(json);
			ExtendedProfile profile = new ExtendedProfile();
			profile.setProfileId(o.getString("profileId"));
			profile.setSocialId(""+o.getLong("socialId"));
			profile.setUserId(o.getString("userId"));
			profile.setContent(toMap(o.getJSONObject("content")));
			return profile;
		} catch (JSONException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> toMap(JSONObject o) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator<String> keys = o.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			map.put(key, fromJson(o.get(key)));
		}
		return map;
	}

	private static Object fromJson(Object json) throws JSONException {
		if (json == JSONObject.NULL) {
			return null;
		} else if (json instanceof JSONObject) {
			return toMap((JSONObject) json);
		} else if (json instanceof JSONArray) {
			return toList((JSONArray) json);
		} else {
			return json;
		}
	}

	private static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			list.add(fromJson(array.get(i)));
		}
		return list;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	@Override
	public String toString() {
		return "ExtendedProfile [profileId=" + profileId
				+ ", content=" + content + ", userId=" + userId + ", socialId="
				+ socialId + "]";
	}
	
	
}
