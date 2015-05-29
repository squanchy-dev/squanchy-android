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

/**
 * Created on 17.04.2015.
 */
public class RequestConfig {
    private BaseRequest.RequestFormat requestFormat;
    private BaseRequest.ResponseFormat responseFormat;
    private Object responseClassSpecifier;
    private Object errorResponseClassSpecifier;

    public RequestConfig()
    {

    }

    public RequestConfig(Object responseClassSpecifier)
    {
       this(responseClassSpecifier,null,null);
    }

    public RequestConfig(Object responseClassSpecifier,BaseRequest.RequestFormat requestFormat, BaseRequest.ResponseFormat responseFormat)
    {
        this.responseClassSpecifier = responseClassSpecifier;
        this.requestFormat = requestFormat;
        this.responseFormat = responseFormat;
    }

    public BaseRequest.RequestFormat getRequestFormat() {
        return requestFormat;
    }

    public void setRequestFormat(BaseRequest.RequestFormat requestFormat) {
        this.requestFormat = requestFormat;
    }

    public BaseRequest.ResponseFormat getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(BaseRequest.ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }

    /**
     * @return  Class or Type, returned as data field of ResultData object, can be null if you don't need one.
     */
    public Object getResponseClassSpecifier() {
        return responseClassSpecifier;
    }

    /**
     * @param responseClassSpecifier  Class or Type, returned as data field of ResultData object, can be null if you don't need one.
     */
    public void setResponseClassSpecifier(Object responseClassSpecifier) {
        this.responseClassSpecifier = responseClassSpecifier;
    }

    public Object getErrorResponseClassSpecifier() {
        return errorResponseClassSpecifier;
    }

    public void setErrorResponseClassSpecifier(Object errorResponseClassSpecifier) {
        this.errorResponseClassSpecifier = errorResponseClassSpecifier;
    }
}
