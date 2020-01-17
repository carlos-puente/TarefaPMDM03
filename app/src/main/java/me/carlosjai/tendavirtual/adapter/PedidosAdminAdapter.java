package me.carlosjai.tendavirtual.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.carlosjai.tendavirtual.R;
import me.carlosjai.tendavirtual.modelos.Compra;

public class PedidosAdminAdapter extends RecyclerView.Adapter<PedidosAdminAdapter.MyHolder> {
    private List<Map<String,Object>> mDataset;
    private List<String> mIdPedido;
    private Task<Void> db;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PedidosAdminAdapter(List<Map<String,Object>> myDataset, List<String> lIdPedido) {
        mDataset = myDataset;
        mIdPedido = lIdPedido;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent,
                                       int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.item_layout_admin, parent, false);

        MyHolder holder = new MyHolder( v, new MyHolder.MyClickListener(){
            @Override
            public void aceptar(int p) {
                Log.d("debug", "aceptando: "+mDataset.get(p).toString()+ "\nID:"+mIdPedido.get(p));
                marcarComoAceptado(mIdPedido.get(p));
                mDataset.remove(p);
                mIdPedido.remove(p);
                notifyItemRemoved(p);
                notifyItemRangeChanged(p, mDataset.size());
            }

            @Override
            public void rexeitar(int p) {
                Log.d("debug", "rexeitando: "+mDataset.get(p).toString()+ "\nID:"+mIdPedido.get(p));
                marcarComoRexeitado(mIdPedido.get(p));
                mDataset.remove(p);
                mIdPedido.remove(p);
                notifyItemRemoved(p);
                notifyItemRangeChanged(p, mDataset.size());
            }
        });
        return holder;
    }

    private void marcarComoAceptado(String idPedido) {
        Map<String, Object> data = new HashMap<>();
        data.put("en_tramite", false);
        db = FirebaseFirestore.getInstance().collection("pedidos").document(idPedido)
                .set(data, SetOptions.merge());
    }

    private void marcarComoRexeitado(String idPedido) {
        Map<String, Object> data = new HashMap<>();
        data.put("en_tramite", false);
        data.put("rexeitado", true);
        db = FirebaseFirestore.getInstance().collection("pedidos").document(idPedido)
                .set(data, SetOptions.merge());
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.textData.setText(xerarTextoPedido(position+1, mDataset.get(position)));
        holder.textData.setTag(mDataset.get(position).get("id_pedido"));
        holder.aceptar.setTag(mDataset.get(position).get("id_pedido"));
        holder.rexeitar.setTag(mDataset.get(position).get("id_pedido"));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private String xerarTextoPedido(int num, Map<String, Object> data) {
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
        str+="\n- Id Usuario: "+data.get("id_usuario");

        return str;
    }

    static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        MyClickListener listener;
        TextView textData;
        Button aceptar;
        Button rexeitar;

        //contructor for getting reference to the widget
        public MyHolder(View itemView, MyClickListener listener) {
            super(itemView);

            textData = (TextView) itemView.findViewById(R.id.item_title_admin);
            aceptar = (Button) itemView.findViewById(R.id.btnAceptarPedido);
            rexeitar = (Button) itemView.findViewById(R.id.btnRexeitarPedido);
            this.listener = listener;

            aceptar.setOnClickListener(this);
            rexeitar.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAceptarPedido:
                    listener.aceptar(this.getLayoutPosition());
                    break;
                case R.id.btnRexeitarPedido:
                    listener.rexeitar(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

        public interface MyClickListener {
            void aceptar(int p);
            void rexeitar(int p);
        }
    }

}