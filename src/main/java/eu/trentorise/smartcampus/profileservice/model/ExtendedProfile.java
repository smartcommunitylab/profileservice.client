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

import java.util.Map;

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
