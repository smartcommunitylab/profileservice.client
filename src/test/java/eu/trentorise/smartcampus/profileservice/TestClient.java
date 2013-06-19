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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfile;
import eu.trentorise.smartcampus.socialservice.SocialService;
import eu.trentorise.smartcampus.socialservice.SocialServiceException;
import eu.trentorise.smartcampus.socialservice.model.ShareOperation;
import eu.trentorise.smartcampus.socialservice.model.ShareVisibility;

public class TestClient {

	private ProfileService profileConnector;
	private SocialService socialConnector;

	@Before
	public void init() {
		profileConnector = new ProfileService(Constants.PROFILE_SRV_URL);
		socialConnector = new SocialService(Constants.SOCIAL_SRV_URL);
	}

	@Test
	public void basicProfile() throws Exception {
		BasicProfile result;
		List<BasicProfile> results;

		// get token owner profile
		result = profileConnector.getBasicProfile(Constants.AUTH_TOKEN_1);
		Assert.assertNotNull(result);
		System.out.println(result);

		// get all profiles
		results = profileConnector.getBasicProfiles(null,
				Constants.AUTH_TOKEN_1);
		Assert.assertNotNull(results);
		System.out.println(results);

		// get profiles with filter
		results = profileConnector
				.getBasicProfiles("a", Constants.AUTH_TOKEN_1);
		Assert.assertNotNull(results);
		System.out.println(results);

		// get profiles with filter, no results
		results = profileConnector
				.getBasicProfiles("?", Constants.AUTH_TOKEN_1);
		Assert.assertTrue(results == null || results.size() == 0);
		System.out.println(results);
	}

	@Test
	public void extendedProfile() throws Exception {
		ExtendedProfile result;
		List<ExtendedProfile> results;
		Map<String, Object> map = new TreeMap<String, Object>();
		boolean fail;

		BasicProfile profile = profileConnector
				.getBasicProfile(Constants.AUTH_TOKEN_1);
		Assert.assertNotNull(profile);

		// cannot update non existing
		fail = false;
		try {
			profileConnector.updateExtendedProfile(profile.getUserId(),
					"test_app", "1", map, Constants.AUTH_TOKEN_1);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// cannot do operations with wrong token
		fail = false;
		try {
			map.put("keyA", "valueA");
			profileConnector.createExtendedProfile(profile.getUserId(),
					"test_app", "1", map, "token");
		} catch (SecurityException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// create
		fail = false;
		try {
			map.put("keyA", "valueA");
			profileConnector.createExtendedProfile(profile.getUserId(),
					"test_app", "1", map, Constants.AUTH_TOKEN_1);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertFalse(fail);

		// cannot create an object with same userId/appId/profileId
		fail = false;
		try {
			profileConnector.createExtendedProfile(profile.getUserId(),
					"test_app", "1", map, Constants.AUTH_TOKEN_1);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// get
		result = profileConnector.getExtendedProfile(profile.getUserId(),
				"test_app", "1", Constants.AUTH_TOKEN_1);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getContent().containsKey("keyA"));
		Assert.assertTrue(result.getContent().get("keyA").equals("valueA"));
		System.out.println(result);

		// get many
		results = profileConnector.getExtendedProfiles(profile.getUserId(),
				"test_app", Constants.AUTH_TOKEN_1);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, results.size());
		System.out.println(results);

		// update
		map = new TreeMap<String, Object>();
		map.put("keyB", "valueB");
		profileConnector.updateExtendedProfile(profile.getUserId(), "test_app",
				"1", map, Constants.AUTH_TOKEN_1);

		// get
		result = profileConnector.getExtendedProfile(profile.getUserId(),
				"test_app", "1", Constants.AUTH_TOKEN_1);
		Assert.assertNotNull(result);
		Assert.assertFalse(result.getContent().containsKey("keyA"));
		Assert.assertTrue(result.getContent().containsKey("keyB"));
		Assert.assertTrue(result.getContent().get("keyB").equals("valueB"));
		System.out.println(result);

		// delete
		profileConnector.deleteExtendedProfile(profile.getUserId(), "test_app",
				"1", Constants.AUTH_TOKEN_1);

		// get with no result for deleted
		result = profileConnector.getExtendedProfile(profile.getUserId(),
				"test_app", "1", Constants.AUTH_TOKEN_1);
		Assert.assertNull(result);
		System.out.println(result);

		// cannot update deleted
		fail = false;
		try {
			profileConnector.updateExtendedProfile(profile.getUserId(),
					"test_app", "1", map, Constants.AUTH_TOKEN_1);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);
	}

	@After
	public void finish() throws SecurityException, ProfileServiceException {
		// delete
		BasicProfile profile = profileConnector
				.getBasicProfile(Constants.AUTH_TOKEN_1);
		profileConnector.deleteExtendedProfile(profile.getUserId(), "test_app",
				"1", Constants.AUTH_TOKEN_1);
	}

	@Test
	public void extendedProfileFunction() throws SecurityException,
			ProfileServiceException {

		profileConnector.deleteExtendedProfile("appId", "test profile",
				Constants.AUTH_TOKEN_1);
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("pref", "my pref");
		attrs.put("role", "developer");
		profileConnector.createExtendedProfile("appId", "test profile", attrs,
				Constants.AUTH_TOKEN_1);

		List<ExtendedProfile> extProfiles = profileConnector
				.getExtendedProfiles("appId", "test profile", attrs,
						Constants.AUTH_TOKEN_1);
		Assert.assertTrue(extProfiles.size() > 0);

		profileConnector.deleteExtendedProfile("appId", "test profile",
				Constants.AUTH_TOKEN_1);
	}

	@Test
	public void readExtendedProfile() throws SecurityException,
			ProfileServiceException, SocialServiceException {

		final String profileId = "test profile";
		final String appId = "appId";
		profileConnector.deleteExtendedProfile(appId, profileId,
				Constants.AUTH_TOKEN_1);

		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("pref", "my pref");
		attrs.put("role", "developer");
		profileConnector.createExtendedProfile(appId, profileId, attrs,
				Constants.AUTH_TOKEN_1);

		Assert.assertEquals(
				0,
				profileConnector.getExtendedProfiles(Constants.USER_ID_1,
						appId, Constants.AUTH_TOKEN_2).size());

		List<ExtendedProfile> extProfiles = profileConnector
				.getExtendedProfiles(appId, profileId, attrs,
						Constants.AUTH_TOKEN_1);

		ShareOperation options = new ShareOperation();
		ShareVisibility visibility = new ShareVisibility();
		visibility.setUserIds(Arrays.asList(396l));
		options.setEntityId(extProfiles.get(0).getSocialId());
		options.setVisibility(visibility);
		socialConnector.share(Constants.AUTH_TOKEN_1, options);

		Assert.assertEquals(
				1,
				profileConnector.getExtendedProfiles(Constants.USER_ID_1,
						appId, Constants.AUTH_TOKEN_2).size());
		profileConnector.deleteExtendedProfile(appId, profileId,
				Constants.AUTH_TOKEN_1);
	}

}
