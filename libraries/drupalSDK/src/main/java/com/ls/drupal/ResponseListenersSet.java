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

import com.android.volley.Request;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created on 27.03.2015.
 */
public class ResponseListenersSet {
    private Map<Request, List<ListenerHolder>> listeners;
    public ResponseListenersSet()
    {
        listeners = new HashMap<Request, List<ListenerHolder>>();
    }

    /**
     *
     * @param request
     * @param listener listener to register for request
     * @return true if new request was registered, false otherwise
     */
    public boolean registerListenerForRequest(Request request,DrupalClient.OnResponseListener listener,Object tag,boolean skipDuplicateRequestListeners)
    {
        boolean result = false;

        if(listener == null)
        {
            return true;
        }

        List<ListenerHolder> listenersList = listeners.get(request);

        if(listenersList == null)
        {
            listenersList = new LinkedList<ListenerHolder>();
            listeners.put(request,listenersList);
            result = true;
        }else{
            if(!listenersList.isEmpty() && skipDuplicateRequestListeners)
            {
                //We don't add duplicate listners in case of reject policy
                return result;
            }
        }

        listenersList.add(new ListenerHolder(listener,tag));
        return result;
    }

    /**
     *
     * @param request
     * @return Listeners, registered for this request
     */
    protected List<ListenerHolder> getListenersForRequest(Request request)
    {
        return listeners.get(request);
    }

    /**
     * Remove all listeners for request
     * @param request
     */
    public void  removeListenersForRequest(Request request)
    {
        listeners.remove(request);
    }

    public synchronized void removeAllListeners()
    {
        listeners.clear();
    }

    public int registeredRequestCount()
    {
        return listeners.size();
    }

    public static class ListenerHolder
    {
        private DrupalClient.OnResponseListener listener;
        private Object tag;
        public ListenerHolder(DrupalClient.OnResponseListener listener, Object tag)
        {
            this.listener = listener;
            this.tag = tag;
        }

        public DrupalClient.OnResponseListener getListener() {
            return listener;
        }

        public Object getTag() {
            return tag;
        }
    }
}
