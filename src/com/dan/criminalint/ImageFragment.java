package com.dan.criminalint;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
	public final static String IMAGE_PATH="image_path";
	public final static String IMAGE_ORITATION="image_oritation";
	private ImageView mImageView;
	public static ImageFragment newInstance(String path,int oritation)
	{
		Bundle args=new Bundle();
		args.putSerializable(IMAGE_PATH, path);
		args.putInt(IMAGE_ORITATION, oritation);
		ImageFragment fragment=new ImageFragment();
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return fragment;
		
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mImageView=new ImageView(getActivity());
		String path=(String) getArguments().getSerializable(IMAGE_PATH);
		int oritation=getArguments().getInt(IMAGE_ORITATION);
		BitmapDrawable image=PictureUtils.getScaledDrawable(getActivity(), path, oritation);
		mImageView.setImageDrawable(image);
		return mImageView;
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		PictureUtils.cleanImageView(mImageView);
	}
	
}
