package com.example.tips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImagesCounters = new ArrayList<>();
    private ArrayList<String> mImagesUsers = new ArrayList<>();
    private ArrayList<String> mImagesKeys = new ArrayList<>();
    private String userMail;

    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageNames,
                               ArrayList<String> mImagesCounters,
                               ArrayList<String> mImageTypes, ArrayList<String> mImagesUsers,
                               ArrayList<String> mImagesKeys, String userMail) {
        this.mImageNames = mImageNames;
        this.mContext = mContext;
        this.mImagesCounters = mImagesCounters;
        this.mImagesUsers = mImagesUsers;
        this.mImagesKeys = mImagesKeys;
        this.userMail = userMail;
        this.userMail = this.userMail.replace("@","_");
        this.userMail = this.userMail.replace(".","_");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.imageName.setText(mImageNames.get(position));
        holder.imageCounter.setText(mImagesCounters.get(position));
        holder.imageMail.setText(mImagesUsers.get(position));
        holder.email = this.userMail;
        holder.imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = mImagesKeys.get(position);
                Integer counterImage = Integer.parseInt(mImagesCounters.get(position));
                final Integer updateCounter = counterImage + 1;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("Types");
                final DatabaseReference referenceToCounter = database.getReference("Counters");

                referenceToCounter.child(userMail).orderByKey().equalTo(id)
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {
                            Toast.makeText(mContext, "You already voted for this tip!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            myRef.child(id).child("counter_likes").setValue(updateCounter);
                            Toast.makeText(mContext, "Thank you for voting!",
                                    Toast.LENGTH_SHORT).show();
                            referenceToCounter.child(userMail).child(id).setValue("1");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        boolean flag = false;
        String email;
        ImageView image, imageLike;
        TextView imageName, imageCounter, imageMail;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_counter_likes);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            imageCounter = itemView.findViewById(R.id.image_counter);
            imageLike = itemView.findViewById(R.id.imageLike);
            imageMail = itemView.findViewById(R.id.imageEmailUser);
        }
    }
}
