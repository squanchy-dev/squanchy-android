/*
 * The MIT License (MIT)
 *  Copyright (c) 2014 Lemberg Solutions Limited
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ls.util.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import com.ls.http.base.SharedGson;

import junit.framework.Assert;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ObjectComparator
{
	private final static Object UNCHANGED = new Object(){};
	
	private Gson converter ;
	
	public ObjectComparator(){
		converter = SharedGson.getGson();
	}
			
	public static class Snapshot
	{
		private final JsonElement data;
		private Snapshot(Object dataSource, Gson converter)
		{			
			this.data = converter.toJsonTree(dataSource);
		}
		
		protected JsonElement getData()
		{
			return data;
		}		
		
		@Override
		public boolean equals(Object o)
		{
			if(o instanceof Snapshot)
			{
				return this.data.equals(((Snapshot)o).data);
			} else {
				return false;
			}
		}
	}
		
	public Snapshot createSnapshot(@NonNull final Object obj)
	{
		Assert.assertNotNull("You can't create Footprint from null object", obj);
		return new Snapshot(obj, converter);
	}
	
	/**
	 * 
	 * @param origin origin original object footprint
	 * @param updated updated updated object footprint
	 * @return null if there is no differences or differences JSON string.
	 */
	public final @Nullable
    String getDifferencesJSON(@NonNull Snapshot origin,@NonNull Snapshot updated)
	{
		Object difference = getDifferences(origin, updated);
		if(difference!= null)
		{
			return this.converter.toJson(difference);
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * @param origin original object footprint
	 * @param updated updated object footprint
	 * @return null if there are no differences or differences Map(or List, depending on input object structure) in case if there are ones.
	 */
	public final static @Nullable
    Object getDifferences(@NonNull Snapshot origin,@NonNull Snapshot updated)
	{		
		Assert.assertNotNull("Origin footprint can't be null", origin);
		Assert.assertNotNull("Updated object footprint can't be null", updated);
		
		if(origin.equals(updated))
		{
			//If strings are equal - there is nothing to compare.
			return null;
		}
	
		Object result = getDifferencesObject(origin.getData(), updated.getData());
		if(result != UNCHANGED)
		{
			return result;
		}else{
			return null;
		}
	}
	
	private static @Nullable
    Object getDifferencesObject(JsonElement origin,JsonElement patched)
	{
		if(origin != null && origin.equals(patched))
		{
			return UNCHANGED;
		}
		
		if(patched == null || patched.isJsonNull())
		{
			return null;
		}
		
		if(origin == null||origin.isJsonNull())
		{
			return  convertElementToStringRepresentation(patched);	
		}
			
		if(origin.isJsonArray())
		{
			if(patched.isJsonArray())
			{
				return getDifferencesForArrays((JsonArray)origin, (JsonArray)patched);
			}else{
				return convertElementToStringRepresentation(patched);
			}			
		}
		
		if(origin.isJsonObject())
		{
			if(patched.isJsonObject())
			{
				return getDifferencesMapForObjects((JsonObject)origin, (JsonObject)patched);
			}else{
				return convertElementToStringRepresentation(patched);
			}
		}
		
		if(origin.isJsonPrimitive())
		{
			if(patched.isJsonPrimitive())
			{
				return getDifferencesForPrimitives((JsonPrimitive)origin, (JsonPrimitive)patched);
			}else{
				return convertElementToStringRepresentation(patched);
			}
		}
		
		return convertElementToStringRepresentation(patched);
	}	
	
	private static Object getDifferencesMapForObjects(JsonObject origin,JsonObject patched)
	{				
		final Map<String,Object> result = new HashMap<String, Object>();
		
		//Create origin entry set
		Set<String> originKeySet = new HashSet<String>();//Later here will be only keys, removed in patched version.
		for(Entry<String, JsonElement> entry: origin.entrySet())
		{
			originKeySet.add(entry.getKey());
		}
		
		for(Entry<String, JsonElement> entry: patched.entrySet())
		{
			originKeySet.remove(entry.getKey());
			Object difference = getDifferencesObject(origin.get(entry.getKey()), entry.getValue());
			if(difference != UNCHANGED)
			{
				result.put(entry.getKey(),difference);
			}
		}
		
		for(String key:originKeySet)
		{
			result.put(key, null);
		}
		
		if(result.isEmpty())
		{
			return UNCHANGED;
		}else{
			return result;
		}
	}	

	private static Object getDifferencesForArrays(JsonArray origin,JsonArray patched)
	{	
		if(origin.equals(patched))
		{
			return UNCHANGED;
		}
		//TODO improve differences calculation for arrays.
		return convertElementToStringRepresentation(patched);		
	}	
	
	private static Object getDifferencesForPrimitives(JsonPrimitive origin,JsonPrimitive patched)
	{			
		if(patched.equals(origin))
		{
			return UNCHANGED;
		}else{
			return patched.toString();
		}
	}	
	
	//------------------Convert JSON to generic object structure
	
	private static Object convertElementToStringRepresentation(JsonElement source)
	{
		if(source.isJsonNull())
		{
			return null;
		}
		
		if(source.isJsonPrimitive())
		{
			return source.toString();
		}
		
		if(source.isJsonObject())
		{
			return getMapFromJsonElement((JsonObject)source);
		}
		
		if(source.isJsonArray())
		{
			return getListFromJsonElement((JsonArray)source);
		}
		
		return null;
	}
	
	private static Map<String,Object> getMapFromJsonElement(JsonObject object)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		for(Entry<String, JsonElement> entry: object.entrySet())
		{
			result.put(entry.getKey(), convertElementToStringRepresentation(entry.getValue()));
		}
		
		return result;
	}
	
	private static List<Object> getListFromJsonElement(JsonArray object)
	{
		List<Object> result = new ArrayList<Object>(object.size());
		for(JsonElement element:object)
		{
			result.add(element);
		}
		return result;
	}
}
