package com.example.tips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AddTipActivity extends AppCompatActivity {

    EditText messageText;
    Button addTip;
    FirebaseAuth mFirebaseAuth;
    ImageView imageLogout;
    ListView typesList;
    String chosenType;
    final List<String> allTypes = new ArrayList<>();
    boolean flagIfChosen;
    TipsAdapter tipsAdapter;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);
        tipsAdapter = new TipsAdapter(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        messageText = findViewById(R.id.TipMessage);
        addTip = findViewById(R.id.SendTipButton);
        imageLogout = findViewById(R.id.imageLogout);
        typesList = (ListView) findViewById(R.id.listOfTips);
        chosenType = "";
        flagIfChosen = false;
        typesList.setAdapter(tipsAdapter);

        imageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddTipActivity.this);
                builder.setMessage("Are you sure you want to exit?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(AddTipActivity.this,
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

        typesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flag = position;
                tipsAdapter.notifyDataSetChanged();
                chosenType =  allTypes.get(position);
                flagIfChosen = true;
            }
        });

        addTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = messageText.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                if (!flagIfChosen)
                {
                    Toast.makeText(AddTipActivity.this,"You need to choose a tip's type"
                            ,Toast.LENGTH_SHORT).show();
                }
                else if(message.isEmpty())
                {
                    Toast.makeText(AddTipActivity.this,"You need to write a message"
                            ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Tip newTip = new Tip(chosenType,mFirebaseAuth.getCurrentUser().getEmail(),
                            messageText.getText().toString(),0,"");
                    myRef.child("Types").push().setValue(newTip);
                    Toast.makeText(AddTipActivity.this,"Thank you for the Tip!"
                            ,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddTipActivity.this, HomeActivity.class));
                }
            }
        });


        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_expandable_list_item_1);

        FirebaseDatabase mDatabse;
        DatabaseReference mReference;
        mDatabse = FirebaseDatabase.getInstance();
        mReference = mDatabse.getReference("tipTypes");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode: snapshot.getChildren())
                {
                    adapter.add(keyNode.getValue().toString());
                    allTypes.add(keyNode.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        typesList.setAdapter(adapter);
    }

    public class TipsAdapter extends BaseAdapter {

        Context context;

        public TipsAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return allTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return allTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = View.inflate(context, R.layout.tip_cell, null);
            TextView nameTxt = rowView.findViewById(R.id.nameText);
            if (flag == position)
            {
                nameTxt.setTextColor(getResources().getColor(android.R.color.white));
                nameTxt.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
            }
            nameTxt.setText(allTypes.get(position));
            return rowView;
        }
    }
}