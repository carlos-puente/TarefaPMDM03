package me.carlosjai.tendavirtual;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import me.carlosjai.tendavirtual.modelos.RexistroUsuario;
import me.carlosjai.tendavirtual.modelos.Usuario;
import me.carlosjai.tendavirtual.utils.Util;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BroadcastReceiver MyReceiver = null;

    private EditText email;
    private EditText contrasinal;
    private Button btLogin;
    private Button btRexistro;
    private TextView error;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private void findViews() {
        email = findViewById(R.id.email);
        contrasinal = findViewById(R.id.contrasinal);
        btLogin = findViewById(R.id.btLogin);
        btRexistro = findViewById(R.id.btRexistro);
        error = findViewById(R.id.error);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        findViews();
        addListeners();

    }

    private void addListeners() {
        email.addTextChangedListener(new TextWatcher() {

                                         @Override
                                         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                         }

                                         @Override
                                         public void onTextChanged(CharSequence s, int start, int before, int count) {

                                         }

                                         @Override
                                         public void afterTextChanged(Editable s) {
                                             comprobarBoton();
                                         }
                                     }
        );


        contrasinal.addTextChangedListener(new TextWatcher() {

                                               @Override
                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                               }

                                               @Override
                                               public void onTextChanged(CharSequence s, int start, int before, int count) {

                                               }

                                               @Override
                                               public void afterTextChanged(Editable s) {
                                                   comprobarBoton();
                                               }
                                           }
        );

        btLogin.setOnClickListener(this);
        btRexistro.setOnClickListener(this);


    }

    private void comprobarBoton() {
        error.setVisibility(View.GONE);
        if (validarEmail() && validarContrasinal()) {
            btLogin.setEnabled(true);
        } else {
            btLogin.setEnabled(false);
        }
    }

    private boolean validarContrasinal() {

        return !Util.estaBaleira(contrasinal.getText().toString());
    }

    private boolean validarEmail() {
        return !Util.estaBaleira(email.getText().toString());
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btLogin.getId()) {
            if (!Util.validarEmail(email.getText().toString().trim())) {
                amosarErro(R.string.error_email);
            } else if (contrasinal.getText().toString().trim().length() < 6) {
                amosarErro(R.string.error_contrasinal);
            } else {
                realizarAutenticacion(email.getText().toString().trim(), contrasinal.getText().toString().trim());

            }
        } else if (v.getId() == btRexistro.getId()) {
            Intent intent = new Intent(this, RexistroActivity.class);
            startActivity(intent);
            restartControles();
        }

    }

    private void restartControles() {
        email.setText("");
        contrasinal.setText("");
        btLogin.setEnabled(false);
    }

    private void realizarAutenticacion(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference docRef = db.collection("usuarios").document(user.getUid());
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Usuario u = documentSnapshot.toObject(Usuario.class);
                                    Log.d("DEBUG", "login:success: " + u.toString());
                                    if (u != null) {
                                        u.setId(mAuth.getCurrentUser().getUid());
                                        Intent intent = !u.isAdmin() ? new Intent(MainActivity.super.getBaseContext(), UsuarioActivity.class):new Intent(MainActivity.super.getBaseContext(), AdminActivity.class);
                                        intent.putExtra("usuario", u);
                                        startActivity(intent);
                                        restartControles();
                                    } else {
                                        amosarErro(R.string.error_login);
                                    }
                                }
                            });


                        } else {
                            amosarErro(R.string.error_login);
                        }
                    }
                });


    }

    private void amosarErro(int textInString) {
        error.setText(textInString);
        error.setVisibility(VISIBLE);
    }

}
