package me.carlosjai.tendavirtual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import me.carlosjai.tendavirtual.modelos.Usuario;

public class UsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private Usuario usuario;
    private TextView datosUsuario;
    private Button btnSair;
    private Button btnCompras;
    private Button btnTramite;
    private Button btnPedido;
    private ImageView avatar;

    private void findViews() {
        avatar = findViewById(R.id.imageView);
        datosUsuario = (TextView)findViewById( R.id.datosUsuario );
        btnSair = (Button)findViewById( R.id.btnSair );
        btnCompras = (Button)findViewById( R.id.btnCompras );
        btnTramite = (Button)findViewById( R.id.btnTramite );
        btnPedido = (Button)findViewById( R.id.btnPedido );

        btnSair.setOnClickListener( this );
        btnCompras.setOnClickListener( this );
        btnTramite.setOnClickListener( this );
        btnPedido.setOnClickListener( this );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        findViews();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if(!usuario.isAdmin()){
            btnSair.setVisibility(View.VISIBLE);
            btnTramite.setVisibility(View.VISIBLE);
            btnPedido.setVisibility(View.VISIBLE);
            btnCompras.setVisibility(View.VISIBLE);
        } else {
            avatar.setImageResource(R.drawable.admin);
        }
        datosUsuario.setText(usuario.getNomeApelidos());

    }

    @Override
    public void onClick(View v) {
        if ( v == btnSair ) {
            this.finish();
        } else if ( v == btnCompras ) {
            Intent intent = new Intent(this, ComprasRealizadasActivity.class);
            intent.putExtra("usuario", usuario );

            startActivity(intent);
        } else if ( v == btnTramite ) {
            Intent intent = new Intent(this, PedidosEnTramiteActivity.class);
            intent.putExtra("usuario", usuario );

            startActivity(intent);
        } else if ( v == btnPedido ) {
            Intent intent = new Intent(this, FacerPedidoActivity.class);
            intent.putExtra("usuario", usuario );

            startActivity(intent);
        }
    }
}
