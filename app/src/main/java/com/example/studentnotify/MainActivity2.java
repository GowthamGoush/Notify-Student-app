package com.example.studentnotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentnotify.ui.main.BlankFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity implements MyCustomDialog4.OnInputSelected{

    private EditText editText;
    private Button teacherButton,studButton,signInBtn;
    private TextView signUpBtn,goBackBtn;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private String serverId,enteredId;
    private boolean checkTid,checkSid,teacher;
    private String Name,Id;

    private DatabaseReference databaseReference1,databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editText = findViewById(R.id.editInput);
        teacherButton = findViewById(R.id.teachDetail);
        studButton = findViewById(R.id.studDetail);
        signInBtn = findViewById(R.id.signIn);
        signUpBtn = findViewById(R.id.signUp);
        goBackBtn = findViewById(R.id.goBack);
        progressBar = findViewById(R.id.progressBar);

        editText.setVisibility(View.GONE);
        signInBtn.setVisibility(View.GONE);
        goBackBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        final Intent intent = new Intent(MainActivity2.this,MainActivity.class);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);
        String ifLogged = preferences.getString("Logged","false");
        int loggedInId = preferences.getInt("LoggedId",0);

        if(ifLogged.equals("true")){
            intent.putExtra("LoginId",loggedInId);
            startActivity(intent);
        }

        databaseReference1 = FirebaseDatabase.getInstance().getReference("teacherId");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("studentId");

        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                teacherButton.setVisibility(View.GONE);
                studButton.setVisibility(View.GONE);
                signUpBtn.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);

                countDownTimer = new CountDownTimer(500,500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        editText.setVisibility(View.VISIBLE);
                        signInBtn.setVisibility(View.VISIBLE);
                        goBackBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }.start();

                signInBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();
                        enteredId = editText.getText().toString();
                        if(!editText.getText().toString().isEmpty()){
                            databaseReference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            Profile profile = postSnapshot.getValue(Profile.class);
                                            serverId = profile.getUserId();

                                            if (enteredId.equals(serverId)) {
                                                checkTid = true;
                                                Toast toast = Toast.makeText(MainActivity2.this, "Welcome " + profile.getUserName() + " !", Toast.LENGTH_SHORT);
                                                toast.show();
                                                intent.putExtra("LoginId", 1);
                                                startActivity(intent);
                                            }
                                        }

                                        if (!checkTid) {
                                            Toast toast = Toast.makeText(MainActivity2.this, "Invalid Id", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast toast = Toast.makeText(MainActivity2.this,"Failed to connect server !",Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                        else if(editText.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(MainActivity2.this,"Field cannot be empty !",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast = Toast.makeText(MainActivity2.this,"Poor Network !",Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                });
            }
        });

        studButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                teacherButton.setVisibility(View.GONE);
                studButton.setVisibility(View.GONE);
                signUpBtn.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);

                countDownTimer = new CountDownTimer(500,500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        editText.setVisibility(View.VISIBLE);
                        signInBtn.setVisibility(View.VISIBLE);
                        goBackBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }.start();


                signInBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();
                        enteredId = editText.getText().toString();
                        if(!editText.getText().toString().isEmpty()){
                            databaseReference2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            Profile profile = postSnapshot.getValue(Profile.class);
                                            serverId = profile.getUserId();

                                            if (enteredId.equals(serverId)) {
                                                checkSid = true;
                                                Toast toast = Toast.makeText(MainActivity2.this, "Welcome " + profile.getUserName() + " !", Toast.LENGTH_SHORT);
                                                toast.show();
                                                intent.putExtra("LoginId", Integer.parseInt(serverId));
                                                startActivity(intent);
                                            }
                                        }

                                        if (!checkSid) {
                                            Toast toast = Toast.makeText(MainActivity2.this, "Invalid Id", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast toast = Toast.makeText(MainActivity2.this,"Failed to connect server !",Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                        else if(editText.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(MainActivity2.this,"Field cannot be empty !",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast = Toast.makeText(MainActivity2.this,"Poor Network !",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog4 dialog = new MyCustomDialog4();
                dialog.show(getSupportFragmentManager(), "MyCustomDialog");
            }
        });

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText.setVisibility(View.GONE);
                signInBtn.setVisibility(View.GONE);
                goBackBtn.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);

                countDownTimer = new CountDownTimer(500,500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        teacherButton.setVisibility(View.VISIBLE);
                        studButton.setVisibility(View.VISIBLE);
                        signUpBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }.start();

                closeKeyboard();
            }
        });

    }

    @Override
    public void sendInput(String input, String input1, boolean isTeacher) {
        Name = input;
        Id = input1;
        enteredId = input1;
        teacher = isTeacher;
        sendToCloud();
        Toast toast = Toast.makeText(MainActivity2.this,"Successfully signed up !", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void sendToCloud() {
        if(teacher){
            Profile profile = new Profile(Name,Id);

            String uploadId = databaseReference1.push().getKey();
            databaseReference1.child(uploadId).setValue(profile);
        }
        else {
            Profile profile = new Profile(Name,Id);

            String uploadId = databaseReference2.push().getKey();
            databaseReference2.child(uploadId).setValue(profile);
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}