package com.crygrade.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {

	private MutableLiveData<String> mFullName;
	private MutableLiveData<String> mFirstName;
	private MutableLiveData<String> mLastName;
	private MutableLiveData<String> mType;
	private MutableLiveData<String> mUserClass;
	private MutableLiveData<String> mNewPass;
	private MutableLiveData<String> mOldPass;

	public AccountViewModel() {
		mFullName = new MutableLiveData<>();
		mFirstName = new MutableLiveData<>();
		mLastName = new MutableLiveData<>();
		mType = new MutableLiveData<>();
		mUserClass = new MutableLiveData<>();
		mNewPass = new MutableLiveData<>();
		mOldPass = new MutableLiveData<>();
	}

	public MutableLiveData<String> getFullName() {
		return mFullName;
	}

	public MutableLiveData<String> getFirstName() {
		return mFirstName;
	}

	public MutableLiveData<String> getLastName() {
		return mLastName;
	}

	public MutableLiveData<String> getType() {
		return mType;
	}

	public MutableLiveData<String> getUserClass() {
		return mUserClass;
	}

	public MutableLiveData<String> getNewPass() {
		return mNewPass;
	}

	public MutableLiveData<String> getOldPass() {
		return mOldPass;
	}
}