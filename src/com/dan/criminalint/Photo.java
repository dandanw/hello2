package com.dan.criminalint;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
	private final static String JSON_FILENAME="filename";
	private final static String JSON_ORITATIOIN="oritation";
	private String mFileName;
	private int mOritation;
	public Photo(String filename) {
		// TODO Auto-generated constructor stub
		mFileName=filename;
		
	}
	public Photo(JSONObject json) throws JSONException {
		// TODO Auto-generated constructor stub
		mFileName=json.getString(JSON_FILENAME);
		mOritation=json.getInt(JSON_ORITATIOIN);
	}
	public JSONObject toJson() throws JSONException
	{
		JSONObject json=new JSONObject();
		json.put(JSON_FILENAME, mFileName);
		if(mOritation!=0)
			json.put(JSON_ORITATIOIN, mOritation);
		return json;
	}
	public String getFileName() {
		return mFileName;
	}
	public int getOritation() {
		return mOritation;
	}
	public void setOritation(int oritation) {
		this.mOritation = oritation;
	}
	
	
}
