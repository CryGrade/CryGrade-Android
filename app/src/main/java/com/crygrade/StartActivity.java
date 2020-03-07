package com.crygrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		mAuth = FirebaseAuth.getInstance();
	}

	@Override
	protected void onStart() {
		super.onStart();

		FirebaseUser user = mAuth.getCurrentUser();
		new Handler().postDelayed(() -> {
			Intent intent = new Intent(this, MainActivity.class); //MainActivity
			if(user == null)
				intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}, 1000);
	}
}
