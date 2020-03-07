package com.crygrade.ui.teacher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.crygrade.models.User;

import java.util.HashMap;

public class TeacherViewModel extends ViewModel {

	private MutableLiveData<HashMap<String, User>> mStudents;

	public TeacherViewModel() {
		mStudents = new MutableLiveData<>();
		if(mStudents.getValue() == null)
			mStudents.setValue(new HashMap<>());
	}

	public MutableLiveData<HashMap<String, User>> getStudents() {
		return mStudents;
	}

}