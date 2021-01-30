package com.example.shoppinglist_zeeshan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    TextInputLayout createEmail, createPass;
    Button createAccountButton;
    TextView goToLoginButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createEmail = findViewById(R.id.createEmailID);
        createPass = findViewById(R.id.createPassword);
        createAccountButton = findViewById(R.id.createAccountButtonID);
        goToLoginButton = findViewById(R.id.goToLogInAccountID);

        mAuth = FirebaseAuth.getInstance();

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email2 = createEmail.getEditText().getText().toString().trim();
                String pass2 = createPass.getEditText().getText().toString().trim();

                if (!(TextUtils.isEmpty(email2)) && !(TextUtils.isEmpty(pass2))) {

                    createEmail.setError(null);
                    createPass.setError(null);
                    mAuth.createUserWithEmailAndPassword(email2, pass2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CreateAccount.this, "Account Created", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CreateAccount.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    createEmail.setError("Required");
                    createPass.setError("Required");
                }
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount.this, LogIn.class));
                finish();
            }
        });
    }
}