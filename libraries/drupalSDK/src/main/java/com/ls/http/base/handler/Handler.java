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

import com.ls.http.base.BaseRequest;
import com.ls.http.base.RequestHandler;
import com.ls.http.base.ResponseHandler;


/**
 * Created on 15.04.2015.
 */
public class Handler {

    protected static final String PROTOCOL_REQUEST_APP_TYPE_JSON = "application/json";
    protected static final String PROTOCOL_REQUEST_APP_TYPE_XML = "application/xml";
    protected static final String PROTOCOL_REQUEST_APP_TYPE_JSON_HAL = "application/hal+json";
    protected static final String PROTOCOL_REQUEST_APP_TYPE_TEXT = "text/plain";

    protected static final String CONTENT_TYPE_CHARSET_PREFIX = "; charset=";

    public static RequestHandler getRequestHandlerForFormat(BaseRequest.RequestFormat requestFormat) {
        switch (requestFormat) {
            case XML:
                return new XMLRequestHandler();
            case JSON_HAL:
                return new JSONRequestHandler();
            case JSON:
                return new JSONRequestHandler();
            case TEXT:
                return new TextRequestHandler();
            case MULTIPART:
                return new MultipartRequestHandler();
            default:
                throw new IllegalArgumentException("Unrecognised request requestFormat:" + requestFormat.name());
        }
    }

    public static ResponseHandler getResponseHandlerForFormat(BaseRequest.ResponseFormat responseFormat)
    {
        switch (responseFormat) {
            case XML:
                return new XMLResponseHandler();
            case TEXT:
                return new TextResponseHandler();
            case JSON:
                return new JSONResponseHandler();
            case JSON_HAL:
                return new JSONHALResponseHandler();
            case BYTE:
                return new PlainByteReponseHandler();
            default: {
                throw new IllegalArgumentException("Unrecognised request responseFormat:"+responseFormat.name());
            }
        }
    }
}
