package br.com.dfn.realtimedatabase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static final String DATABASE_CHILD = "users";

    private RecyclerView mUsersRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<User, UserViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUsersRecyclerView = (RecyclerView)findViewById(R.id.usersRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDatabase();
            }
        });


        mFirebaseAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.item_user,
                UserViewHolder.class,
                myRef.child(DATABASE_CHILD)) {

            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User user, int position) {
                viewHolder.nameTextView.setText(user.name);
                viewHolder.lastNameTextView.setText(user.sobrenome);

                if (user.photo == null) {
                    viewHolder.userImageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(MainActivity.this)
                            .load(user.photo)
                            .into(viewHolder.userImageView);
                }
            }
        };

        mUsersRecyclerView.setLayoutManager(mLinearLayoutManager);
        mUsersRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertDatabase() {

        User user = new User();
        user.uuid = UUID.randomUUID().toString();
        user.name = "Diego";
        user.sobrenome = "Nascimento";
        user.photo = "https://media.licdn.com/mpr/mpr/shrink_100_100/p/1/000/1db/29c/1b902ec.jpg";

        myRef.child(DATABASE_CHILD).child(user.uuid).setValue(user);
    }

    //RecyclerView
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView lastNameTextView;
        public CircleImageView userImageView;

        public UserViewHolder(View v) {
            super(v);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            lastNameTextView = (TextView) itemView.findViewById(R.id.lastNameTextView);
            userImageView = (CircleImageView) itemView.findViewById(R.id.userImageView);
        }
    }
}
