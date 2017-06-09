package com.dan.criminalint;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class IntentUtils {
	public static boolean isIntentValid(Context c,Intent i)
	{
		PackageManager pm=c.getPackageManager();
		List<ResolveInfo> activities=pm.queryIntentActivities(i, 0);
		return activities.size()>0;
	}
}
