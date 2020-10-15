package com.example.tips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    Button addTip;
    ImageView imageLogout;
    TextView userMail;
    FirebaseAuth mFirebaseAuth;
    private final List<Tip> allTips = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImagesCounters = new ArrayList<>();
    private ArrayList<String> mImageTypes = new ArrayList<>();
    private ArrayList<String> mImagesUsers = new ArrayList<>();
    private ArrayList<String> mImagesKeys = new ArrayList<>();
    String mImageLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseDatabase mDatabse;
        final DatabaseReference mReference;

        mDatabse = FirebaseDatabase.getInstance();
        mReference = mDatabse.getReference("Types");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode: snapshot.getChildren())
                {
                    String tipText = keyNode.child("text").getValue(String.class);
                    String tipType = keyNode.child("type").getValue(String.class);
                    String tipUserUploaded = keyNode.child("user_uploaded").getValue(String.class);
                    int tipCounter = keyNode.child("counter_likes").getValue(Integer.class);
                    allTips.add(new Tip(tipType, tipUserUploaded, tipText, tipCounter,
                            keyNode.getKey()));
                }
                initImagebitmaps();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        userMail = (TextView) findViewById(R.id.helloUserMail);
        String messageMailUser = mFirebaseAuth.getCurrentUser().getEmail();
        userMail.setText(messageMailUser);
        addTip = findViewById(R.id.addTipButton);
        imageLogout = findViewById(R.id.imageLogout);

        imageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Are you sure you want to exit?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(HomeActivity.this,
                                        MainActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert= builder.create();
                alert.show();
            }
        });

        addTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddTipActivity.class));
            }
        });
    }

    private void initImagebitmaps(){
        Collections.sort(allTips);
        for (Tip tip: allTips)
        {
            String imageContent = tip.getType() + ":  " + tip.getText();
            mNames.add(imageContent);
            mImageTypes.add(tip.getType());
            mImagesUsers.add(tip.getUser_uploaded());
            mImagesKeys.add(tip.getId());
            mImagesCounters.add(Integer.toString(tip.getCounter_likes()));
        }
        initRecyclerView();
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(this,mNames,mImagesCounters,mImageTypes,mImagesUsers,
                mImagesKeys,mFirebaseAuth.getCurrentUser().getEmail());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}