package com.crygrade.ui.admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdminViewModel extends ViewModel {

	private MutableLiveData<String> mFirstName;
	private MutableLiveData<String> mLastName;
	private MutableLiveData<String> mEmail;
	private MutableLiveData<String> mPassword;
	private MutableLiveData<Integer> mType;
	private MutableLiveData<String> mClass;

	private MutableLiveData<String> mSubject;

	public AdminViewModel() {
		mFirstName = new MutableLiveData<>();
		mLastName = new MutableLiveData<>();
		mEmail = new MutableLiveData<>();
		mPassword = new MutableLiveData<>();
		mType = new MutableLiveData<>();
		mClass = new MutableLiveData<>();
		mSubject = new MutableLiveData<>();
	}

	public MutableLiveData<String> getFirstName() {
		return mFirstName;
	}

	public MutableLiveData<String> getLastName() {
		return mLastName;
	}

	public MutableLiveData<String> getEmail() {
		return mEmail;
	}

	public MutableLiveData<String> getPassword() {
		return mPassword;
	}

	public MutableLiveData<Integer> getType() {
		if(mType.getValue() == null)
			mType.setValue(0);
		return mType;
	}

	public MutableLiveData<String> getUserClass() {
		return mClass;
	}

	public MutableLiveData<String> getSubject() {
		return mSubject;
	}
}