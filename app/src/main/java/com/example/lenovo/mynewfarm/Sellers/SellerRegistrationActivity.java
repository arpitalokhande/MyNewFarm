package com.example.lenovo.mynewfarm.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.mynewfarm.Buyers.HomeActivity;
import com.example.lenovo.mynewfarm.Buyers.LoginActivity;
import com.example.lenovo.mynewfarm.Buyers.MainActivity;
import com.example.lenovo.mynewfarm.Buyers.RegisterActivity;
import com.example.lenovo.mynewfarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private TextView sellerLoginBegin;
    private EditText nameInput, phoneInput, passwordInput, emailInput, addressInut,cityInput;
    private Button registerbtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);


        sellerLoginBegin = (TextView) findViewById(R.id.seller_already_have_acct_btn);
        nameInput = (EditText) findViewById(R.id.seller_name);
        phoneInput = (EditText) findViewById(R.id.seller_phone);
        passwordInput = (EditText) findViewById(R.id.seller_password);
        emailInput = (EditText) findViewById(R.id.seller_email);
        addressInut = (EditText) findViewById(R.id.seller_address);
        cityInput=(EditText)findViewById(R.id.seller_city);
        registerbtn = (Button) findViewById(R.id.seller_register_btn);

        loadingBar = new ProgressDialog(this);

        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {

        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();
        String email = emailInput.getText().toString();
        String address = addressInut.getText().toString();
        String city=cityInput.getText().toString();

        if (!name.equals("") && !phone.equals("") && !password.equals("") && !email.equals("") && !address.equals(""))
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateSellerphoneNumber(name, phone,password,email,address,city);
        }
        else
        {
            Toast.makeText(this, "Please Complete the form", Toast.LENGTH_SHORT).show();
        }

    }
    private void ValidateSellerphoneNumber(final String name, final String phone, final String password,final String email,final String address,final String city)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Sellers").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    userdataMap.put("email",email);
                    userdataMap.put("address",address);
                    userdataMap.put("city",city);

                    RootRef.child("Sellers").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SellerRegistrationActivity.this, "Congratulations seller, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(SellerRegistrationActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SellerRegistrationActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(SellerRegistrationActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SellerRegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
