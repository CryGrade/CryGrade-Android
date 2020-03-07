package com.crygrade.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.crygrade.R;
import com.crygrade.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AdminFragment extends Fragment implements TextWatcher,
		AdapterView.OnItemSelectedListener {

	private AdminViewModel adminViewModel;
	private EditText emailEdit, passwordEdit, firstNameEdit, lastNameEdit, classEdit, subjectEdit;
	private Spinner typeSpinner;
	private Button confirmButton;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		adminViewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);
		View root = inflater.inflate(R.layout.fragment_admin, container, false);

		firstNameEdit = root.findViewById(R.id.admin_firstNameEdit);
		lastNameEdit = root.findViewById(R.id.admin_lastNameEdit);
		emailEdit = root.findViewById(R.id.admin_emailEdit);
		passwordEdit = root.findViewById(R.id.admin_passwordEdit);
		subjectEdit = root.findViewById(R.id.admin_subjectEdit);
		typeSpinner = root.findViewById(R.id.admin_typeSpinner);
		classEdit = root.findViewById(R.id.admin_classEdit);
		confirmButton = root.findViewById(R.id.admin_confirmButton);

		assert adminViewModel.getType().getValue() != null;
		firstNameEdit.setText(adminViewModel.getFirstName().getValue());
		lastNameEdit.setText(adminViewModel.getLastName().getValue());
		emailEdit.setText(adminViewModel.getEmail().getValue());
		passwordEdit.setText(adminViewModel.getPassword().getValue());
		subjectEdit.setText(adminViewModel.getSubject().getValue());
		classEdit.setText(adminViewModel.getUserClass().getValue());
		typeSpinner.setSelection(adminViewModel.getType().getValue());

		emailEdit.addTextChangedListener(this);
		passwordEdit.addTextChangedListener(this);
		firstNameEdit.addTextChangedListener(this);
		lastNameEdit.addTextChangedListener(this);
		classEdit.addTextChangedListener(this);
		typeSpinner.setOnItemSelectedListener(this);
		confirmButton.setOnClickListener(this::createUser);

		return root;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) { }

	@Override
	public void afterTextChanged(Editable s) {
		adminViewModel.getFirstName().setValue(firstNameEdit.getText().toString());
		adminViewModel.getLastName().setValue(lastNameEdit.getText().toString());
		adminViewModel.getEmail().setValue(emailEdit.getText().toString());
		adminViewModel.getPassword().setValue(passwordEdit.getText().toString());
		adminViewModel.getUserClass().setValue(classEdit.getText().toString());
		adminViewModel.getSubject().setValue(subjectEdit.getText().toString());
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		adminViewModel.getType().setValue(position);
		if(position == 0) {
			classEdit.setVisibility(View.VISIBLE);
			subjectEdit.setVisibility(View.INVISIBLE);
		} else if(position == 1) {
			subjectEdit.setVisibility(View.VISIBLE);
			classEdit.setVisibility(View.INVISIBLE);
		} else {
			classEdit.setVisibility(View.INVISIBLE);
			subjectEdit.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) { }

	private void createUser(View v) {
		String firstName = "" + adminViewModel.getFirstName().getValue();
		String lastName = "" + adminViewModel.getLastName().getValue();
		String email = "" + adminViewModel.getEmail().getValue();
		String password = "" + adminViewModel.getPassword().getValue();
		String role = "";

		assert adminViewModel.getType().getValue() != null;
		switch (adminViewModel.getType().getValue()) {
			case 0:
				role = "student";
				break;
			case 1:
				role = "teacher";
				break;
			case 2:
				role = "admin";
		}

		User user = new User(role, firstName, lastName);
		if(role.equals("student"))
			user.setUserClass(adminViewModel.getUserClass().getValue());

		firstNameEdit.setEnabled(false);
		lastNameEdit.setEnabled(false);
		emailEdit.setEnabled(false);
		passwordEdit.setEnabled(false);
		classEdit.setEnabled(false);
		subjectEdit.setEnabled(false);
		typeSpinner.setEnabled(false);
		confirmButton.setEnabled(false);

		FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
				.setApiKey("AIzaSyCYFn4_XhX8JHQhlorF6MySRJBJ-jH8Lfs")
				.setDatabaseUrl("https://crygrade.firebaseio.com")
				.setProjectId("crygrade")
				.setApplicationId("1:690806186409:web:a4bf12772d04f0058764b8")
				.build();
		assert getContext() != null;
		FirebaseApp newAuth = FirebaseApp.initializeApp(getContext(), firebaseOptions,
				String.valueOf(Calendar.getInstance().getTimeInMillis()));

		FirebaseAuth.getInstance(newAuth).createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(task -> {
					if(task.isSuccessful()) {
						assert task.getResult() != null;
						assert task.getResult().getUser() != null;
						String uid = task.getResult().getUser().getUid();
						assert FirebaseAuth.getInstance().getUid() != null;
						FirebaseDatabase.getInstance().getReference("users")
								.child(uid).setValue(user);
						if(user.getRole().equals("teacher"))
							  FirebaseDatabase.getInstance().getReference("users").child(uid)
						  			.child("subject").setValue(subjectEdit.getText().toString());

						Toast.makeText(getContext(), "Pomyślnie utworzono konto",
								Toast.LENGTH_SHORT).show();
						emailEdit.setText("");
						passwordEdit.setText("");
						firstNameEdit.setText("");
						lastNameEdit.setText("");
						classEdit.setText("");
						subjectEdit.setText("");
						typeSpinner.setSelection(0);
					} else {
						Toast.makeText(getContext(), "Nie udało się utworzyć konta",
								Toast.LENGTH_SHORT).show();
					}
					firstNameEdit.setEnabled(true);
					lastNameEdit.setEnabled(true);
					emailEdit.setEnabled(true);
					passwordEdit.setEnabled(true);
					classEdit.setEnabled(true);
					subjectEdit.setEnabled(true);
					typeSpinner.setEnabled(true);
					confirmButton.setEnabled(true);
		});
	}
}
