package com.ls.drupalconapp.model.http;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class DCJsonObjectRequest extends JsonObjectRequest {

	public DCJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	public DCJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			return super.parseNetworkResponse(response);
		} catch (Exception e) {
			return Response.error(new ParseError());
		}
	}
}
