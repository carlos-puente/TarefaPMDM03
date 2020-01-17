package me.carlosjai.tendavirtual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import me.carlosjai.tendavirtual.modelos.Direccion;
import me.carlosjai.tendavirtual.modelos.Pedido;
import me.carlosjai.tendavirtual.modelos.Usuario;
import me.carlosjai.tendavirtual.utils.Util;

public class DireccionActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText direccion;
    private EditText codpostal;
    private EditText cidade;
    private Button btFinalizar;
    private Usuario usuario;
    private Pedido pedido;
    private FirebaseFirestore db;


    private void findViews() {
        direccion = (EditText) findViewById(R.id.direccion);
        codpostal = (EditText) findViewById(R.id.codpostal);
        cidade = (EditText) findViewById(R.id.cidade);
        btFinalizar = (Button) findViewById(R.id.btFinalizar);
        addListeners();
    }


    @Override
    public void onClick(View v) {
        if (v == btFinalizar) {
            pedido.setDireccion(new Direccion(direccion.getText().toString(), cidade.getText().toString(), codpostal.getText().toString()));

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            rexistrarPedido();
                            DireccionActivity.this.finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            DireccionActivity.this.finish();
                            break;
                    }

                }
            };
            String str = amosarInfoPedido();
            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
            builder.setMessage("Desexas confirmar o pedido?\n"+str).setPositiveButton("Confirmar Pedido", dialogClickListener)
                    .setNegativeButton("Cancelar Pedido", dialogClickListener).show();


        }
    }

    private void rexistrarPedido() {
        db.collection("pedidos")
                .add(pedido.toMap(usuario.getId()))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DEBUG", "DocumentSnapshot written with ID: " + documentReference.getId());
                        Util.amosarMensaxe(DireccionActivity.this,"Pedido realizado con éxito", Toast.LENGTH_LONG);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERROR", "Error adding document", e);
                        Util.amosarMensaxe(DireccionActivity.this,"Pedido non realizado\nInténtao máis tarde", Toast.LENGTH_LONG);
                    }
                });
    }

    private String amosarInfoPedido() {
        String str = "";
        str += "\nCategoria: " + pedido.getCategoria();
        str += "\nProduto: " + pedido.getProducto();
        str += "\nCantidade: " + pedido.getCantidad();
        str += "\nDatos de envío:\n";
        str += "\nDirección: " + pedido.getDireccion().getRua() + " (" + pedido.getDireccion().getCidade() + ")";
        str += "\nCP: " + pedido.getDireccion().getCodigoPostal();
        return str;
    }

    private void addListeners() {
        direccion.addTextChangedListener(new TextWatcher() {

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


        cidade.addTextChangedListener(new TextWatcher() {

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
        codpostal.addTextChangedListener(new TextWatcher() {

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

        btFinalizar.setOnClickListener(this);


    }

    private void comprobarBoton() {

        if (validarCodPostal() && validarDireccion() && validarCidade()) {
            btFinalizar.setEnabled(true);
        } else {
            btFinalizar.setEnabled(false);
        }
    }

    private boolean validarCodPostal() {
        return !Util.estaBaleira(codpostal.getText().toString()) && Util.isNumeric(codpostal.getText().toString());
    }


    private boolean validarDireccion() {
        return !Util.estaBaleira(direccion.getText().toString());
    }

    private boolean validarCidade() {
        return !Util.estaBaleira(cidade.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);
        findViews();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        pedido = (Pedido) getIntent().getSerializableExtra("pedido");
        db = FirebaseFirestore.getInstance();

    }
}