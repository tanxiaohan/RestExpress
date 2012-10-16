/*
    Copyright 2012, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.restexpress.query;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Test;

import com.strategicgains.restexpress.Request;

/**
 * @author toddf
 * @since Jul 27, 2012
 */
public class QueryFilterTest
{
	@Test
	public void shouldParseFilterHeader()
	{
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.example.com/somethings");
		httpRequest.addHeader("filter", "name::todd|description::amazing");
		Request request = new Request(httpRequest, null);
		QueryFilter f = QueryFilter.parseFrom(request);
		assertTrue(f.hasFilters());
		FCallback callback = new FCallback();
		f.iterate(callback);
		assertEquals(2, callback.getFilterCount());
		assertEquals("todd", callback.get("name"));
		assertEquals("amazing", callback.get("description"));
	}
	
	private class FCallback
	implements FilterCallback
	{
		private Map<String, String> filters = new HashMap<String, String>();

        @Override
        public void filterOn(FilterComponent c)
        {
        	filters.put(c.getField(), c.getValue());
        }
        
        public int getFilterCount()
        {
        	return filters.size();
        }
        
        public String get(String name)
        {
        	return filters.get(name);
        }
	}
}
