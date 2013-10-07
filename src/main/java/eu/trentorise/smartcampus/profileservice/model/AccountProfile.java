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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import eu.trentorise.smartcampus.network.JsonUtils;

/**
 * User registration information (different account of the registered user)
 * @author raman
 *
 */
public class AccountProfile {

	private Map<String,Map<String,String>> attributes;
	
	public AccountProfile() {
		attributes = new HashMap<String, Map<String,String>>();
	}

	/**
	 * @return all account names for the profile
	 */
	public Set<String> getAccountNames() {
		return attributes.keySet();
	}
	
	/**
	 * Return all the attributes of the specified account
	 * @param account
	 * @return
	 */
	public Map<String,String> getAccountAttributes(String account) {
		return attributes.get(account);
	}
	
	/**
	 * Return the value for the specified attribute of the specified account.
	 * @param account
	 * @param attribute
	 * @return
	 */
	public String getAttribute(String account, String attribute) {
		if (!attributes.containsKey(account)) return null;
		return attributes.get(account).get(attribute);
	}

	/**
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	public static AccountProfile valueOf(String json) {
		Map<String,Object> map = JsonUtils.toObject(json, Map.class);
		return valueOf(map);
	}

	/**
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static AccountProfile valueOf(Map<String, Object> inmap) {
		Map<String,Object> map = (Map<String, Object>) inmap.get("accounts");
		AccountProfile ap = new AccountProfile();
		for (String account : map.keySet()) {
			Map<String,String> attrs = (Map<String, String>) map.get(account);
			for (String attr : attrs.keySet()) {
				ap.addAttribute(account, attr, attrs.get(attr));
			}
		}
		return ap;
	}
	
	private void addAttribute(String account, String attribute, String value) {
		if (account != null && attribute != null) {
			if (attributes.get(account) == null) {
				attributes.put(account, new HashMap<String, String>());
			}
			attributes.get(account).put(attribute, value);
		}
	}

	@Override
	public String toString() {
		return "AccountProfile "+attributes;
	}

	/**
	 * @return the attributes
	 */
	public Map<String, Map<String, String>> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, Map<String, String>> attributes) {
		this.attributes = attributes;
	}
	
}
