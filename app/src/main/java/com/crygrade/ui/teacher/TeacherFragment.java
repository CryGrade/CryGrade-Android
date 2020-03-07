package com.crygrade.ui.teacher;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.crygrade.R;
import com.crygrade.models.Grade;
import com.crygrade.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class TeacherFragment extends Fragment {

	private TeacherViewModel teacherViewModel;
	private EditText gradeEdit, weightEdit, descEdit;
	private Spinner studentSpinner, categorySpinner;
	private Button addGradeButton;
	private FirebaseAuth mAuth;
	private FirebaseDatabase mDatabase;
	private DatabaseReference usersRef;
	private String subject;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		teacherViewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);
		View root = inflater.inflate(R.layout.fragment_teacher, container, false);

		gradeEdit = root.findViewById(R.id.teacher_gradeEdit);
		weightEdit = root.findViewById(R.id.teacher_weightEdit);
		descEdit = root.findViewById(R.id.teacher_descriptionEdit);
		studentSpinner = root.findViewById(R.id.teacher_studentsSpinner);
		categorySpinner = root.findViewById(R.id.teacher_categorySpinner);
		addGradeButton = root.findViewById(R.id.teacher_addGradeButton);

		mAuth = FirebaseAuth.getInstance();
		mDatabase = FirebaseDatabase.getInstance();
		usersRef = mDatabase.getReference("users");

		assert getContext() != null;
		ArrayList<User> students = new ArrayList<>();
		ArrayAdapter<User> arrayAdapter = new ArrayAdapter<>(getContext(),
				com.google.android.material.R.layout.support_simple_spinner_dropdown_item, students);
		arrayAdapter.setDropDownViewResource(
				com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
		studentSpinner.setAdapter(arrayAdapter);

		teacherViewModel.getStudents().observe(getViewLifecycleOwner(), users -> {
			students.clear();
			users.forEach((s, user) -> {
					//Log.d(s, user.toString());
					students.add(user);
			});
			arrayAdapter.notifyDataSetChanged();
		});

		assert FirebaseAuth.getInstance().getUid() != null;
		usersRef.child(FirebaseAuth.getInstance().getUid()).child("subject")
				.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				subject = dataSnapshot.getValue(String.class);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) { }
		});

		assert teacherViewModel.getStudents().getValue() != null;
		usersRef.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				User user = dataSnapshot.getValue(User.class).setUid(dataSnapshot.getKey());
				if(user.getRole().equals("student"))
					teacherViewModel.getStudents().getValue().put(dataSnapshot.getKey(), user);
				//Log.d("USER", user.toString());
			}

			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				User user = dataSnapshot.getValue(User.class).setUid(dataSnapshot.getKey());
				if(user.getRole().equals("student"))
					teacherViewModel.getStudents().getValue().replace(dataSnapshot.getKey(), user);
			}

			@Override
			public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
				User user = dataSnapshot.getValue(User.class).setUid(dataSnapshot.getKey());
				if(user.getRole().equals("student"))
					teacherViewModel.getStudents().getValue().remove(dataSnapshot.getKey());
			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) { }
		});

		addGradeButton.setOnClickListener(v -> {
			User user = (User) studentSpinner.getSelectedItem();
			if(user == null ||
				weightEdit.getText().length() == 0 ||
				gradeEdit.getText().length() == 0) {
				return;
			}

			gradeEdit.setEnabled(false);
			weightEdit.setEnabled(false);
			descEdit.setEnabled(false);
			studentSpinner.setEnabled(false);
			categorySpinner.setEnabled(false);
			addGradeButton.setEnabled(false);

			Integer g = Integer.valueOf("0"+gradeEdit.getText().toString());
			Integer w = Integer.valueOf("0"+weightEdit.getText().toString());
			String d = descEdit.getText().toString();
			String c = (String) categorySpinner.getSelectedItem();
			Grade grade = new Grade(g, w, c, d);
			String uid = user.getUid();
			grade.setDate(Calendar.getInstance().getTimeInMillis());
			//grade.setCreator(mAuth.getUid());
			grade.setSubject(subject);
			usersRef.child(uid).child("grades").push().setValue(grade)
					.addOnCompleteListener(task -> {
						if(task.isSuccessful())
							Toast.makeText(getContext(), "Pomyślnie dodano ocenę", Toast.LENGTH_SHORT).show();
						else Toast.makeText(getContext(), "Wystąpił błąd", Toast.LENGTH_SHORT).show();

						gradeEdit.setEnabled(true);
						weightEdit.setEnabled(true);
						descEdit.setEnabled(true);
						studentSpinner.setEnabled(true);
						categorySpinner.setEnabled(true);
						addGradeButton.setEnabled(true);
					});
		});

		return root;
	}
}
