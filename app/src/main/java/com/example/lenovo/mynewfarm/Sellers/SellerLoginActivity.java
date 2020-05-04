package com.example.lenovo.mynewfarm.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.mynewfarm.Model.Users;
import com.example.lenovo.mynewfarm.Prevalent.Prevalent;
import com.example.lenovo.mynewfarm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerLoginActivity extends AppCompatActivity {
    private EditText SellerPhoneNumber, SellerPassword;
    private Button SellerLoginButton;
    private ProgressDialog loadingBar;

    private String parentDbName = "Sellers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        SellerLoginButton = (Button) findViewById(R.id.seller_login_btn);
        SellerPassword = (EditText) findViewById(R.id.seller_login_password);
        SellerPhoneNumber = (EditText) findViewById(R.id.seller_login_phone);
        loadingBar = new ProgressDialog(this);

        SellerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginSeller();
            }
        });
    }




    private void LoginSeller()
    {
        String phone = SellerPhoneNumber.getText().toString();
        String password = SellerPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccountSeller(phone, password);
        }
    }
    private void AllowAccessToAccountSeller(final String phone, final String password)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Sellers"))
                            {
                                Toast.makeText(SellerLoginActivity.this, "Welcome Seller, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(SellerLoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                        loadingBar.dismiss();
                    }
                }
                else
                {
                    Toast.makeText(SellerLoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
