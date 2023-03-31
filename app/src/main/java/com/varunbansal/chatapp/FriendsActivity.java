package com.varunbansal.chatapp;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private UserAdapter userAdapter;
    String myImageUrl;
    UserAdapter.onUserClickListener onUserClickListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        progressBar = findViewById(R.id.progressBar);
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViews);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        onUserClickListener = new UserAdapter.onUserClickListener() {
            @Override
            public void onClicked(int position) {
                startActivity(new Intent(FriendsActivity.this,MessageActivity.class)
                        .putExtra("username_of_roommate",users.get(position).getUsername())
                        .putExtra("email_of_username",users.get(position).getEmail())
                        .putExtra("img_of_roommate",users.get(position).getProfilepicture())
                        .putExtra("my_img",myImageUrl)
                );

            }
        };

        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_profile)
            startActivity(new Intent(FriendsActivity.this, Profile.class));
        return super.onOptionsItemSelected(item);
    }

    private void getUsers(){
        users.clear();
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    users.add(dataSnapshot.getValue(User.class));
                }
                userAdapter = new UserAdapter(users,FriendsActivity.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
                recyclerView.setAdapter(userAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                for(User user : users){
                    if(user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        myImageUrl = user.getProfilepicture();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}