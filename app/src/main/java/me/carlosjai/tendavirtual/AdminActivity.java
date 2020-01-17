package me.carlosjai.tendavirtual;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.carlosjai.tendavirtual.modelos.Usuario;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private Usuario usuario;
    private TextView datosUsuario;
    private Button btnSair;
    private Button btnRexeitados;
    private Button btnTramite;
    private Button btnAceptados;
    private ImageView avatar;

    private void findViews() {
        avatar = findViewById(R.id.adminImageView);
        datosUsuario = (TextView)findViewById( R.id.datosAdmin );
        btnSair = (Button)findViewById( R.id.btnAdminSair );
        btnRexeitados = (Button)findViewById( R.id.btnPedidosRexeitados);
        btnTramite = (Button)findViewById( R.id.btnAdminTramite );
        btnAceptados = (Button)findViewById( R.id.btnAdminPedidoAceptado);

        btnSair.setOnClickListener( this );
        btnRexeitados.setOnClickListener( this );
        btnTramite.setOnClickListener( this );
        btnAceptados.setOnClickListener( this );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findViews();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if(!usuario.isAdmin()){
            btnSair.setVisibility(View.VISIBLE);
            btnTramite.setVisibility(View.VISIBLE);
            btnAceptados.setVisibility(View.VISIBLE);
            btnRexeitados.setVisibility(View.VISIBLE);
        } else {
            avatar.setImageResource(R.drawable.admin);
        }
        datosUsuario.setText(usuario.getNomeApelidos());

    }

    @Override
    public void onClick(View v) {
        if ( v == btnSair ) {
            this.finish();
        } else if ( v == btnRexeitados ) {
            Intent intent = new Intent(this, PedidosAdminActivity.class);
            intent.putExtra("usuario", usuario );
            intent.putExtra("tipo", "rexeitados");
            startActivity(intent);
        } else if ( v == btnTramite ) {
            Intent intent = new Intent(this, PedidosAdminActivity.class);
            intent.putExtra("usuario", usuario );
            intent.putExtra("tipo", "tramite");

            startActivity(intent);
        } else if ( v == btnAceptados ) {
            Intent intent = new Intent(this, PedidosAdminActivity.class);
            intent.putExtra("usuario", usuario );
            intent.putExtra("tipo", "aceptados");
            startActivity(intent);
        }
    }
}
