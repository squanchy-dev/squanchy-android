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


import com.ls.util.ObserverHolder;

/**
 * Created by Lemberg-i5 on 07.10.2014.
 * Note: this isn't the best implementation of singleton but it's enough for our application
 */
public class ConnectionManager {
    private static ConnectionManager instance = new ConnectionManager();

    private ObserverHolder<OnConnectionStateChangedObserver> connectionObservers;

    public boolean connected;

    public static ConnectionManager instance()
    {
        return instance;
    }

    private ConnectionManager()
    {
        this.connected = true;
        connectionObservers = new ObserverHolder<OnConnectionStateChangedObserver>();
    }

    public void registerObserver(OnConnectionStateChangedObserver observer)
    {
        if(connectionObservers.registerObserver(observer)){
            observer.onConnectionStateChanged(connected);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(final boolean connected) {
        if(this.connected != connected) {
            this.connected = connected;
            this.connectionObservers.notifyAllObservers(new ObserverHolder.ObserverNotifier<OnConnectionStateChangedObserver>() {
                @Override
                public void onNotify(OnConnectionStateChangedObserver observer) {
                    observer.onConnectionStateChanged(connected);
                }
            });
        }
    }

    public void unregisterObserver(OnConnectionStateChangedObserver observer)
   {
        connectionObservers.unregisterObserver(observer);
   }

    public static interface OnConnectionStateChangedObserver
    {
        public void onConnectionStateChanged(boolean connectionPresent);
    }

}
