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

import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;

public abstract class RequestHandler
{
	protected final String DEFAULT_CHARSET = "utf-8";
	
	protected Object object;
	
	public abstract String stringBodyFromItem();

    public abstract String getBodyContentType(String defaultCharset);

    public abstract byte[]getBody(String defaultCharset) throws UnsupportedEncodingException;
	
	public RequestHandler()
	{

	}
	
	protected boolean implementsPostableInterface()
	{
		return object instanceof IPostableItem;
	}
	
	protected String getCharset(@Nullable String defaultCharset)
	{
		String charset = null;
		if(object instanceof ICharsetItem)
		{
			charset =  ((ICharsetItem)object).getCharset();
		}
		
		if(charset == null)
		{		
			charset = defaultCharset;;
		}
		
		if(charset == null)
		{		
			charset = DEFAULT_CHARSET;
		}
		return charset;
	}

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
