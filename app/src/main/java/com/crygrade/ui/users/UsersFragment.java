package com.crygrade.ui.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.crygrade.R;
import com.crygrade.models.User;
import com.crygrade.ui.home.HomeViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class UsersFragment extends Fragment {

	private UsersViewModel usersViewModel;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);
		View root = inflater.inflate(R.layout.fragment_users, container, false);

		usersViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
			for(Map.Entry<String, User> entry : usersViewModel.getUsers().getValue().entrySet()) {
				String key = entry.getKey();
				User user = entry.getValue();

				Log.d("USER", String.format("(%s)%s: %s %s %s",
						key,
						user.getRole(),
						user.getFirstName(),
						user.getLastName(),
						user.getUserClass()
				));
			}
			Log.d("USER", "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		});



		return root;
	}
}
