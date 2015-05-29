
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

package com.ls.drupal;

import com.ls.http.base.BaseRequest;
import com.ls.http.base.ResponseData;

import android.support.annotation.NonNull;

public abstract class AbstractMultipartDrupalEntity extends AbstractBaseDrupalEntity
{

    /**
     * Note: Multipart entity serializer is checking if non-transient field implements {@link com.ls.http.base.handler.multipart.IMultiPartEntityPart} interface if so
     * - {@link com.ls.http.base.handler.multipart.IMultiPartEntityPart@getContentBody()} method is called and `toString` otherwise
     * @param client
     */
	public AbstractMultipartDrupalEntity(DrupalClient client)
	{
		super(client);		
	}
	
	@Override
	public @NonNull
    Object getManagedData()
	{		
		return this;
	}

    @Override
    public ResponseData pullFromServer(boolean synchronous, Object tag, OnEntityRequestListener listener) {
        throw new UnsupportedOperationException("This operation isn't supported by multipart entity");
    }

    @Override
    public ResponseData patchServerData(boolean synchronous, Class<?> resultClass, Object tag, OnEntityRequestListener listener) throws IllegalStateException {
        throw new UnsupportedOperationException("This operation isn't supported by multipart entity");
    }

    @Override
    protected BaseRequest.RequestFormat getItemRequestFormat(BaseRequest.RequestMethod method) {
        switch (method)
        {
            case PUT:
            case POST:
                return BaseRequest.RequestFormat.MULTIPART;
        }
        return null;
    }
}
