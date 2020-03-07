package com.crygrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

	private FirebaseAuth mAuth;
	private Button loginButton;
	private ProgressBar progressBar;
	private EditText emailEdit, passwordEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mAuth = FirebaseAuth.getInstance();

		loginButton = findViewById(R.id.login_buttonLogin);
		progressBar = findViewById(R.id.login_progress);
		emailEdit = findViewById(R.id.login_editEmail);
		passwordEdit = findViewById(R.id.login_editPassword);

		loginButton.setOnClickListener(this);
		emailEdit.addTextChangedListener(this);
		passwordEdit.addTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

	@Override
	public void afterTextChanged(Editable s) { }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String email = emailEdit.getText().toString();
		String password = passwordEdit.getText().toString();

		if(!email.isEmpty() && password.length() >= 8)
			loginButton.setEnabled(true);
		else loginButton.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		if(v == findViewById(R.id.login_buttonLogin)) {
			String email = emailEdit.getText().toString();
			String password = passwordEdit.getText().toString();

			emailEdit.setEnabled(false);
			passwordEdit.setEnabled(false);
			loginButton.setEnabled(false);
			progressBar.setVisibility(View.VISIBLE);

			login(email, password);
		}
	}

	private void login(String email, String password) {
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(task -> {
					if(task.isSuccessful()) {
						Intent intent = new Intent(this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						emailEdit.setEnabled(true);
						passwordEdit.setEnabled(true);
						loginButton.setEnabled(true);
						progressBar.setVisibility(View.GONE);
						Toast.makeText(this, "Nie udało się zalogować!", Toast.LENGTH_SHORT).show();
					}
				});
	}
}
