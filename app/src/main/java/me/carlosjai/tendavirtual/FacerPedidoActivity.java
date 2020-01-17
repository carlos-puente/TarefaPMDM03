package me.carlosjai.tendavirtual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Calendar;

import me.carlosjai.tendavirtual.modelos.Pedido;
import me.carlosjai.tendavirtual.modelos.Usuario;

public class FacerPedidoActivity extends AppCompatActivity  implements View.OnClickListener{


    private ArrayAdapter<CharSequence> adapter;
    private Spinner spCategorias;
    private Spinner spSubCategorias;
    private Spinner spCantidade;
    private Button btSeguinte;
    private Usuario usuario;

    private void findViews() {
        spCategorias = findViewById(R.id.spCategorias);
        spSubCategorias = findViewById(R.id.spSubCategorias);
        spCantidade = findViewById( R.id.spCantidade );
        btSeguinte = findViewById( R.id.btSeguinte );

        btSeguinte.setOnClickListener( this );

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facer_pedido);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        findViews();


        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0) {
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.categorias_informática, android.R.layout.simple_spinner_item);
                } else if(position == 1){
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.categorias_electrónica, android.R.layout.simple_spinner_item);
                } else {
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.categorias_mobil, android.R.layout.simple_spinner_item);
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSubCategorias.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // todo
            }

        });

    }
    @Override
    public void onClick(View v) {
        if ( v == btSeguinte ) {
            Pedido pedido = new Pedido(null, spCategorias.getSelectedItem().toString(),spSubCategorias.getSelectedItem().toString(),Integer.parseInt(spCantidade.getSelectedItem().toString()), Calendar.getInstance());
            Intent intent = new Intent(this, DireccionActivity.class);
            intent.putExtra("usuario", usuario );
            intent.putExtra("pedido", pedido );
            startActivity(intent);
            this.finish();
        }
    }

}