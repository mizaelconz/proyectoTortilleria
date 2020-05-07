package com.example.tortilleria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//Recuerda que hay que importar los widgets personalizados
import com.rey.material.widget.CheckBox;
import com.example.tortilleria.Model.Users;
import com.example.tortilleria.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;


    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button)findViewById(R.id.login_btn);
        InputPhoneNumber = (EditText)findViewById(R.id.login_phone_number_input);
        InputPassword = (EditText)findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        AdminLink = (TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView)findViewById(R.id.not_admin_panel_link);


        chkBoxRememberMe = (CheckBox)findViewById(R.id.remember_me_chkb);
        Paper.init(this);

            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LoginUser();
                    
                }
            });

            AdminLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginButton.setText("Login Admin");
                    AdminLink.setVisibility(View.INVISIBLE);
                    NotAdminLink.setVisibility(View.VISIBLE);
                    parentDbName = "Admins";

                }
            });

            NotAdminLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginButton.setText("Login");
                    AdminLink.setVisibility(View.VISIBLE);
                    NotAdminLink.setVisibility(View.INVISIBLE);
                    parentDbName = "Users";
                }
            });

    }

    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Favor de Ingresar tu Numero de Telefono", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Favor de Ingresar tu Contrasena elegida", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Ingresa a tu Cuenta");
            loadingBar.setMessage("Espera Por favor, Chechando tus Credenciales de Acceso");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone, password);

        }
    }

    //Aqui se Realiza la validacion de accesos a la base de datos de la app se checa el telefono asi como el password del usuario en el registro

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if (chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);

        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);


                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Has Ingresado como Administrador Correctamente", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "Has Ingresado Correctamente", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }else
                            {
                            Toast.makeText(LoginActivity.this, "La contrasena es incorrecta", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }

                    }

                }else {
                    Toast.makeText(LoginActivity.this, "Cuenta de " + phone + "No esta registrada", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

               //     Toast.makeText(LoginActivity.this, "Necesitas Crear una Cuenta para Acceder", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
