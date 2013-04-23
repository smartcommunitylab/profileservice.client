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

import org.json.JSONObject;

import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfiles;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfile;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfiles;

/**
 * Class used to connect with the profile service.
 * 
 */
public class ProfileService {

	private String profileManagerURL;

	/** Basic profile path */
	private static final String BASIC_PROFILE = "eu.trentorise.smartcampus.profileservice.model.BasicProfile/";
	/** Extended profile path */
	private static final String EXTENDED_PROFILE = "eu.trentorise.smartcampus.profileservice.model.ExtendedProfile/";

	/**
	 * 
	 * @param serverURL
	 *            address of the server to connect to
	 */
	public ProfileService(String serverURL) {
		this.profileManagerURL = serverURL;
		if (!profileManagerURL.endsWith("/")) profileManagerURL += '/';
	}

	/**
	 * Return the basic profile associated to the authorization token owner
	 * 
	 * @param token
	 *            an authorization token
	 * @return a basic profile
	 * @throws ProfileServiceException
	 */
	public BasicProfile getBasicProfile(String token) throws SecurityException,
			ProfileServiceException {
		try {
			String json = RemoteConnector.getJSON(profileManagerURL, BASIC_PROFILE + "me", token);
			return BasicProfile.valueOf(json);
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
	 *            an authorization token
	 * @return a basic profile
	 * @throws ProfileServiceException
	 */
	public BasicProfile getBasicProfile(String userId, String token)
			throws SecurityException, ProfileServiceException {
		try {
			String json = RemoteConnector.getJSON(profileManagerURL,
					BASIC_PROFILE + userId + "/", token);
			return BasicProfile.valueOf(json);
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
	 *            an authorization token
	 * @return a list of basic profile
	 * @throws ProfileServiceException
	 */
	public List<BasicProfile> getBasicProfiles(String filter, String token)
			throws SecurityException, ProfileServiceException {
		String query = null;
		try {
			query = "filter="
					+ URLEncoder.encode(filter == null ? "" : filter, "utf8");
		} catch (UnsupportedEncodingException e1) {
			throw new ProfileServiceException(e1);
		}

		String json;
		try {
			json = RemoteConnector.getJSON(profileManagerURL, BASIC_PROFILE + "?" + query, token);
		} catch (RemoteException e1) {
			throw new ProfileServiceException(e1);
		}

		try {
			return BasicProfiles.valueOf(json).getProfiles();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	/**
	 * Returns the list of basic profiles of a list of users
	 * 
	 * @param userIds
	 * @param token
	 * @return
	 * @throws ProfileServiceException
	 */
	public List<BasicProfile> getBasicProfilesByUserId(List<String> userIds,
			String token) throws ProfileServiceException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userIds", userIds);
		try {
			String json = RemoteConnector.getJSON(profileManagerURL,
					"/profiles", token, parameters);
			return BasicProfiles.valueOf(json).getProfiles();
		} catch (RemoteException e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Create an extended profile
	 * 
	 * @param userId
	 *            id of the user
	 * @param appId
	 *            id of the application
	 * @param profileId
	 *            id of the profile
	 * @param content
	 *            profile content
	 * @param token
	 *            an authorization token
	 * @throws ProfileServiceException
	 */
	public void createExtendedProfile(String userId, String appId,
			String profileId, Map<String, Object> content, String token)
			throws SecurityException, ProfileServiceException {
			try {
				profileId = URLEncoder.encode(profileId, "utf8");
				appId = URLEncoder.encode(appId, "utf8");
				RemoteConnector.postJSON(profileManagerURL,
						EXTENDED_PROFILE + userId + "/" + appId
								+ "/" + profileId,
						new JSONObject(content).toString(), token);
			} catch (UnsupportedEncodingException e) {
				throw new ProfileServiceException(e);
			} catch (RemoteException e) {
				throw new ProfileServiceException(e);
			}
	}

	/**
	 * Creates an extended profile for authenticated user
	 * 
	 * @param appId
	 * @param profileId
	 * @param content
	 * @param token
	 * @throws SecurityException
	 * @throws ProfileServiceException
	 */
	public void createExtendedProfile(String appId, String profileId,
			Map<String, Object> content, String token)
			throws SecurityException, ProfileServiceException {
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector.postJSON(profileManagerURL, EXTENDED_PROFILE + "me/" + appId + "/" + profileId,
					new JSONObject(content).toString(), token);
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Returns an extended profile of a user if authenticated user has the
	 * visibility permissions.
	 * 
	 * @param userId
	 *            id of the user
	 * @param appId
	 *            id of the application
	 * @param profileId
	 *            id of the profile
	 * @param token
	 *            an authorization token
	 * @return an extended profile
	 * @throws ProfileServiceException
	 */
	public ExtendedProfile getExtendedProfile(String userId, String appId,
			String profileId, String token) throws ProfileServiceException {
		if (userId == null || appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + userId + "/" + appId
							+ "/" + profileId, token);
			return ExtendedProfile.valueOf(json);
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Return a list of extended profiles
	 * 
	 * @param userId
	 *            id of the user
	 * @param appId
	 *            id of the application
	 * @param token
	 *            an authorization token
	 * @return a list of extended profile
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getExtendedProfiles(String userId,
			String appId, String token) throws SecurityException,
			ProfileServiceException {
		if (userId == null || appId == null)
			throw new ProfileServiceException("Incomplete request parameters");
		try {
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + userId + "/" + appId,
					token);
			return ExtendedProfiles.valueOf(json).getProfiles();
		} catch (Exception e1) {
			throw new ProfileServiceException(e1);
		}
	}

	/**
	 * Returns list of extended profiles of a list of users filtered for
	 * authenticated user visibility permissions
	 * 
	 * @param userId
	 * @param appId
	 * @param token
	 * @return
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getExtendedProfiles(List<String> userIds,
			String appId, String token) throws ProfileServiceException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userIds", userIds);
		try {
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE, token, parameters);
			return ExtendedProfiles.valueOf(json).getProfiles();
		} catch (RemoteException e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Returns the list of extended profiles filtered by a set of profile
	 * attributes and for authenticated user visibility permissions
	 * 
	 * @param appId
	 * @param profileId
	 * @param profileAttributes
	 * @param token
	 * @return
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getExtendedProfiles(String appId,
			String profileId, Map<String, Object> profileAttributes,
			String token) throws ProfileServiceException {

		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector
					.postJSON(profileManagerURL, EXTENDED_PROFILE + appId + "/" + profileId,
							new JSONObject(profileAttributes).toString(), token);
			return ExtendedProfiles.valueOf(json).getProfiles();
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}

	}

	/**
	 * Update an extended profile
	 * 
	 * @param userId
	 *            id of the user
	 * @param appId
	 *            id of the application
	 * @param profileId
	 *            id of the profile
	 * @param content
	 *            profile content
	 * @param token
	 *            an authorization token
	 * @throws ProfileServiceException
	 */
	public void updateExtendedProfile(String userId, String appId,
			String profileId, Map<String, Object> content, String token)
			throws SecurityException, ProfileServiceException {
		try {
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector.putJSON(profileManagerURL,
					EXTENDED_PROFILE + userId + "/" + appId
							+ "/" + profileId,
					new JSONObject(content).toString(), token);
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Updates an extended profile of authenticated user
	 * 
	 * @param appId
	 * @param profileId
	 * @param content
	 * @param token
	 * @throws SecurityException
	 * @throws ProfileServiceException
	 */
	public void updateExtendedProfile(String appId, String profileId,
			Map<String, Object> content, String token)
			throws SecurityException, ProfileServiceException {
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector.putJSON(profileManagerURL, 
					EXTENDED_PROFILE + "me/" + appId + "/" + profileId,
					new JSONObject(content).toString(), token);
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Delete an extended profile
	 * 
	 * @param userId
	 *            id of the user
	 * @param appId
	 *            id of the application
	 * @param profileId
	 *            id of the profile
	 * @param token
	 *            an authorization token
	 * @throws ProfileServiceException
	 */
	public void deleteExtendedProfile(String userId, String appId,
			String profileId, String token) throws SecurityException,
			ProfileServiceException {
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector.deleteJSON(profileManagerURL,
					EXTENDED_PROFILE + userId + "/" + appId
							+ "/" + profileId, token);
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Deletes an extended profile of authenticated user
	 * 
	 * @param appId
	 * @param profileId
	 * @param token
	 * @throws SecurityException
	 * @throws ProfileServiceException
	 */
	public void deleteExtendedProfile(String appId, String profileId,
			String token) throws SecurityException, ProfileServiceException {
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector
					.deleteJSON(profileManagerURL,
							EXTENDED_PROFILE + "me/" + appId + "/"
							+ profileId, token);
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

}
