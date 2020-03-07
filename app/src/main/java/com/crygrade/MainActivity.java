package com.crygrade;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FirebaseAuth mAuth = FirebaseAuth.getInstance();

		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		BottomNavigationView navView = findViewById(R.id.nav_view);

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference usersRef = database.getReference("users");

		assert mAuth.getCurrentUser() != null;
		FirebaseUser user = mAuth.getCurrentUser();
		String uid = user.getUid();

		usersRef.child(uid).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				String role = dataSnapshot.getValue(String.class);
				if(role == null) {
					mAuth.signOut();
					finish();
					return;
				}
				switch (role) {
					case "teacher":
						navView.inflateMenu(R.menu.nav_menu_teacher);
						break;
					case "admin":
						navView.inflateMenu(R.menu.nav_menu_admin);
						break;
					case "student":
					default:
						navView.inflateMenu(R.menu.nav_menu_student);
				}
				NavigationUI.setupWithNavController(navView, navController);
				findViewById(R.id.main_loading).setVisibility(View.GONE);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) { }
		});
	}
}
