package com.example.dinr;
/*@author Nola Smtih
@date 4/25/2019 */
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FriendSearch extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mResultList;
    private TextView major;
    private TextView location;
    private TextView year;
    private TextView name;
    private DatabaseReference mUserDatabase;

    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_search);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        mResultList.setLayoutManager(linearLayoutManager);
        mResultList.setHasFixedSize(true);
        fetch();

    }

    private void firebaseUserSearch(String searchText) {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users").child("id")
                .orderByChild("searchText").equalTo(searchText);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, new SnapshotParser<User>() {
                    @NonNull
                    @Override
                    public User parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new User(snapshot.child("fName").getValue().toString()
                                ,snapshot.child("major").getValue().toString()
                                ,snapshot.child("location").getValue().toString()
                                ,snapshot.child("year").getValue().toString(),snapshot.getKey());}}).build();

        adapter = new FirebaseRecyclerAdapter<User, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, User model) {
                holder.setTxtName(model.getName(),model.getMajor(),model.getLocation(),model.getYear());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(FriendSearch.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.friend_search_card, parent, false);
                return new ViewHolder(view);
            }

        };
        mResultList.setAdapter(adapter);


    }


    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .orderByChild("id");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, new SnapshotParser<User>() {
                            @NonNull
                            @Override
                            public User parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new User(snapshot.child("fName").getValue().toString()
                                        ,snapshot.child("major").getValue().toString()
                                ,snapshot.child("location").getValue().toString()
                                ,snapshot.child("year").getValue().toString(),snapshot.getKey());}}).build();

        adapter = new FirebaseRecyclerAdapter<User, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, User model) {
                holder.setTxtName(model.getName(),model.getMajor(),model.getLocation(),model.getYear());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(FriendSearch.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.friend_search_card, parent, false);
                return new ViewHolder(view);
            }

        };
        mResultList.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView txtTitle;
        public TextView major;
        public TextView location;
        public TextView year;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.list_title);
            major = itemView.findViewById(R.id.major);
            location = itemView.findViewById(R.id.location);
            year = itemView.findViewById(R.id.year);
        }

        public void setTxtName(String string1, String string2, String string3, String string4) {
            txtTitle.setText(string1);
            major.setText(string2);
            location.setText(string3);
            year.setText(string4);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}