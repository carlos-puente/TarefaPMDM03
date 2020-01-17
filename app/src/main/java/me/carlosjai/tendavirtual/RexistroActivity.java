package me.carlosjai.tendavirtual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import me.carlosjai.tendavirtual.modelos.RexistroUsuario;
import me.carlosjai.tendavirtual.modelos.TiposErro;
import me.carlosjai.tendavirtual.utils.Util;

public class RexistroActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNome;
    private TextView txtTituloRexistro;
    private EditText etApelidos;
    private EditText etEmail;
    private EditText etRepiteEmail;
    private EditText etContrasinal;
    private EditText etRepiteContrasinal;
    private RadioGroup radioGroup2;
    private RadioButton rbAdministrador;
    private RadioButton rbCliente;
    private Button btAtras;
    private Button btRexistro;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private void findViews() {
        etNome = (EditText)findViewById( R.id.etNome );
        txtTituloRexistro = (TextView)findViewById( R.id.txtTituloRexistro );
        etApelidos = (EditText)findViewById( R.id.etApelidos );
        etEmail = (EditText)findViewById( R.id.etEmail );
        etRepiteEmail = (EditText)findViewById( R.id.etRepiteEmail );
        etContrasinal = (EditText)findViewById( R.id.etContrasinal );
        etRepiteContrasinal = (EditText)findViewById( R.id.etRepiteContrasinal );
        radioGroup2 = (RadioGroup)findViewById( R.id.radioGroup2 );
        rbAdministrador = (RadioButton)findViewById( R.id.rbAdministrador );
        rbCliente = (RadioButton)findViewById( R.id.rbCliente );
        btAtras = (Button)findViewById( R.id.btAtras );
        btRexistro = (Button)findViewById( R.id.btRexistro );

        rbAdministrador.setOnClickListener( this );
        rbCliente.setOnClickListener( this );
        btAtras.setOnClickListener( this );
        btRexistro.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2020-01-02 10:57:30 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
         if ( v == btAtras ) {
            super.onBackPressed();
        } else if ( v == btRexistro ) {
            realizarRexistro();
        }
    }

    private void realizarRexistro() {
        if(validarCampos()) {
            //(String nome, String apelidos, String email, String contrasinal, boolean admin)
            final RexistroUsuario datosRexistro = new RexistroUsuario(
                    etNome.getText().toString(),
                    etApelidos.getText().toString(),
                    etEmail.getText().toString(),
                    etContrasinal.getText().toString(),
                    rbAdministrador.isChecked());
            try {
                mAuth.createUserWithEmailAndPassword(datosRexistro.getEmail(), datosRexistro.getContrasinal())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("DEBUG", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Map<String, Object> usuario = new HashMap<>();
                                    usuario.put("email", datosRexistro.getEmail());
                                    usuario.put("nome", datosRexistro.getNome());
                                    usuario.put("apelidos", datosRexistro.getApelidos());
                                    usuario.put("admin", datosRexistro.isAdmin());
                                    db.collection("usuarios").document(user.getUid()).set(usuario);
                                    Util.amosarMensaxe(RexistroActivity.this,"Rexistro correcto!", Toast.LENGTH_SHORT);
                                    RexistroActivity.super.onBackPressed();
                                } else {

                                    Log.w("ERROR", "createUserWithEmail:failure", task.getException());

                                    if(task.getException().getMessage().trim().equalsIgnoreCase(TiposErro.EMAIL_EN_USO.toString())) {
                                        Util.amosarErro(RexistroActivity.this, "O email xa está en uso.");
                                    } else {
                                        Util.amosarErro(RexistroActivity.this, "Produciuse un erro.\nInténteo máis tarde.");
                                    }
                                }
                            }
                        });

            } catch (Exception ex) {

            }
        }
    }

    private boolean validarCampos() {
        boolean correcto = false;
        if(!Util.validarEmail(etEmail.getText().toString())){
            Util.amosarErro(RexistroActivity.this, "O email non é válido");
        } else if(!Util.getTextoInput(etEmail).equalsIgnoreCase(Util.getTextoInput(etRepiteEmail))) {
            Util.amosarErro(RexistroActivity.this, "Os emails non son iguais");
        } else if(Util.getTextoInput(etContrasinal).length()<6){
            Util.amosarErro(RexistroActivity.this, "O contrasinal debe ter un mínimo de 6 caracteres");
        } else if(!Util.getTextoInput(etContrasinal).equalsIgnoreCase(Util.getTextoInput(etRepiteContrasinal))) {
            Util.amosarErro(RexistroActivity.this, "Os contrasinais non son iguais");
        } else if(Util.getTextoInput(etNome).isEmpty()){
            Util.amosarErro(RexistroActivity.this, "Debe introducir un nome.");
        } else if(Util.getTextoInput(etApelidos).isEmpty()){
            Util.amosarErro(RexistroActivity.this, "Debe introducir un apelido.");
        } else {
            correcto = true;
        }
        return correcto;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rexistro);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        findViews();
    }
}
