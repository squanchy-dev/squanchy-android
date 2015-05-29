package com.ls.drupalconapp.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import org.json.JSONObject;

import java.io.StringReader;
import java.lang.reflect.Type;

public abstract class JsonSerializable {

	public String toJson() {
		return toJson(this);
	}

	public JsonElement toJsonTree() {
		Gson gson = new Gson();
		return gson.toJsonTree(this);
	}

	public static <T> T fromJson(Class<T> classOfT, String json) {
		return fromJson((Type) classOfT, json);
	}

	public static <T> T fromJson(Type type, String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}

		String jsonInner = json.trim();

		T result = null;

		Gson gson = new Gson();

		try {
			result = gson.fromJson(jsonInner, type);
		} catch (Exception e) {
			e.printStackTrace();
			//json string can include extra BOM symbol, we trying to exclude this symbol

			//get starting position of json object
			int jBeginPosition = jsonInner.indexOf("{");

			//+1 - method substring returns string containing the characters from start to end - 1
			int jLastPosition = jsonInner.lastIndexOf("}") + 1;

			if (jBeginPosition >= 0 && jLastPosition >= 0) {
				if (jBeginPosition <= jLastPosition && jLastPosition <= jsonInner.length()) {
					jsonInner = jsonInner.substring(jBeginPosition, jLastPosition);

					try {
						result = gson.fromJson(jsonInner, type);
					} catch (Exception eIn1) {
						eIn1.printStackTrace();

						try {
							JsonReader lenientJsonReader = createLenientJsonReader(jsonInner);
							result = gson.fromJson(lenientJsonReader, type);
						} catch (Exception eIn2) {
							eIn2.printStackTrace();
						}
					}
				}
			}
		}

		return result;
	}

	private static JsonReader createLenientJsonReader(String json) {
		StringReader reader = new StringReader(json);

		JsonReader jsonReader = new JsonReader(reader);
		jsonReader.setLenient(true);

		return jsonReader;
	}

	public static <T> T fromJson(Class<T> classOfT, JSONObject json) {
		return fromJson(classOfT, json.toString());
	}

	public static String toJson(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
}

