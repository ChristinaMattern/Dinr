package com.example.dinr;
/*@author Nola Smtih
@date 4/25/2019 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FriendSearch extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_search);
        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);
        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString().toLowerCase();
                if(searchText.equals("")){//displays all users if user enters 1 space
                    fetch();
                }else {//searches for users
                    firebaseUserSearch(searchText);
                    if(adapter.getItemCount()==0){
                        Toast.makeText(FriendSearch.this, "User not found...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        mResultList.setLayoutManager(linearLayoutManager);
        mResultList.setHasFixedSize(true);
        fetch();//retrieves all users
    }

    private void firebaseUserSearch(String searchText) {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUser=currentFirebaseUser.getUid();//retrieves user id of signed in user
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("users").orderByChild("fNameS").equalTo(searchText);
        //tells firebase where to begin retrieving user data
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter = new FirebaseRecyclerAdapter<User, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final User model) {
                if(!currentUser.equals(model.getUserId())) {//to make sure the current user does not pop up in search
                    String fullName = model.getfName() + " " + model.getlName();
                    holder.setTxtName(fullName, model.getMajor(), model.getLocation(), model.getYear());
                    holder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences sharedPref = getSharedPreferences("OtherId", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("id", model.getUserId().toString());
                            editor.apply();
                            startActivity(new Intent(FriendSearch.this, OtherProfile.class));
                        }
                    });
                }
                else{//hides current user from display
                    holder.root.setVisibility(View.GONE);
                }
            }

            @NonNull
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
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUser=currentFirebaseUser.getUid();//retrieves user id of signed in user
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .orderByChild("id");//tells firebase where to begin retrieving user data
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new FirebaseRecyclerAdapter<User, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final User model) {
                if(!currentUser.equals(model.getUserId())) {//so current user is not displayed
                    String fullName = model.getfName() + " " + model.getlName();
                    holder.setTxtName(fullName, model.getMajor(), model.getLocation(), model.getYear());
                    holder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences sharedPref = getSharedPreferences("OtherId", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("id", model.getUserId().toString());
                            editor.apply();
                            startActivity(new Intent(FriendSearch.this, OtherProfile.class));
                        }
                    });
                }
                else{//hides current user form search
                    holder.root.setVisibility(View.GONE);
                }
            }

            @NonNull
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friend_search_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()) {
            case R.id.Settings:
                startActivity(new Intent(FriendSearch.this, Settings.class));
                return true;
            case R.id.Logout:
                Toast.makeText(FriendSearch.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(FriendSearch.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(FriendSearch.this, Faq.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(FriendSearch.this, HomeScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}