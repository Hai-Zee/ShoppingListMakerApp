package com.example.shoppinglist_zeeshan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Data;
import MyAdapter.MyAdapter;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Data> list;
    MyAdapter myAdapter;
    FloatingActionButton floatingActionButton;
    TextView totalAmount;
    Button add_Button;
    TextInputLayout add_Item, add_Desc, add_Price;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    LottieAnimationView lottieAnimation;

    int x, totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList();
        floatingActionButton = findViewById(R.id.floatingActionButtonID);
        totalAmount = findViewById(R.id.totalAmountID);
        recyclerView = findViewById(R.id.recyclerViewID);
        toolbar = findViewById(R.id.toolbarID);
        lottieAnimation = findViewById(R.id.lottieID);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(null);

        lottieAnimation.setVisibility(View.VISIBLE);

//        setup firebase
        setUpFirebase();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lottieAnimation.setVisibility(View.INVISIBLE);

                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, null);
                add_Button = v.findViewById(R.id.addButtonID);
                add_Item = v.findViewById(R.id.addItemID);
                add_Desc = v.findViewById(R.id.addDescID);
                add_Price = v.findViewById(R.id.addPriceID);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
//                       .setTitle("Build")
//                       .setMessage("wanna build?")
//                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                           @Override
//                           public void onClick(DialogInterface dialogInterface, int i) {
//
//                           }
//                       })
//                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                           @Override
//                           public void onClick(DialogInterface dialogInterface, int i) {
//
//                           }
//                       });
//
                        .setView(v);
                AlertDialog myDialog = builder.create();
                myDialog.show();

                add_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String item = add_Item.getEditText().getText().toString().trim();
                        String desc = add_Desc.getEditText().getText().toString().trim();
                        String priceString = add_Price.getEditText().getText().toString().trim();
                        int intPrice = Integer.parseInt(priceString);

                        if (!TextUtils.isEmpty(item) && !TextUtils.isEmpty(desc) || !TextUtils.isEmpty(priceString)) {
//                            Date currentTime = Calendar.getInstance().getTime();
//                            Float priceFloat = Float.parseFloat(priceString);
                            add_Item.setError(null);
                            add_Desc.setError(null);
                            add_Price.setError(null);

                            String randomId = databaseReference.push().getKey();
//                            DatabaseReference newReference = databaseReference.child(randomId);
//                            newReference.setValue(data
                            Data data = new Data(item, desc, intPrice, DateFormat.getTimeInstance().format(new Date()), randomId);
                            list.add(data);
                            checkVisibility();
                            databaseReference.child("MainList").child(randomId).setValue(data);

                            setupRecyclerView();

                            myDialog.dismiss();
                        } else {
                            if (TextUtils.isEmpty(item)) {
                                add_Item.setError("Required");
                            }
                        }
                    }
                });
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    if (direction == ItemTouchHelper.LEFT){
                        int posD = viewHolder.getAdapterPosition();
                        Data deletedItemLeft = list.get(posD);

                        databaseReference.child("MainList").child(deletedItemLeft.getRandomId()).removeValue();

                        list.remove(posD);

                        checkVisibility();

                        myAdapter.notifyDataSetChanged();
                        Snackbar.make(recyclerView, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                list.add(posD, deletedItemLeft);
                                checkVisibility();
                                databaseReference.child("MainList").child(deletedItemLeft.getRandomId()).setValue(deletedItemLeft);
                                myAdapter.notifyDataSetChanged();
                            }
                        }).show();
                    }

                    if (direction == ItemTouchHelper.RIGHT) {
                    int posR = viewHolder.getAdapterPosition();
                    Data archievedItemRight = list.get(posR);
                    databaseReference.child("MainList").child(archievedItemRight.getRandomId()).removeValue();
                    databaseReference.child("ArchList").child(archievedItemRight.getRandomId()).setValue(archievedItemRight);
                    list.remove(posR);
//                    checkVisibility();
                    myAdapter.notifyDataSetChanged();
                    Snackbar.make(recyclerView, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            list.add(posR, archievedItemRight);
//                            checkVisibility();
                            databaseReference.child("MainList").child(archievedItemRight.getRandomId()).setValue(archievedItemRight);
                            databaseReference.child("ArchList").child(archievedItemRight.getRandomId()).removeValue();
                            myAdapter.notifyDataSetChanged();
                        }
                    }).show();
                }
                }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor((ContextCompat.getColor(MainActivity.this, R.color.swipeLeft_color)))
                        .addSwipeLeftActionIcon(R.drawable.ic_archive_24)
                        .addSwipeRightBackgroundColor((ContextCompat.getColor(MainActivity.this, R.color.swipeRight_color)))
                        .addSwipeRightActionIcon(R.drawable.left_swipe_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(currentUserID);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkVisibility();

        databaseReference.child("MainList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                totalPrice = 0;

                for (DataSnapshot childValue : snapshot.getChildren()) {
                    Data retrievedData = childValue.getValue(Data.class);
                    list.add(retrievedData);

                    x = retrievedData.getPrice();

                    totalPrice = totalPrice + x;
                }
                totalAmount.setText(String.valueOf(totalPrice));
                setupRecyclerView();
                checkVisibility();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                checkVisibility();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(list);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.archieveID: startActivity(new Intent(MainActivity.this, ArchieveActivity.class));
                break;
            case R.id.signOut: mAuth.signOut(); startActivity(new Intent(MainActivity.this, LogIn.class)); finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show();
    }

    private void checkVisibility(){
        if(list.size() == 0){
            recyclerView.setVisibility(View.INVISIBLE);
            lottieAnimation.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            lottieAnimation.setVisibility(View.INVISIBLE);
        }
    }
}