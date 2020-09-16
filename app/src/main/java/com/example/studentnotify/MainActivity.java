package com.example.studentnotify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.studentnotify.ui.main.BlankFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentnotify.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private int logTo;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.logout_btn);

        logTo = getIntent().getIntExtra("LoginId", 1);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), logTo);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setTabTextColors(R.color.Black, R.color.dBlack);
        tabs.setupWithViewPager(viewPager);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Logged", "false");
                editor.putInt("LoggedId", logTo);
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                Toast toast = Toast.makeText(MainActivity.this, "Logged out successfully !", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Logged", "true");
        editor.putInt("LoggedId", logTo);
        editor.commit();
        finishAffinity();
    }
}