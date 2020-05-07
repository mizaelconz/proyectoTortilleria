package com.example.tortilleria;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button)findViewById(R.id.register_btn);
        InputName = (EditText)findViewById(R.id.register_username_input);
        InputPhoneNumber = (EditText)findViewById(R.id.register_phone_number_input);
        InputPassword = (EditText)findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);


        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });


    }

    /*

     */
    private void CreateAccount() {

        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Favor de Ingresar tu Nombre", Toast.LENGTH_SHORT).show();
        }
      else  if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Favor de Ingresar tu Numero de Telefono", Toast.LENGTH_SHORT).show();
        }
      else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Favor de Ingresar tu Contrasena elegida", Toast.LENGTH_SHORT).show();
        }
      else {
            loadingBar.setTitle("Crear Cuenta");
            loadingBar.setMessage("Espera Por favor, Chechando tus Credenciales de Acceso");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(name, phone, password);
            
        }
    }

    /**
     *
     * @param name
     * @param phone
     * @param password
     */
    private void ValidatephoneNumber(final String name, final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!(dataSnapshot.child("Users").child(phone).exists()))
                    {
                        HashMap<String,Object> userdataMap = new HashMap<>();
                        userdataMap.put("phone", phone);
                        userdataMap.put("password", password);
                        userdataMap.put("name", name);

                        RootRef.child("Users").child(phone).updateChildren(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this, "Bien Hecho Tu Cuenta fue Creada Con Exito!", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Error de Red : Verifica tu Conexion  ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                        {
                            Toast.makeText(RegisterActivity.this, "El numero " + phone + "ya esta registrado", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this, "Intentalo de nuevo usando otro numero de telefono", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}