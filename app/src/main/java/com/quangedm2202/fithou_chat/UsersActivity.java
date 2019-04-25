package com.quangedm2202.fithou_chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;


public class UsersActivity extends AppCompatActivity {


    private RecyclerView mUserList;
    private DatabaseReference mUsersDatabase;
    private Toolbar mToolbar;

    //tim kiem
    //private EditText search_input_text;
    //private ImageButton search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        mToolbar = (Toolbar) findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUserList = (RecyclerView) findViewById(R.id.users_list);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));


        //xóa 1 user
        registerForContextMenu(mUserList);


        //tim kiem
//        search_input_text = (EditText) findViewById(R.id.search_input_text);
//        search_button = (ImageButton) findViewById(R.id.search_button);
//
//        //su kien tim kiem
//        search_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String searchUserName = search_input_text.getText().toString();
//                if(TextUtils.isEmpty(searchUserName)) {
//                    Toast.makeText(UsersActivity.this,"Please write a user name to search...",Toast.LENGTH_SHORT).show();
//                }
//                SearchFriend(searchUserName);
//            }
//        });


    }

    //xóa 1 user
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.delete_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.delete_friend:
                Toast.makeText(this,"Delete option",Toast.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onContextItemSelected(item);
        }
    }
    //end xóa 1 user


//    private void SearchFriend(String searchUserName){
//        Toast.makeText(this,"Searching...",Toast.LENGTH_SHORT).show();
//        Query searchFriend = mUsersDatabase.orderByChild("name")
//                .startAt(searchUserName).endAt(searchUserName + "\uf8ff");

    @Override
        protected void onStart() {
            super.onStart();

        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UserViewHolder.class,
                mUsersDatabase
                //searchFriend
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {

                viewHolder.setDisplayName(model.getName());
                viewHolder.setUserStatus(model.getStatus());
                viewHolder.setUserImage(model.getThumb_image(),getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };

        mUserList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDisplayName(String name){
            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }
        public void setUserStatus(String status){
            TextView userNameView = mView.findViewById(R.id.user_single_status);
            userNameView.setText(status);
        }
        public void setUserImage(String thumb_image, Context ctx){
            CircleImageView userImageView =(CircleImageView)mView.findViewById(R.id.user_single_image);
            Picasso.get().load(thumb_image).placeholder(R.drawable.default_avata).into(userImageView);
        }

    }
}
