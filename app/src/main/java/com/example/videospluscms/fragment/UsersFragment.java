package com.example.videospluscms.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.videospluscms.R;
import com.example.videospluscms.adapter.UserListAdapter;
import com.example.videospluscms.object.User;
import com.example.videospluscms.object.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;

    public UsersFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(v -> {
            UserDialogFragment userDialogFragment = new UserDialogFragment(getActivity());
            userDialogFragment.show(getActivity().getSupportFragmentManager(), "User Information");
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       getUsers();

    }

    private void getUsers() {
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest usersStringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.103:8080/api/users", response -> {
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> users = new Gson().fromJson(response, listType);
            UserListAdapter userListAdapter = new UserListAdapter(users);
            recyclerView.setAdapter(userListAdapter);
        }, error -> Log.d("failure", "sendRequestUsers: Failed "));

        requestQueue.add(usersStringRequest);
    }
}
