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

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lemberg-i5 on 07.10.2014.
 */
public class ObserverHolderWeak<ObserverClass> {
    protected List<WeakReference<ObserverClass>> observers;

    public ObserverHolderWeak()
    {
        observers = new LinkedList<WeakReference<ObserverClass>>();
    }

    /**
     *
     * @param theObserver
     * @return false if observer was already registered
     */
    public synchronized boolean registerObserver(ObserverClass theObserver)
    {
        List<WeakReference<ObserverClass>> toRemove = new LinkedList<WeakReference<ObserverClass>>();

        boolean isAlreadyRegistered = false;
        for(WeakReference<ObserverClass> observerRef:observers)
        {
            ObserverClass observer = observerRef.get();
            if(observer != null)
            {
                if(observer==theObserver)
                {
                    isAlreadyRegistered = true;
                }
            }else{
                toRemove.add(observerRef);
            }
        }
        this.observers.removeAll(toRemove);

        if(!isAlreadyRegistered)
        {
            this.observers.add(new WeakReference<ObserverClass>(theObserver));
        }
        return !isAlreadyRegistered;
    }

    public synchronized void unregisterObserver(ObserverClass theObserver)
    {
        List<WeakReference<ObserverClass>> toRemove = new LinkedList<WeakReference<ObserverClass>>();

        for(WeakReference<ObserverClass> observerRef:observers)
        {
            ObserverClass observer = observerRef.get();
            if(observer == null || observer == theObserver)
            {
                toRemove.add(observerRef);
            }
        }
        this.observers.removeAll(toRemove);
    }

    public synchronized void clearAll()
    {
        this.observers.clear();
    }

    public synchronized void notifyAllObservers(ObserverNotifier<ObserverClass> notifier)
    {
        for(WeakReference<ObserverClass> observerRef:observers)
        {
            ObserverClass observer = observerRef.get();
            if(observer != null)
            {
                notifier.onNotify(observer);
            }
        }
    }

   public static interface ObserverNotifier<ObserverClass>
   {
       public void onNotify(ObserverClass observer);
   }
}
