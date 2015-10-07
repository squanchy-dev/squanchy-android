

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

package com.ls.drupal.login;

import com.android.volley.RequestQueue;
import com.ls.http.base.BaseRequest;


public interface ILoginManager {
	/**
	 * Login request, responsible for login data fetch
	 * @param userName
	 * @param password
	 * @param queue operation queue to perform login within
	 * @return login result object
	 */
	Object login(String userName, String password, RequestQueue queue);

    /**
     * @return true if manager has to perform login restore attempt in case of 401 error
     */
    boolean shouldRestoreLogin();

    /**
     * @return true if login can be restored (there are credentials or access token cached)
     */
    boolean canRestoreLogin();

	/**
	 * Add necessary authentication data to request headers or post/get parameters
	 * @param request
	 */
	void applyLoginDataToRequest(BaseRequest request);
	/**
	 * Restore login data, if possible.
     * Note: this call should be performed synchronously
     * @param queue operation queue you can to perform login within (but it isn't necessary)
     * @return true if restore succeeded (or you can't define a result) false in case of failure
	 */
	boolean restoreLoginData(RequestQueue queue);

    /**
     * Method will be called in case if {@link #restoreLoginData restoreLoginData} returned false or we get 401 exception after login was restored
     */
    void onLoginRestoreFailed();

	/**
	 * Perform logout operation
	 * @param queue
	 * @return logout request result
	 */
	Object logout(RequestQueue queue);

}
