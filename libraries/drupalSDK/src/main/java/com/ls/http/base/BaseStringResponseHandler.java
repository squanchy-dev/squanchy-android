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

package com.ls.http.base;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;


public abstract class BaseStringResponseHandler extends ResponseHandler
{
    protected abstract Object itemFromResponse(@NonNull String response, @NonNull Class<?> theClass);
    protected abstract Object itemFromResponse(@NonNull String response, @NonNull Type theType);

    protected Object itemFromResponseWithSpecifier(String response, Object theSpecifier)
	{
        Log.d("Json", response);

        Object result = null;
		if(response != null && theSpecifier != null)
		{
			if(theSpecifier instanceof Class<?>)
			{
                result = itemFromResponse(response, (Class<?>)theSpecifier);
			}else if(theSpecifier instanceof Type){
                result = itemFromResponse(response, (Type)theSpecifier);
			}else{
				throw new IllegalArgumentException("You have to specify Class<?> or Type instance");
			}
		}
		return result;
	}

    protected Response<ResponseData> parseNetworkResponse(NetworkResponse response,Object responseClassSpecifier)
    {
        String resultStr = parseResponseString(response);
        ResponseData responseData = new ResponseData();

        responseData.statusCode = response.statusCode;
        responseData.headers = new HashMap<String, String>(response.headers);

        if(!TextUtils.isEmpty(resultStr))
        {
            responseData.data = this.itemFromResponseWithSpecifier(resultStr, responseClassSpecifier);
        }

        Response<ResponseData> result = Response.success(responseData, HttpHeaderParser.parseCacheHeaders(response));

        responseData.error = result.error;

        return result;
    };

    protected String parseResponseString(NetworkResponse response) {
        String parsed = null;
        if(response.data != null) {
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                parsed = new String(response.data);
            }
        }
        return parsed;
    }
}
