package com.example.studentnotify.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentnotify.MainActivity2;
import com.example.studentnotify.MainFragment;
import com.example.studentnotify.MyCustomDialog;
import com.example.studentnotify.Profile;
import com.example.studentnotify.R;
import com.example.studentnotify.Recycler_Adapter;
import com.example.studentnotify.SubjectDetails;
import com.example.studentnotify.pdfDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BlankFragment extends Fragment implements MyCustomDialog.OnInputSelected, Recycler_Adapter.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private int LoggerId;

    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(String param1, int param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            LoggerId = getArguments().getInt(ARG_PARAM2, 0);
        }
    }

    private RecyclerView recyclerView;
    private Recycler_Adapter adapter;
    private ArrayList<SubjectDetails> Items;
    private View view;
    private DatabaseReference databaseReference;
    private int count = 0;

    @Override
    public void sendInput(String input, String input1, String input2) {
        if (input1.isEmpty()) {
            input1 = "Not available";
        }
        if (input2.isEmpty()) {
            input2 = "Not available";
        }
        Items.add(new SubjectDetails(input, "Credits : " + input1, "Materials : " + input2, 0, 0));
        Items.get(Items.size() - 1).setSubjectKey("Key");
        adapter.notifyDataSetChanged();
        Snackbar.make(view, "1 item added", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_blank, container, false);

        String Id = Integer.toString(LoggerId);
        databaseReference = FirebaseDatabase.getInstance().getReference(Id);

        Items = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        adapter = new Recycler_Adapter(Items);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (count == 0) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        String subName = postSnapshot.child("subject").getValue().toString();
                        String subContent = postSnapshot.child("content").getValue().toString();
                        String subDesc = postSnapshot.child("description").getValue().toString();

                        String attended = postSnapshot.child("attended").getValue().toString();
                        String bunked = postSnapshot.child("bunked").getValue().toString();

                        Items.add(new SubjectDetails(subName, subContent, subDesc, Integer.parseInt(attended), Integer.parseInt(bunked)));

                        Items.get(Items.size() - 1).setSubjectKey(postSnapshot.getKey());

                        adapter.notifyDataSetChanged();
                        count = 1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomDialog dialog = new MyCustomDialog();
                dialog.setTargetFragment(BlankFragment.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                }
                if (dy < 0) {
                    fab.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onRemoveClicked(int position) {
        databaseReference.child(Items.get(position).getSubjectKey()).removeValue();
        Items.remove(position);
        adapter.notifyItemRemoved(position);
        Snackbar.make(view, "1 item removed", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
    }

    @Override
    public void onStop() {

        super.onStop();

        for (int i = 0; i < Items.size(); i++) {
            SubjectDetails subjectDetails = Items.get(i);

            String key = subjectDetails.getSubjectKey();

            if (!key.equals("Key")) {
                databaseReference.child(key).removeValue();
            }

            String uploadId = databaseReference.push().getKey();
            Items.get(i).setSubjectKey(uploadId);
            databaseReference.child(uploadId).setValue(subjectDetails);
        }
    }

}