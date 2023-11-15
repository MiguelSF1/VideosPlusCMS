package com.example.videospluscms.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.videospluscms.fragment.MovieVersionsFragment;
import com.example.videospluscms.fragment.MoviesFragment;
import com.example.videospluscms.R;
import com.example.videospluscms.fragment.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new UsersFragment()).commit();
    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment;

        int itemId = item.getItemId();
        if (itemId == R.id.nav_users) {
            selectedFragment = new UsersFragment();
        } else if (itemId == R.id.nav_movies) {
            selectedFragment = new MoviesFragment();
        } else {
            selectedFragment = new MovieVersionsFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();

        return true;
    };
}