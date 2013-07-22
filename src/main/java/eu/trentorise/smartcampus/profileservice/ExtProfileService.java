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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfile;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfiles;

/**
 * Class used to connect with the profile service.
 * 
 */
public class ExtProfileService {

	private String profileManagerURL;

	/** Extended profile path */
	private static final String EXTENDED_PROFILE = "extprofile/";

	/**
	 * 
	 * @param serverURL
	 *            address of the server to connect to
	 */
	public ExtProfileService(String serverURL) {
		this.profileManagerURL = serverURL;
		if (!profileManagerURL.endsWith("/")) profileManagerURL += '/';
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
				if (userId == null || appId == null || profileId == null)
					throw new ProfileServiceException("Incomplete request parameters");
				
				profileId = URLEncoder.encode(profileId, "utf8");
				appId = URLEncoder.encode(appId, "utf8");
				RemoteConnector.postJSON(profileManagerURL,
						EXTENDED_PROFILE+"app/" + userId + "/" + appId
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
	public void createMyExtendedProfile(String appId, String profileId,
			Map<String, Object> content, String token)
			throws SecurityException, ProfileServiceException {
		try {
			if (appId == null || profileId == null)
				throw new ProfileServiceException("Incomplete request parameters");

			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector.postJSON(profileManagerURL, EXTENDED_PROFILE + "me/" + appId + "/" + profileId,
					new JSONObject(content).toString(), token);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Returns an extended profile of a user.
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
			String profileId, String token) throws SecurityException, ProfileServiceException {
		if (userId == null || appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + "app/" + userId + "/" + appId
							+ "/" + profileId, token);
			return ExtendedProfile.valueOf(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Returns an extended profile of the authenticated user.
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
	public ExtendedProfile getMyExtendedProfile(String appId,
			String profileId, String token) throws SecurityException, ProfileServiceException {
		if (appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + "me/" + appId + "/" + profileId, token);
			return ExtendedProfile.valueOf(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Return a list of extended profiles of the authenticated user
	 * 
	 * @param appId
	 *            id of the application
	 * @param token
	 *            an authorization token
	 * @return a list of extended profile
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getMyExtendedProfiles(String appId, String token) throws SecurityException,
			ProfileServiceException {
		try {
			if (appId == null)
				throw new ProfileServiceException("Incomplete request parameters");
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + "me/" + appId,
					token);
			return ExtendedProfiles.valueOf(json).getProfiles();
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e1) {
			throw new ProfileServiceException(e1);
		}
	}

	/**
	 * Return a list of extended profiles of the authenticated user
	 * 
	 * @param token
	 *            an authorization token
	 * @return a list of extended profile
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getMyExtendedProfiles(String token) throws SecurityException,
			ProfileServiceException {
		try {
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + "me",
					token);
			return ExtendedProfiles.valueOf(json).getProfiles();
		} catch (Exception e1) {
			throw new ProfileServiceException(e1);
		}
	}

	/**
	 * Returns an extended profile of the authenticated user.
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
	public List<ExtendedProfile> getSharedExtendedProfiles(String appId,
			String profileId, String token) throws SecurityException, ProfileServiceException {
		if (appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + "shared/" + appId + "/" + profileId, token);
			return ExtendedProfiles.valueOf(json).getProfiles();
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Return a list of extended profiles of the authenticated user
	 * 
	 * @param appId
	 *            id of the application
	 * @param token
	 *            an authorization token
	 * @return a list of extended profile
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getSharedExtendedProfiles(String appId, String token) throws SecurityException,
			ProfileServiceException {
		try {
			if (appId == null)
				throw new ProfileServiceException("Incomplete request parameters");
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + "shared/" + appId,
					token);
			return ExtendedProfiles.valueOf(json).getProfiles();
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e1) {
			throw new ProfileServiceException(e1);
		}
	}

	/**
	 * Return a list of extended profiles of the authenticated user
	 * 
	 * @param token
	 *            an authorization token
	 * @return a list of extended profile
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getSharedExtendedProfiles(String token) throws SecurityException,
			ProfileServiceException {
		try {
			String json = RemoteConnector.getJSON(profileManagerURL,
					EXTENDED_PROFILE + "shared",
					token);
			return ExtendedProfiles.valueOf(json).getProfiles();
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e1) {
			throw new ProfileServiceException(e1);
		}
	}

	/**
	 * Returns list of extended profiles of a list of users
	 * 
	 * @param userId
	 * @param appId optional app identity
	 * @param profileId optional profile identity
	 * @param token
	 * @return
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getExtendedProfilesForUsers(List<String> userIds,
			String appId, String profileId, String token) throws ProfileServiceException, SecurityException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userIds", userIds);
		try {
			String path = EXTENDED_PROFILE+"all";
			if (appId != null) path += "/"+appId;
			if (profileId != null) {
				if (appId == null) throw new ProfileServiceException("App id is required when profile id is used");
				path += "/"+profileId;
			}
			String json = RemoteConnector.getJSON(profileManagerURL, 	path, token, parameters);
			return ExtendedProfiles.valueOf(json).getProfiles();
		}catch (SecurityException e) {
			throw e;
		} catch (RemoteException e) {
			throw new ProfileServiceException(e);
		}
	}

	/**
	 * Returns the list of extended profiles filtered by a set of profile
	 * attributes 
	 * 
	 * @param appId
	 * @param profileId
	 * @param profileAttributes
	 * @param token
	 * @return
	 * @throws ProfileServiceException
	 */
	public List<ExtendedProfile> getExtendedProfilesByAttributes(String appId,
			String profileId, Map<String, Object> profileAttributes,
			String token) throws SecurityException,  ProfileServiceException {

		if (appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");

		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			String json = RemoteConnector
					.postJSON(profileManagerURL, EXTENDED_PROFILE +"all/" + appId + "/" + profileId,
							new JSONObject(profileAttributes).toString(), token);
			return ExtendedProfiles.valueOf(json).getProfiles();
		}catch (SecurityException e) {
			throw e;
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
		if (userId == null || appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");

		try {
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector.putJSON(profileManagerURL,
					EXTENDED_PROFILE + "app/" + userId + "/" + appId
							+ "/" + profileId,
					new JSONObject(content).toString(), token);
		}catch (SecurityException e) {
			throw e;
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
	public void updateMyExtendedProfile(String appId, String profileId,
			Map<String, Object> content, String token)
			throws SecurityException, ProfileServiceException {

		if (appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");
		
		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector.putJSON(profileManagerURL, 
					EXTENDED_PROFILE + "me/" + appId + "/" + profileId,
					new JSONObject(content).toString(), token);
		}catch (SecurityException e) {
			throw e;
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
		if (userId == null || appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");

		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector.deleteJSON(profileManagerURL,
					EXTENDED_PROFILE + "app/" + userId + "/" + appId
							+ "/" + profileId, token);
		}catch (SecurityException e) {
			throw e;
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
	public void deleteMyExtendedProfile(String appId, String profileId,
			String token) throws SecurityException, ProfileServiceException {
		if (appId == null || profileId == null)
			throw new ProfileServiceException("Incomplete request parameters");

		try {
			profileId = URLEncoder.encode(profileId, "utf8");
			appId = URLEncoder.encode(appId, "utf8");
			RemoteConnector
					.deleteJSON(profileManagerURL,
							EXTENDED_PROFILE + "me/" + appId + "/"
							+ profileId, token);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new ProfileServiceException(e);
		}
	}

}
