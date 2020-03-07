package com.crygrade.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.crygrade.R;
import com.crygrade.StartActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AccountFragment extends Fragment implements View.OnClickListener, Observer<String>,
		TextWatcher {

	private AccountViewModel accountViewModel;
	private Button changePassButton;
	private EditText oldPassEdit, newPassEdit;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
		View root = inflater.inflate(R.layout.fragment_account, container, false);

		final TextView nameEdit = root.findViewById(R.id.account_nameText);
		final TextView typeEdit = root.findViewById(R.id.account_typeText);
		oldPassEdit = root.findViewById(R.id.account_passwordCurrent);
		newPassEdit = root.findViewById(R.id.account_passwordNew);
		changePassButton = root.findViewById(R.id.account_changeButton);

		root.findViewById(R.id.account_logoutButton).setOnClickListener(this);
		changePassButton.setOnClickListener(this);

		accountViewModel.getFullName().observe(getViewLifecycleOwner(), nameEdit::setText);
		accountViewModel.getType().observe(getViewLifecycleOwner(), s -> {
			switch (s) {
				case "student":
					typeEdit.setText(getResources().getStringArray(R.array.account_types)[0]);
					break;
				case "teacher":
					typeEdit.setText(getResources().getStringArray(R.array.account_types)[1]);
					break;
				case "admin":
					typeEdit.setText(getResources().getStringArray(R.array.account_types)[2]);
			}
		});

		accountViewModel.getFirstName().observe(getViewLifecycleOwner(), this);
		accountViewModel.getLastName().observe(getViewLifecycleOwner(), this);
		accountViewModel.getUserClass().observe(getViewLifecycleOwner(), this);

		oldPassEdit.addTextChangedListener(this);
		newPassEdit.addTextChangedListener(this);

		return root;
	}

	@Override
	public void onResume() {
		assert FirebaseAuth.getInstance().getCurrentUser() != null;
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		String uid = user.getUid();
		FirebaseDatabase.getInstance().getReference("users").child(uid)
				.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				accountViewModel.getType().setValue(dataSnapshot.child("role").getValue(String.class));
				accountViewModel.getFirstName().setValue(dataSnapshot.child("firstName").getValue(String.class));
				accountViewModel.getLastName().setValue(dataSnapshot.child("lastName").getValue(String.class));
				accountViewModel.getUserClass().setValue(dataSnapshot.child("userClass").getValue(String.class));
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) { }
		});
		super.onResume();
	}

	@Override
	public void onChanged(String s) {
		if(Objects.equals(accountViewModel.getType().getValue(), "student"))
			accountViewModel.getFullName().setValue(String.format("%s %s, %s",
					accountViewModel.getFirstName().getValue(),
					accountViewModel.getLastName().getValue(),
					accountViewModel.getUserClass().getValue()
			));
		else
			accountViewModel.getFullName().setValue(String.format("%s %s",
					accountViewModel.getFirstName().getValue(),
					accountViewModel.getLastName().getValue()
			));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.account_logoutButton:
				FirebaseAuth.getInstance().signOut();
				startActivity(new Intent(getContext(), StartActivity.class));
				assert getActivity() != null;
				getActivity().finish();
			case R.id.account_changeButton:
				changePassword();
		}
	}

	private void changePassword() {
		String oldPass = "" + accountViewModel.getOldPass().getValue();
		String newPass = "" + accountViewModel.getNewPass().getValue();

		if(oldPass.isEmpty()) {
			oldPassEdit.setError("Hasło nie może być puste");
			return;
		}
		if(newPass.isEmpty()) {
			newPassEdit.setError("Hasło nie może być puste");
			return;
		}
		if (oldPass.equals(newPass)) {
			newPassEdit.setError("Hasła są takie same");
			return;
		}

		newPassEdit.setEnabled(false);
		oldPassEdit.setEnabled(false);
		changePassButton.setEnabled(false);

		assert FirebaseAuth.getInstance().getCurrentUser() != null;
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		String email = "" + user.getEmail();
		AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass);
		user.reauthenticate(credential).addOnCompleteListener(task -> {
			if(task.isSuccessful()) {
				oldPassEdit.setError(null);
				user.updatePassword(newPass).addOnCompleteListener(task1 -> {
					if(task1.isSuccessful()) {
						newPassEdit.setError(null);
						Toast.makeText(getContext(), "Pomyślnie zmieniono hasło",
								Toast.LENGTH_SHORT).show();
						newPassEdit.setText("");
						oldPassEdit.setText("");
					} else {
						newPassEdit.setError("Nieprawidłowe hasło");
					}
					newPassEdit.setEnabled(true);
					oldPassEdit.setEnabled(true);
					changePassButton.setEnabled(true);
				});
			} else {
				oldPassEdit.setError("Nieprawidłowe hasło");
				newPassEdit.setEnabled(true);
				oldPassEdit.setEnabled(true);
				changePassButton.setEnabled(true);
			}
		});
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) { }

	@Override
	public void afterTextChanged(Editable s) {
		accountViewModel.getNewPass().setValue(newPassEdit.getText().toString());
		accountViewModel.getOldPass().setValue(oldPassEdit.getText().toString());
	}
}
