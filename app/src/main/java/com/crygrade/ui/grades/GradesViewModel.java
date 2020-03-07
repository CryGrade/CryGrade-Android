package com.crygrade.ui.grades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GradesViewModel extends ViewModel {

	private MutableLiveData<String> mText;

	public GradesViewModel() {
		mText = new MutableLiveData<>();

	}

	public LiveData<String> getText() {
		return mText;
	}
}