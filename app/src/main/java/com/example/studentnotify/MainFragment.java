package com.example.studentnotify;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentnotify.ui.main.BlankFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainFragment extends Fragment implements MyCustomDialog2.OnInputSelected,MyCustomDialog3.OnInputSelected, Recycler_PDF2_Adapter.OnItemClickListener {

    private static final String TAG = "MainFragment";

    private FloatingActionButton fab2;
    private RecyclerView recyclerView;
    private ArrayList<pdfDetails> pdfDetailsList;
    private Recycler_PDF2_Adapter adapter;
    private RelativeLayout uploadLayout;
    private int pos=0;

    private FirebaseStorage mStorage;
    private StorageReference storageReference;
    private DatabaseReference mDatabaseRef;
    private Uri uri1;

    @Override
    public void sendInput(String input,String input1, Uri uriPath) {
        pdfDetailsList.add(new pdfDetails(input,input1));
        pdfDetailsList.get(pdfDetailsList.size()-1).setFilePath(uriPath);
        uploadLayout.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void sendInput2(Boolean input) {
        if(!pdfDetailsList.get(pos).getPdfUrl().equals("pdfUrl")){

            final String selectedKey = pdfDetailsList.get(pos).getFileKey();

            StorageReference storageReference = mStorage.getReferenceFromUrl(pdfDetailsList.get(pos).getPdfUrl());
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(getContext(), "File deleted successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Deletion failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        pdfDetailsList.remove(pos);
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = view.findViewById(R.id.pdfListView);
        fab2 = view.findViewById(R.id.fab2);
        uploadLayout = view.findViewById(R.id.uploadLayout);
        uploadLayout.setVisibility(View.GONE);

        loadData();

        mStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        recyclerView.setHasFixedSize(true);
        adapter = new Recycler_PDF2_Adapter(pdfDetailsList,getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(MainFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomDialog2 dialog2 = new MyCustomDialog2();
                dialog2.setTargetFragment(MainFragment.this, 1);
                dialog2.show(getFragmentManager(), "MyCustomDialog2");
            }
        });

        return view;
    }


    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Shared preferences2", Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String json1 = sharedPreferences.getString("PDF list", null);

        Type type1 = new TypeToken<ArrayList<pdfDetails>>() {}.getType();
        pdfDetailsList = gson.fromJson(json1, type1);
        if (pdfDetailsList == null) {
            pdfDetailsList = new ArrayList<>();
        }
    }

    @Override
    public void onUploadClicked(int position) {

        if(pdfDetailsList.get(position).getPdfUrl().equals("pdfUrl")) {
            uri1 = pdfDetailsList.get(position).getFilePath();

            uploadPDFFile(uri1, position);

            StorageReference filepath = storageReference.child("PDFs").child(Objects.requireNonNull(uri1.getLastPathSegment()));
            filepath.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "File uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getContext(), "File have been uploaded already", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveClicked(int position) {
        pdfDetailsList.remove(position);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDeleteClicked(int position) {
            pos = position;
            MyCustomDialog3 dialog3 = new MyCustomDialog3();
            dialog3.setTargetFragment(MainFragment.this, 1);
            dialog3.show(getFragmentManager(), "MyCustomDialog3");
    }

    private void uploadPDFFile(Uri data, final int position) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(uri1));
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete()) ;
                Uri url = uri.getResult();

                String pdfUrl = url.toString();

                pdfDetailsList.get(position).setPdfUrl(pdfUrl);

                String pdfName = pdfDetailsList.get(position).getPdfName();

                pdfDetails pdfDetails = new pdfDetails(pdfName,pdfUrl);

                String uploadId = mDatabaseRef.push().getKey();
                mDatabaseRef.child(uploadId).setValue(pdfDetails);

                pdfDetailsList.get(position).setFileKey(uploadId);

                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("   Uploaded: " + (int) progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = Objects.requireNonNull(getActivity()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Shared preferences2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json1 = gson.toJson(pdfDetailsList);
        editor.putString("PDF list", json1);
        editor.apply();
    }
}
