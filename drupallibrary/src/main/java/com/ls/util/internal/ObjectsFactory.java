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

package com.ls.util.internal;

import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ObjectsFactory
{
	public static Object newInstance(Class<?> theClass)
	{
		Method newInstance;
		try
		{
			newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
			newInstance.setAccessible(true);
			Object result =  newInstance.invoke(null, theClass, Object.class);
			return result;
		} catch (NoSuchMethodException e)
		{			
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{			
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{			
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
//	public static Object newInstance(Class<?> theClass)
//	{
//		Objenesis objenesis = new ObjenesisStd();
//		ObjectInstantiator instantiator = objenesis.getInstantiatorOf(theClass);
//		Object result = instantiator.newInstance();
//		Log.d("ObjectsFactory","Created instance:"+result.toString());
//		return result;
//	}
}
