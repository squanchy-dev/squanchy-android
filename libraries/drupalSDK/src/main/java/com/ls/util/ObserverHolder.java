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

package com.ls.util;

import java.util.LinkedList;
import java.util.List;

public class ObserverHolder<ObserverClass> {
    protected List<ObserverClass> observers;

    public ObserverHolder() {
        observers = new LinkedList<ObserverClass>();
    }

    /**
     * @param theObserver
     * @return false if observer was already registered
     */
    public synchronized boolean registerObserver(ObserverClass theObserver) {
        boolean isAlreadyRegistered = false;
        for (ObserverClass observer : observers) {
            if (observer == theObserver) {
                isAlreadyRegistered = true;
                break;
            }
        }

        if (!isAlreadyRegistered) {
            this.observers.add(theObserver);
        }
        return !isAlreadyRegistered;
    }

    public synchronized void unregisterObserver(ObserverClass theObserver) {
        this.observers.remove(theObserver);
    }

    public synchronized void clearAll() {
        this.observers.clear();
    }

    public synchronized void notifyAllObservers(ObserverNotifier<ObserverClass> notifier) {
        List<ObserverClass> observersCopy = new LinkedList<ObserverClass>(observers);
        for (ObserverClass observer : observersCopy) {
            if (observer != null) {
                notifier.onNotify(observer);
            }
        }
    }

    public static interface ObserverNotifier<ObserverClass> {
        public void onNotify(ObserverClass observer);
    }
}
