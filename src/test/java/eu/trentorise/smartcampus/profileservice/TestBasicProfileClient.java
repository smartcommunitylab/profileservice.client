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

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

public class TestBasicProfileClient {

	private BasicProfileService profileConnector;

	@Before
	public void init() {
		profileConnector = new BasicProfileService(Constants.BASIC_PROFILE_SRV_URL);
	}

	@Test
	public void accountProfile() throws SecurityException, ProfileServiceException {
		AccountProfile accountProfile = profileConnector.getAccountProfile(Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(accountProfile);
		System.out.println(accountProfile);
		
	}
	@Test
	public void basicProfile() throws Exception {
		BasicProfile result;
		List<BasicProfile> results;

		// get token owner profile
		result = profileConnector.getBasicProfile(Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(result);
		System.out.println(result);

		// get token owner profile
		result = profileConnector.getBasicProfileBySocialId(result.getSocialId(),Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(result);
		System.out.println(result);

		// get all profiles
		results = profileConnector.getBasicProfiles(null,
				Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(results);
		System.out.println(results);

		// get profiles with filter
		results = profileConnector
				.getBasicProfiles("a", Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(results);
		System.out.println(results);

		// get profiles with filter, no results
		results = profileConnector
				.getBasicProfiles("?", Constants.USER_AUTH_TOKEN);
		Assert.assertTrue(results == null || results.size() == 0);
		System.out.println(results);
	}

}
