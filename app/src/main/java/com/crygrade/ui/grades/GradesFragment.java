package com.crygrade.ui.grades;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.crygrade.R;
import com.crygrade.models.Grade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GradesFragment extends Fragment {

	//private GradesViewModel gradesViewModel;
	private List<String> gradeHeader;
	private HashMap<String, List<Grade>> gradeMap;
	private ExpandableListAdapter<Grade> gradeAdapter;
	private DatabaseReference usersRef, gradesRef;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		//gradesViewModel = new ViewModelProvider(requireActivity()).get(GradesViewModel.class);
		View root = inflater.inflate(R.layout.fragment_grades, container, false);

		final ExpandableListView listView = root.findViewById(R.id.grades_expandable);

		assert FirebaseAuth.getInstance().getUid() != null;
		usersRef = FirebaseDatabase.getInstance().getReference("users");
		gradesRef = usersRef.child(FirebaseAuth.getInstance().getUid()).child("grades");

		loadGrades();
		gradeAdapter = new ExpandableListAdapter<>(getContext(), gradeHeader, gradeMap);
		listView.setAdapter(gradeAdapter);

		return root;
	}

	private void loadGrades() {
		gradeMap = new HashMap<>();
		gradeHeader = new ArrayList<>();

		gradesRef.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				Grade grade = dataSnapshot.getValue(Grade.class);
				assert grade != null;
				addGrade(grade);
			}

			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

			@Override
			public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) { }
		});
	}

	private void addGrade(Grade grade) {
		if(!gradeHeader.contains(grade.getSubject())) {
			gradeHeader.add(grade.getSubject());
			gradeMap.put(grade.getSubject(), new ArrayList<>());
		}
		assert gradeMap.get(grade.getSubject()) != null;
		gradeMap.get(grade.getSubject()).add(grade);
		if(gradeAdapter != null)
			gradeAdapter.notifyDataSetChanged();
	}
}
