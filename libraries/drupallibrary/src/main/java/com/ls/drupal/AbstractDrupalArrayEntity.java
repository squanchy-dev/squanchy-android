

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

import com.ls.http.base.ResponseData;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractDrupalArrayEntity<E> extends AbstractDrupalEntity implements Collection<E>
{
	private transient final ArrayList<E> innerItems;

	public AbstractDrupalArrayEntity(DrupalClient client, int itemCount)
	{
		super(client);
		innerItems = new ArrayList<E>(itemCount);
	}

	@Override
	public boolean add(E object)
	{
		return innerItems.add(object);
	}

	@Override
	public boolean addAll(Collection<? extends E> collection)
	{
		return this.innerItems.addAll(collection);
	}

	@Override
	public void clear()
	{
		this.innerItems.clear();
	}

	@Override
	public boolean contains(Object object)
	{
		return innerItems.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection)
	{
		return innerItems.containsAll(collection);
	}

	@Override
	public boolean isEmpty()
	{
		return innerItems.isEmpty();
	}

	@Override
	public Iterator<E> iterator()
	{
		return innerItems.iterator();
	}

	@Override
	public boolean remove(Object object)
	{
		return innerItems.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> collection)
	{
		return innerItems.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection)
	{
		return innerItems.retainAll(collection);
	}

	@Override
	public int size()
	{
		return innerItems.size();
	}

	@Override
	public Object[] toArray()
	{
		return innerItems.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array)
	{
		return innerItems.toArray(array);
	}

	/**
	 * Returns the element at the specified location in this list.
	 * 
	 * @param index
	 *            the index of the element to return.
	 * @return the element at the specified index.
	 */
	public E get(int index)
	{
		return innerItems.get(index);
	}

	/**
	 * Replaces the element at the specified location in this ArrayList with the
	 * specified object.
	 * 
	 * @param index
	 * @param object
	 * @return the previous element at the index.
	 * @throws IndexOutOfBoundsException
	 *             - when location < 0 || location >= size()
	 */
	public E set(int index, E object)
	{
		return innerItems.set(index, object);
	}

	/**
	 * Removes the object at the specified location from this list.
	 * 
	 * @param index
	 *            the index of the object to remove.
	 * @return the removed object.
	 * @throws IndexOutOfBoundsException
	 *             - when location < 0 || location >= size()
	 */
	public E remove(int index)
	{
		return innerItems.remove(index);
	}

	// Replacing this with items set

	@SuppressWarnings("null")
	@Override
	public @NonNull
    Object getManagedData()
	{
		return this.innerItems;
	}

	@Override
	protected void consumeObject(ResponseData entity)
	{	
		@SuppressWarnings("unchecked")
		E[] items = (E[]) entity.getData();
		for (E item : items)
		{
			this.add(item);
		}
	}

	@Override
	protected Object getManagedDataClassSpecifyer()
	{
		Class<?> itemsArrayClass = this.getClass();
		Type classType = null;

		while (classType == null)
		{
			if (itemsArrayClass.getSuperclass().equals(AbstractDrupalArrayEntity.class))
			{
				classType = itemsArrayClass.getGenericSuperclass();
			}
			itemsArrayClass = itemsArrayClass.getSuperclass();
		}

		Type genericArgType = ((ParameterizedType) classType).getActualTypeArguments()[0];


			if(genericArgType instanceof Class)
			{
				Class<?> genericArgClass = (Class<?>) (genericArgType);
				@SuppressWarnings("unchecked")
				E[] array = (E[]) java.lang.reflect.Array.newInstance(genericArgClass, 0);
				return array.getClass();	
			}else{
				throw new IllegalArgumentException(AbstractDrupalArrayEntity.class.getName()+" doesn't support RAW types deserialization");
			}
	}
}
