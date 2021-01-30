package com.example.shoppinglist_zeeshan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.Data;
import MyAdapter.MyAdapter;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ArchieveActivity extends AppCompatActivity {

    List<Data> list;
    RecyclerView mRecyclerView;
    Toolbar toolbar;
    LottieAnimationView lottieAnimation;

    MyAdapter myAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archieve);

        toolbar = findViewById(R.id.archToolbarID);
        lottieAnimation = findViewById(R.id.archLottieID);
        mRecyclerView = findViewById(R.id.archRecyclerViewID);

        list = new ArrayList<Data>();
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(null);

        checkVisibility();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(mAuth.getCurrentUser().getUid());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArchieveActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT){
                    int posD = viewHolder.getAdapterPosition();
                    Data itemToBeDeleted = list.get(posD);
                    mDatabaseReference.child("ArchList").child(itemToBeDeleted.getRandomId()).removeValue();
                    list.remove(itemToBeDeleted);
                    checkVisibility();
                    myAdapter.notifyDataSetChanged();

                    Snackbar.make(mRecyclerView, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            list.add(posD, itemToBeDeleted);
                            mDatabaseReference.child("ArchList").child(itemToBeDeleted.getRandomId()).setValue(itemToBeDeleted);
                            myAdapter.notifyDataSetChanged();
                        }
                    }).show();
                }
                if(direction == ItemTouchHelper.RIGHT){
                    int posS = viewHolder.getAdapterPosition();
                    Data itemToSend = list.get(posS);
                    mDatabaseReference.child(itemToSend.getRandomId()).removeValue();
                    list.remove(itemToSend);
                    checkVisibility();
                    myAdapter.notifyDataSetChanged();

                    Snackbar.make(mRecyclerView, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            list.add(posS, itemToSend);
                            mDatabaseReference.child("ArchList").child(itemToSend.getRandomId()).setValue(itemToSend);
                            myAdapter.notifyDataSetChanged();
                        }
                    }).show();
                }
            }

            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(ArchieveActivity.this, R.color.swipeLeft_color))
                        .addActionIcon(R.drawable.left_swipe_delete)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(ArchieveActivity.this, R.color.swipeRight_color))
                        .addSwipeRightActionIcon(R.drawable.ic_unarchive_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkVisibility();
        mDatabaseReference.child("ArchList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot childValues : snapshot.getChildren()) {
                    Data data = childValues.getValue(Data.class);
                    list.add(data);
                }
                myAdapter = new MyAdapter(list);
                mRecyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                checkVisibility();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                checkVisibility();
                Toast.makeText(ArchieveActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void checkVisibility(){
        if(list.size() == 0){
            mRecyclerView.setVisibility(View.INVISIBLE);
            lottieAnimation.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            lottieAnimation.setVisibility(View.INVISIBLE);
        }
    }

}