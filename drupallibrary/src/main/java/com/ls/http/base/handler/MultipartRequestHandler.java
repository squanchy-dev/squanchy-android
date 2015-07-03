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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.ls.http.base.IPostableItem;
import com.ls.http.base.RequestHandler;
import com.ls.http.base.handler.multipart.IMultiPartEntityPart;
import com.ls.util.L;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

class MultipartRequestHandler extends RequestHandler
{

    private MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    private HttpEntity httpentity;


    public MultipartRequestHandler() {
        this.entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }

    @Override
    public void setObject(Object object) {
        super.setObject(object);
        formMultipartEntity(object);
    }

    private void formMultipartEntity(Object source)
    {
        Class<?> currentClass = source.getClass();
        while (!Object.class.equals(currentClass))
        {
            Field[] fields = currentClass.getDeclaredFields();
            for (int counter = 0; counter < fields.length; counter++)
            {
                Field field = fields[counter];
                Expose expose = field.getAnnotation(Expose.class);
                if (expose != null && !expose.deserialize() || Modifier.isTransient(field.getModifiers()))
                {
                    continue;// We don't have to copy ignored fields.
                }
                field.setAccessible(true);
                Object value;

                String name;
                SerializedName serializableName = field.getAnnotation(SerializedName.class);
                if(serializableName != null)
                {
                    name = serializableName.value();
                }else{
                    name = field.getName();
                }
                try
                {
                    value = field.get(source);
                   addEntity(name,value);
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                } catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        httpentity = entity.build();
    }

    private void addEntity(String name, Object value ) throws UnsupportedEncodingException {
        if(value != null)
        {
            if(value instanceof IMultiPartEntityPart) {
                ContentBody body = ((IMultiPartEntityPart)value).getContentBody();
                entity.addPart(name,body);
            }else{
                if(value instanceof List)
                {
                    for(Object item:(List)value)
                    {
                       addEntity(name+"[]",item);
                    }
                    return;
                }

                if(value.getClass().isArray())
                {
                    Object[]array = (Object[])value;
                    for(int counter = 0;counter < array.length; counter++)
                    {
                        Object item = array[counter];
                        if(item != null)
                        {
                            addEntity(name+"[]",item);
                        }
                    }
                    return;
                }

                entity.addTextBody(name,value.toString());
            }
        }
    }

    @Override
	public String stringBodyFromItem()
	{
		if(implementsPostableInterface())
		{
			IPostableItem item = (IPostableItem)this.object;
			return item.toPlainText();
		}else{
			return this.object.toString();
		}
	}

    @Override
    public String getBodyContentType(String defaultCharset) {
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody(String defaultCharset) throws UnsupportedEncodingException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity.writeTo(bos);
        } catch (IOException e) {
            L.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
}
