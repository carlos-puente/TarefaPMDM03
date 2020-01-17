package me.carlosjai.tendavirtual;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.carlosjai.tendavirtual.adapter.PedidosAdapter;
import me.carlosjai.tendavirtual.modelos.Usuario;

public class PedidosEnTramiteActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_en_tramite);
        db = FirebaseFirestore.getInstance();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        recyclerView = (RecyclerView) findViewById(R.id.rvPedidosTramite);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        crearReciclerView();
    }

    private void crearReciclerView() {
    if(!usuario.isAdmin()) {
        db.collection("pedidos")
                .whereEqualTo("id_usuario", usuario.getId()).whereEqualTo("en_tramite", true).whereEqualTo("rexeitado", false)
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
    } else {
        db.collection("pedidos").whereEqualTo("en_tramite", true).orderBy("fecha")
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