package com.dan.criminalint;

import java.util.ArrayList;
import java.util.UUID;


import android.content.Context;
import android.util.Log;

public class CrimeLab {
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	private CriminalIntentJSONSerializer mSerializer;
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	private ArrayList<Crime> mCrimes;

	private CrimeLab(Context appContext) {
		// TODO Auto-generated constructor stub
		mAppContext = appContext;
		mSerializer = new CriminalIntentJSONSerializer(appContext, FILENAME);
		try {
			mCrimes = mSerializer.loadCrimes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			mCrimes = new ArrayList<Crime>();
			Log.e(TAG, "Error loading crimes: " + e);
			e.printStackTrace();
		}
	}

	public static CrimeLab get(Context c) {
		if (sCrimeLab == null)
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		return sCrimeLab;
	}

	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}

	public Crime getCrime(UUID id) {
		for (Crime c : mCrimes) {
			if (c.getId().equals(id))
				return c;
		}
		return null;
	}

	public void addCrime(Crime c) {
		mCrimes.add(c);
	}
	public void deleteCrime(Crime c)
	{
		mCrimes.remove(c);
	}
	public boolean saveCrimes() {
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "crimes saved to file");
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "error saving crimes: " + e);
			return false;
		}
	}
}
