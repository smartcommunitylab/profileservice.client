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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfile;

public class TestExtProfileClient {

	private ExtProfileService extProfileConnector;
	private BasicProfileService basicProfileConnector;

	@Before
	public void init() {
		extProfileConnector = new ExtProfileService(Constants.PROFILE_SRV_URL);
		basicProfileConnector = new BasicProfileService(Constants.BASIC_PROFILE_SRV_URL); 
	}

	@Test
	public void clientExtendedProfile() throws Exception {
		ExtendedProfile result;
		List<ExtendedProfile> results;
		Map<String, Object> map = new TreeMap<String, Object>();
		boolean fail;

		BasicProfile profile = basicProfileConnector
				.getBasicProfile(Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(profile);

		// cannot update non existing
		fail = false;
		try {
			extProfileConnector.updateExtendedProfile(profile.getUserId(),"myapp", "myprofile", map, Constants.CLIENT_AUTH_TOKEN);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// cannot do operations with wrong token
		fail = false;
		try {
			map.put("keyA", "valueA");
			extProfileConnector.createExtendedProfile(profile.getUserId(), "myapp", "myprofile", map, Constants.USER_AUTH_TOKEN);
		} catch (SecurityException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// create
		fail = false;
		try {
			map.put("keyA", "valueA");
			extProfileConnector.createExtendedProfile(profile.getUserId(), "myapp", "myprofile", map, Constants.CLIENT_AUTH_TOKEN);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertFalse(fail);

		// cannot create an object with same userId/appId/profileId
		fail = false;
		try {
			extProfileConnector.createExtendedProfile(profile.getUserId(), "myapp", "myprofile", map, Constants.CLIENT_AUTH_TOKEN);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// get
		result = extProfileConnector.getExtendedProfile(profile.getUserId(),
				"myapp", "myprofile", Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getContent().containsKey("keyA"));
		Assert.assertTrue(result.getContent().get("keyA").equals("valueA"));
		System.out.println(result);

		// get many
		results = extProfileConnector.getExtendedProfilesForUsers(Collections.singletonList(profile.getUserId()),"myapp", "myprofile", Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, results.size());
		System.out.println(results);
		// get many
		results = extProfileConnector.getExtendedProfilesForUsers(Collections.singletonList(profile.getUserId()),"myapp", null, Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, results.size());
		System.out.println(results);
		// get many
		results = extProfileConnector.getExtendedProfilesForUsers(Collections.singletonList(profile.getUserId()), null, null, Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, results.size());
		System.out.println(results);

		// get many with attrs
		Map<String, Object> attrs = new TreeMap<String, Object>();
		attrs.put("keyA", "valueA");
		results = extProfileConnector.getExtendedProfilesByAttributes("myapp", "myprofile", attrs, Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, results.size());
		System.out.println(results);

		attrs.put("keyA", "valueB");
		results = extProfileConnector.getExtendedProfilesByAttributes("myapp", "myprofile", attrs, Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertEquals(0, results.size());
		
		// update
		map = new TreeMap<String, Object>();
		map.put("keyB", "valueB");
		extProfileConnector.updateExtendedProfile(profile.getUserId(), "myapp", "myprofile", map, Constants.CLIENT_AUTH_TOKEN);

		// get
		result = extProfileConnector.getExtendedProfile(profile.getUserId(), "myapp", "myprofile", Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertFalse(result.getContent().containsKey("keyA"));
		Assert.assertTrue(result.getContent().containsKey("keyB"));
		Assert.assertTrue(result.getContent().get("keyB").equals("valueB"));
		System.out.println(result);

		// delete
		extProfileConnector.deleteExtendedProfile(profile.getUserId(), "myapp", "myprofile", Constants.CLIENT_AUTH_TOKEN);

		// get with no result for deleted
		result = extProfileConnector.getExtendedProfile(profile.getUserId(), "myapp", "myprofile", Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNull(result);
		System.out.println(result);

		// cannot update deleted
		fail = false;
		try {
			extProfileConnector.updateExtendedProfile(profile.getUserId(),
					"myapp", "myprofile", map, Constants.CLIENT_AUTH_TOKEN);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);
	}

	@Test
	public void myExtendedProfile() throws Exception {
		ExtendedProfile result;
		List<ExtendedProfile> results;
		Map<String, Object> map = new TreeMap<String, Object>();
		boolean fail;

		BasicProfile profile = basicProfileConnector
				.getBasicProfile(Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(profile);

		// cannot update non existing
		fail = false;
		try {
			extProfileConnector.updateMyExtendedProfile("myapp", "myprofile", map, Constants.USER_AUTH_TOKEN);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// cannot do operations with wrong token
		fail = false;
		try {
			map.put("keyA", "valueA");
			extProfileConnector.createMyExtendedProfile("myapp", "myprofile", map, "token");
		} catch (SecurityException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// create
		fail = false;
		try {
			map.put("keyA", "valueA");
			extProfileConnector.createMyExtendedProfile("myapp", "myprofile", map, Constants.USER_AUTH_TOKEN);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertFalse(fail);

		// cannot create an object with same userId/appId/profileId
		fail = false;
		try {
			extProfileConnector.createMyExtendedProfile("myapp", "myprofile", map, Constants.USER_AUTH_TOKEN);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);

		// get
		result = extProfileConnector.getMyExtendedProfile("myapp", "myprofile", Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getContent().containsKey("keyA"));
		Assert.assertTrue(result.getContent().get("keyA").equals("valueA"));
		System.out.println(result);

		// get many
		results = extProfileConnector.getMyExtendedProfiles("myapp", Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, results.size());
		System.out.println(results);

		// update
		map = new TreeMap<String, Object>();
		map.put("keyB", "valueB");
		extProfileConnector.updateMyExtendedProfile("myapp", "myprofile", map, Constants.USER_AUTH_TOKEN);

		// get
		result = extProfileConnector.getMyExtendedProfile("myapp", "myprofile", Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(result);
		Assert.assertFalse(result.getContent().containsKey("keyA"));
		Assert.assertTrue(result.getContent().containsKey("keyB"));
		Assert.assertTrue(result.getContent().get("keyB").equals("valueB"));
		System.out.println(result);

		// delete
		extProfileConnector.deleteMyExtendedProfile("myapp", "myprofile", Constants.USER_AUTH_TOKEN);

		// get with no result for deleted
		result = extProfileConnector.getMyExtendedProfile("myapp", "myprofile", Constants.USER_AUTH_TOKEN);
		Assert.assertNull(result);
		System.out.println(result);

		// cannot update deleted
		fail = false;
		try {
			extProfileConnector.updateExtendedProfile(profile.getUserId(),
					"myapp", "myprofile", map, Constants.USER_AUTH_TOKEN);
		} catch (ProfileServiceException e) {
			fail = true;
		}
		Assert.assertTrue(fail);
	}

	@After
	public void finish() throws SecurityException, ProfileServiceException {
		// delete
		extProfileConnector.deleteMyExtendedProfile("myapp", "myprofile", Constants.USER_AUTH_TOKEN);
	}

//	@Test
//	public void sharingExtendedProfile() throws SecurityException,
//			ProfileServiceException, SocialServiceException {
//
//		BasicProfile profile1 = basicProfileConnector.getBasicProfile(Constants.AUTH_TOKEN_1);
//		BasicProfile profile2 = basicProfileConnector.getBasicProfile(Constants.AUTH_TOKEN_2);
//		
//		final String profileId = "test profile";
//		final String appId = "appId";
//		extProfileConnector.deleteMyExtendedProfile(appId, profileId, Constants.AUTH_TOKEN_1);
//
//		Map<String, Object> attrs = new HashMap<String, Object>();
//		attrs.put("pref", "my pref");
//		attrs.put("role", "developer");
//		extProfileConnector.createMyExtendedProfile(appId, profileId, attrs, Constants.AUTH_TOKEN_1);
//
//		Assert.assertEquals(
//				0,
//				extProfileConnector.getSharedExtendedProfiles(appId, Constants.AUTH_TOKEN_2).size());
//
//		ExtendedProfile extProfile = extProfileConnector
//				.getMyExtendedProfile(appId, profileId, Constants.AUTH_TOKEN_1);
//
//		ShareOperation options = new ShareOperation();
//		ShareVisibility visibility = new ShareVisibility();
//		visibility.setUserIds(Arrays.asList(396l));
//		options.setEntityId(extProfile.getSocialId());
//		options.setVisibility(visibility);
//		socialConnector.share(Constants.AUTH_TOKEN_1, options);
//
//		Assert.assertEquals(
//				1,
//				extProfileConnector.getExtendedProfiles(Constants.USER_ID_1,
//						appId, Constants.AUTH_TOKEN_2).size());
//		extProfileConnector.deleteExtendedProfile(appId, profileId,
//				Constants.AUTH_TOKEN_1);
//	}

}
