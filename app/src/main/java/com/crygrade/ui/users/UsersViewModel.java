package com.crygrade.ui.users;

import android.icu.lang.UScript;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.crygrade.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class UsersViewModel extends ViewModel {

	private MutableLiveData<HashMap<String, User>> mUsers;

	private FirebaseAuth mAuth;
	private FirebaseUser mUser;
	private FirebaseDatabase mDatabase;
	private DatabaseReference mUsersRef;

	public UsersViewModel() {
		mUsers = new MutableLiveData<>();

		mAuth = FirebaseAuth.getInstance();
		mUser = mAuth.getCurrentUser();
		mDatabase = FirebaseDatabase.getInstance();
		mUsersRef = mDatabase.getReference("users");
		mUsers.setValue(new HashMap<>());
		fetchUsers();
	}

	private void fetchUsers() {
		assert mUsers.getValue() != null;
		mUsersRef.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				mUsers.getValue().put(dataSnapshot.getKey(), dataSnapshot.getValue(User.class));
			}

			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				mUsers.getValue().remove(dataSnapshot.getKey());
				mUsers.getValue().put(dataSnapshot.getKey(), dataSnapshot.getValue(User.class));
			}

			@Override
			public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
				mUsers.getValue().remove(dataSnapshot.getKey());
			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) { }
		});
	}

	public LiveData<HashMap<String, User>> getUsers() {
		return mUsers;
	}
}