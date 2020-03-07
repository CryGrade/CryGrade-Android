package com.crygrade.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

	private MutableLiveData<String> mText;

	public HomeViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("To nie działa");
	}

	public LiveData<String> getText() {
		return mText;
	}
}