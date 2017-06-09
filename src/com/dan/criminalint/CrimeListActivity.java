package com.dan.criminalint;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

	public CrimeListActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new CrimeListFragment();
	}

}
