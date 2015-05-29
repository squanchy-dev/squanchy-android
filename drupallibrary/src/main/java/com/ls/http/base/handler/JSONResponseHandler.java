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

package com.ls.http.base.handler;

import com.google.gson.Gson;

import com.ls.http.base.BaseStringResponseHandler;
import com.ls.http.base.IResponseItem;
import com.ls.http.base.SharedGson;
import com.ls.util.internal.ObjectsFactory;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

class JSONResponseHandler extends BaseStringResponseHandler
{

    protected Object itemFromResponse(@NonNull String json,@NonNull Class<?> theClass)
	{
		Object result = createInstanceByInterface(json, theClass);
		if (result == null)
		{
			Gson gson = SharedGson.getGson();
			result = gson.fromJson(json, theClass);			
		}
		return result;
	}

    protected Object itemFromResponse(@NonNull String json,@NonNull Type theType)
	{		
		Class<?> theClass = theType.getClass();

		Object result = createInstanceByInterface(json, theClass);
		if (result == null)
		{
			Gson gson = SharedGson.getGson();
			result = gson.fromJson(json, theType);			
		}
		return result;
	}

    @Override
    protected String getAcceptValueType() {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_JSON;
    }

    private Object createInstanceByInterface(String json, Class<?> theClass)
	{
		Object result = null;

		if (IResponseItem.class.isAssignableFrom(theClass))
		{
			IResponseItem item;
			item = (IResponseItem) ObjectsFactory.newInstance(theClass);
			item.initWithJSON(json);
			result = item;
		}
		return result;
	}

}
