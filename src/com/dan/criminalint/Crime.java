package com.dan.criminalint;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	private static final String JSON_PHOTO = "photo";
	private static final String JSON_SUSPECT = "suspect";
	private static final String JSON_SUSPECT_PHONE="phone";
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private Photo mPhoto;
	private String mSuspect;
	private String mSuspectPhone;
	public String getSuspectPhone() {
		return mSuspectPhone;
	}

	public void setSuspectPhone(String suspectPhone) {
		mSuspectPhone = suspectPhone;
	}

	public Crime() {
		// TODO Auto-generated constructor stub
		mId = UUID.randomUUID();
		mDate = new Date();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return mTitle;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	public UUID getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Photo getmPhoto() {
		return mPhoto;
	}

	public void setPhoto(Photo p) {
		mPhoto = p;
	}


	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		json.put(JSON_SUSPECT, mSuspect);
		json.put(JSON_SUSPECT_PHONE, mSuspectPhone);
		if(mPhoto!=null)
			json.put(JSON_PHOTO, mPhoto.toJson());
		return json;

	}
	public String getSuspect() {
		return mSuspect;
	}

	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}

	public Crime (JSONObject json) throws JSONException
	{
		mId=UUID.fromString(json.getString(JSON_ID));
		if(json.has(JSON_TITLE))
			mTitle=json.getString(JSON_TITLE);
		if(json.has(JSON_PHOTO))
			mPhoto=new Photo(json.getJSONObject(JSON_PHOTO));
		if(json.has(JSON_SOLVED))
		mSolved=json.getBoolean(JSON_SOLVED);
		if(json.has(JSON_DATE))
		mDate=new Date(json.getLong(JSON_DATE));
		if(json.has(JSON_SUSPECT))
		mSuspect=json.getString(JSON_SUSPECT);
		if(json.has(JSON_SUSPECT_PHONE))
			mSuspectPhone=json.getString(JSON_SUSPECT_PHONE);
	}
	
}
