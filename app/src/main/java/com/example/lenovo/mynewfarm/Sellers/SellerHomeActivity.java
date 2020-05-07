package com.example.lenovo.mynewfarm.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.mynewfarm.Admin.AdminCheckNewProductsActivity;
import com.example.lenovo.mynewfarm.Buyers.MainActivity;
import com.example.lenovo.mynewfarm.Model.Products;
import com.example.lenovo.mynewfarm.Prevalent.Prevalent;
import com.example.lenovo.mynewfarm.R;
import com.example.lenovo.mynewfarm.ViewHolder.ItemViewHolder;
import com.example.lenovo.mynewfarm.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class SellerHomeActivity extends AppCompatActivity
{

    private TextView mTextMessage;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intenthome = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                    startActivity(intenthome);
                    return true;
                case R.id.navigation_new_orders:
                    Intent intentneworders = new Intent(SellerHomeActivity.this,SellerNewOrderActivity.class);
                    startActivity(intentneworders);
                    return true;

                case R.id.navigation_add:
                    Intent intentnew = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                    startActivity(intentnew);
                    return true;

                case R.id.navigation_logout:

                        Paper.book().destroy();

                        Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        Paper.init(this);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView=(RecyclerView) findViewById(R.id.seller_home_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverifiedProductsRef.orderByChild("sellerPhone")
                                .equalTo(Prevalent.currentOnlineUser.getPhone()),Products.class).build();

        FirebaseRecyclerAdapter<Products,ItemViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductStatus.setText("State: " + model.getProductState());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                final String productID = model.getPid();

                                CharSequence options[] = new CharSequence[]
                                        {

                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder= new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to Delete this Product? Are you Sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position)
                                    {
                                        if(position==0)
                                        {
                                            deleteProduct(productID);

                                        }
                                        if(position==1)
                                        {

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seller_item_view, viewGroup, false);
                        ItemViewHolder holder = new ItemViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void deleteProduct(String productID)
    {
        unverifiedProductsRef.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(SellerHomeActivity.this, "The selected product has been Deleted Successfully.", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
