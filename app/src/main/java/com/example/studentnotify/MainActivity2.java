package com.example.studentnotify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements MyCustomDialog4.OnInputSelected{

    private static final int SIGN_IN = 1;
    private EditText editText;
    private Button teacherButton,studButton,signInBtn;
    private TextView signUpBtn,goBackBtn;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private String serverId,enteredId,signUpId;
    private boolean checkTid,checkSid,teacher,idExist;
    private ArrayList<String> teacherList,studentList;
    private String Name,Id;
    private int count1 = 0,count2 = 0;

    private DatabaseReference databaseReference1,databaseReference2;
    private DatabaseReference databaseReference3,databaseReference4;
    private ValueEventListener valueStudent,valueTeacher;

    private SignInButton gSignInButton;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

    private Intent intent2;
    private int googleId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        intent2 = new Intent(MainActivity2.this,MainActivity.class);

        editText = findViewById(R.id.editInput);
        teacherButton = findViewById(R.id.teachDetail);
        studButton = findViewById(R.id.studDetail);
        signInBtn = findViewById(R.id.signIn);
        signUpBtn = findViewById(R.id.signUp);
        goBackBtn = findViewById(R.id.goBack);
        progressBar = findViewById(R.id.progressBar);

        gSignInButton = findViewById(R.id.googleSignIn);
        firebaseAuth = FirebaseAuth.getInstance();

        teacherList = new ArrayList<>();
        studentList = new ArrayList<>();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

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

        databaseReference3 = FirebaseDatabase.getInstance().getReference("teacherId");
        databaseReference4 = FirebaseDatabase.getInstance().getReference("studentId");

        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Profile profile = postSnapshot.getValue(Profile.class);
                    teacherList.add(profile.getUserId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Profile profile = postSnapshot.getValue(Profile.class);
                    studentList.add(profile.getUserId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                teacherButton.setVisibility(View.GONE);
                studButton.setVisibility(View.GONE);
                signUpBtn.setVisibility(View.GONE);
                gSignInButton.setVisibility(View.GONE);

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
                            valueTeacher = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(count2 == 0) {
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
                                            count2 = 1;
                                        }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast toast = Toast.makeText(MainActivity2.this,"Failed to connect server !",Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            };
                            databaseReference1.addValueEventListener(valueTeacher);
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
                gSignInButton.setVisibility(View.GONE);

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
                             valueStudent = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(count1==0) {
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
                                        count1 = 1;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast toast = Toast.makeText(MainActivity2.this,"Failed to connect server !",Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            };
                            databaseReference2.addValueEventListener(valueStudent);
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
                        gSignInButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }.start();

                closeKeyboard();
            }
        });

    }

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            teacherButton.setVisibility(View.GONE);
            studButton.setVisibility(View.GONE);
            signUpBtn.setVisibility(View.GONE);
            gSignInButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast toast = Toast.makeText(MainActivity2.this,"Signed In Successfully !",Toast.LENGTH_SHORT);
            toast.show();
            FirebaseGoogleAuth(account);
        } catch (ApiException e) {
            Toast toast = Toast.makeText(MainActivity2.this,"Sign In Failed !",Toast.LENGTH_SHORT);
            toast.show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    if(user != null){

                        String userName = account.getDisplayName();
                        String userId = account.getId().trim();
                        String passId = "" + userId.charAt(0) + userId.charAt(1) + userId.charAt(2) + userId.charAt(3)+ userId.charAt(4) + userId.charAt(5) + userId.charAt(6) + userId.charAt(7) + "";
                        googleId = Integer.parseInt(passId.trim());
                        Toast toast = Toast.makeText(MainActivity2.this,"Welcome "+userName+" !",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        }).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                /*teacherButton.setVisibility(View.VISIBLE);
                studButton.setVisibility(View.VISIBLE);
                signUpBtn.setVisibility(View.VISIBLE);
                gSignInButton.setVisibility(View.VISIBLE);*/

                if(googleId != 0) {
                    intent2.putExtra("LoginId", googleId);
                    startActivity(intent2);
                }

                progressBar.setVisibility(View.GONE);
            }
        });;

    }

    @Override
    public void sendInput(String input, String input1, boolean isTeacher) {
        Name = input;
        Id = input1;
        signUpId = input1;
        teacher = isTeacher;
        sendToCloud();
    }

    private void sendToCloud() {
        if(teacher){
            if(count2 == 1){
                databaseReference1.removeEventListener(valueTeacher);
                count2 = 0;
            }
            signUpTeacher();
        }
        else {
            if(count1 == 1) {
                databaseReference2.removeEventListener(valueStudent);
                count1 = 0;
            }
            signUpStudent();
        }
    }

    private void signUpTeacher(){
       // count2 = 0;
        for (int i = 0 ; i < teacherList.size() ; i++){
            if(signUpId.equals(teacherList.get(i))){
                idExist = true;
            }
        }

        if(!idExist) {
            Profile profile = new Profile(Name, Id);
            String uploadId = databaseReference1.push().getKey();
            databaseReference1.child(uploadId).setValue(profile);
            Toast toast = Toast.makeText(MainActivity2.this, "Successfully signed up !", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            idExist = false;
            Toast toast = Toast.makeText(MainActivity2.this, "Id exist already !", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void signUpStudent(){
       // count1 = 0;
        for (int i = 0 ; i < studentList.size() ; i++){
            if(signUpId.equals(studentList.get(i))){
                idExist = true;
            }
        }

        if(!idExist) {
            Profile profile = new Profile(Name, Id);
            String uploadId = databaseReference2.push().getKey();
            databaseReference2.child(uploadId).setValue(profile);
            Toast toast = Toast.makeText(MainActivity2.this, "Successfully signed up !", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            idExist = false;
            Toast toast = Toast.makeText(MainActivity2.this, "Id exist already !", Toast.LENGTH_SHORT);
            toast.show();
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