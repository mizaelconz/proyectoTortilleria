package com.example.tortilleria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView tortillas, totopos;
    private ImageView salsa, frijoles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tortillas = findViewById(R.id.tortillas);
        totopos = findViewById(R.id.totopos);
        salsa = findViewById(R.id.salsa);
        frijoles = findViewById(R.id.frijoles);



        tortillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "cafe");
                startActivity(intent);
            }
        });

        totopos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "galleta");
                startActivity(intent);
            }
        });

        salsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "pastel");
                startActivity(intent);
            }
        });

        frijoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "te");
                startActivity(intent);
            }
        });
    }
}
