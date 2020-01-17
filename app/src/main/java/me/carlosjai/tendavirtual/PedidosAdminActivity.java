package me.carlosjai.tendavirtual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.carlosjai.tendavirtual.adapter.PedidosAdapter;
import me.carlosjai.tendavirtual.adapter.PedidosAdminAdapter;
import me.carlosjai.tendavirtual.modelos.Compra;
import me.carlosjai.tendavirtual.modelos.Usuario;

public class PedidosAdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    private Usuario usuario;
    private String tipo;
    private TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_en_tramite);
        tipo = getIntent().getStringExtra("tipo");
        db = FirebaseFirestore.getInstance();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        recyclerView = (RecyclerView) findViewById(R.id.rvPedidosTramite);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        titulo = (TextView) findViewById(R.id.titulo);
        crearReciclerView();
    }

    private void crearReciclerView() {
        if (tipo.equalsIgnoreCase("tramite")) {
            titulo.setText("Ver pedidos en trámite");
            db.collection("pedidos").whereEqualTo("en_tramite", true).whereEqualTo("rexeitado", false).orderBy("fecha")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Map<String, Object>> lDataset = new ArrayList<>();
                            List<String> lIdPedido = new ArrayList<>();
                            if (task.isSuccessful()) {
                                int i = 1;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("DEBUG", document.getId() + " => " + document.getData());
                                    lDataset.add(document.getData());
                                    lIdPedido.add(document.getId());

                                    i++;
                                }
                                mAdapter = new PedidosAdminAdapter(lDataset, lIdPedido);
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                Log.d("ERROR", "Error getting documents: ", task.getException());
                            }

                        }
                    });

        } else if (tipo.equalsIgnoreCase("aceptados")) {
            titulo.setText("Ver pedidos aceptados");
            db.collection("pedidos").whereEqualTo("en_tramite", false).whereEqualTo("rexeitado", false).orderBy("fecha")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<String> lDataset = new ArrayList<>();
                            if (task.isSuccessful()) {
                                int i = 1;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("DEBUG", document.getId() + " => " + document.getData());

                                    lDataset.add(xerarDataset(i, document.getData()));
                                    i++;
                                }
                                mAdapter = new PedidosAdapter(lDataset.toArray(new String[lDataset.size()]));
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                Log.d("ERROR", "Error getting documents: ", task.getException());
                            }

                        }
                    });

        } else if (tipo.equalsIgnoreCase("rexeitados")) {
            titulo.setText("Ver pedidos rexeitados");
            db.collection("pedidos").whereEqualTo("rexeitado", true).orderBy("fecha")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<String> lDataset = new ArrayList<>();
                            if (task.isSuccessful()) {
                                int i = 1;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("DEBUG", document.getId() + " => " + document.getData());

                                    lDataset.add(xerarDataset(i, document.getData()));
                                    i++;
                                }
                                mAdapter = new PedidosAdapter(lDataset.toArray(new String[lDataset.size()]));
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                Log.d("ERROR", "Error getting documents: ", task.getException());
                            }

                        }
                    });
        }


    }

    private String xerarDataset(int num, Map<String, Object> data) {
        String str = "";
        str += "Pedido " + num + ":\n-producto: " + data.get("producto");
        str += "\n-categoria: " + data.get("categoria");
        str += "\n-cantidade: " + data.get("cantidad");
        Timestamp stamp = (Timestamp) data.get("fecha");
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String strDate = sm.format(stamp.toDate());
        str += "\n-data pedido:" + strDate;
        str += "\n-Direccion entrega:\n";
        Map<String, Object> mDireccion = (Map<String, Object>) data.get("direccion");
        str += mDireccion.get("direccion") + ", " + mDireccion.get("cidade") + " (" + mDireccion.get("codigo_postal") + ")";

        return str;
    }
}
