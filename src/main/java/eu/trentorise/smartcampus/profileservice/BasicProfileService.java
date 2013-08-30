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
package eu.trentorise.smartcampus.profileservice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfiles;

/**
 * Class used to connect with the profile service.
 * 
 */
public class BasicProfileService {

	private String profileManagerURL;

	/** Basic profile path */
	private static final String BASIC_PROFILE = "basicprofile/";
	/** account profile path */
	private static final String ACCOUNT_PROFILE = "accountprofile/";

	private static final String ALL = "all/";

	/**
	 * 
	 * @param serverURL
	 *            address of the server to connect to
	 */
	public BasicProfileService(String serverURL) {
		this.profileManagerURL = serverURL;
		if (!profileManagerURL.endsWith("/")) profileManagerURL += '/';
	}

	/**
	 * Return the basic profile associated to the access token owner
	 * 
	 * @param token
	 *            a user access token
	 * @return a basic profile
	 * @throws ProfileServiceException
	 */
	public BasicProfile getBasicProfile(String token) throws SecurityException,
			ProfileServiceException {
		try {
			String json = RemoteConnector.getJSON(profileManagerURL, BASIC_PROFILE + "me", token);
			return JsonUtils.toObject(json, BasicProfile.class);
		} catch (RemoteException e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Return the account profile associated to the access token owner
	 * 
	 * @param token
	 *            a user access token
	 * @return a basic profile
	 * @throws ProfileServiceException
	 */
	public AccountProfile getAccountProfile(String token) throws SecurityException,
			ProfileServiceException {
		try {
			String json = RemoteConnector.getJSON(profileManagerURL, ACCOUNT_PROFILE + "me", token);
			return AccountProfile.valueOf(json);
		} catch (RemoteException e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Return the basic profile associated to the access token owner
	 * 
	 * @param token
	 *            a user or client access token
	 * @return a basic profile
	 * @throws ProfileServiceException
	 */
	public BasicProfile getBasicProfileBySocialId(String socialId, String token) throws SecurityException,
			ProfileServiceException {
		try {
			String json = RemoteConnector.getJSON(profileManagerURL, BASIC_PROFILE + "social/"+socialId, token);
			return JsonUtils.toObject(json, BasicProfile.class);
		} catch (RemoteException e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Return a basic profile of a user identified by his userId
	 * 
	 * @param userId
	 *            id of the user
	 * @param token
	 *            an user or client access token
	 * @return a basic profile
	 * @throws ProfileServiceException
	 */
	public BasicProfile getBasicProfile(String userId, String token)
			throws SecurityException, ProfileServiceException {
		try {
			String json = RemoteConnector.getJSON(profileManagerURL,
					BASIC_PROFILE+ ALL + userId + "/", token);
			return JsonUtils.toObject(json, BasicProfile.class);
		} catch (RemoteException e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Return a (filtered) list of profiles
	 * 
	 * @param filter
	 *            if not null, filter the results by full name (name and
	 *            surname)
	 * @param token
	 *            an user or client access token
	 * @return a list of basic profile
	 * @throws ProfileServiceException
	 */
	public List<BasicProfile> getBasicProfiles(String filter, String token)
			throws SecurityException, ProfileServiceException {
		String query = null;
		if (filter != null) {
			try {
				query = "?filter="
						+ URLEncoder.encode(filter == null ? "" : filter, "utf8");
			} catch (UnsupportedEncodingException e1) {
				throw new ProfileServiceException(e1);
			}
		} else {
			query = "";
		}

		String json;
		try {
			json = RemoteConnector.getJSON(profileManagerURL, BASIC_PROFILE+ALL + query, token);
		} catch (RemoteException e1) {
			throw new ProfileServiceException(e1);
		}

		try {
			return JsonUtils.toObject(json, BasicProfiles.class).getProfiles();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	/**
	 * Returns the list of basic profiles of a list of users
	 * 
	 * @param userIds
	 * @param token a user or client access token
	 * @return
	 * @throws ProfileServiceException
	 */
	public List<BasicProfile> getBasicProfilesByUserId(List<String> userIds, String token) throws ProfileServiceException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userIds", userIds);
		try {
			String json = RemoteConnector.getJSON(profileManagerURL,
					"/profiles", token, parameters);
			return JsonUtils.toObject(json, BasicProfiles.class).getProfiles();
		} catch (RemoteException e) {
			throw new ProfileServiceException(e);
		}
	}
}
