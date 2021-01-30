package com.example.shoppinglist_zeeshan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    TextInputLayout logInEmail, logInPassword;
    Button logInButton;
    FirebaseAuth mAuth;
    TextView goToCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logInEmail = findViewById(R.id.loginEmialID);
        logInPassword = findViewById(R.id.loginPassword);
        logInButton = findViewById(R.id.logInButtonID);
        goToCreateAccount = findViewById(R.id.goToCreateAccountID);

        mAuth = FirebaseAuth.getInstance();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = logInEmail.getEditText().getText().toString().trim();
                String pass = logInPassword.getEditText().getText().toString().trim();
                Log.d("login", "onClick: Zeeshu Looged In");
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
                    Log.d("if case", "onClick: Zeeshu if case");
                    logInEmail.setError(null);
                    logInPassword.setError(null);
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LogIn.this, "Logged In", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LogIn.this, MainActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    logInEmail.setError("Required");
                    logInPassword.setError("Required");
                }
            }
        });

        goToCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, CreateAccount.class));
            }
        });
    }
}