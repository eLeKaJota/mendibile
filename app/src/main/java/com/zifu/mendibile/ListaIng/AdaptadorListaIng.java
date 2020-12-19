package com.zifu.mendibile.ListaIng;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.Modelos.Plato;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaIng extends RecyclerView.Adapter<AdaptadorListaIng.ingViewHolder> {
    private List<Ingrediente> ing;
    final private ListItemClick ingOnClickListener;
    final BBDDHelper helper;
    ListaIngredientes listaIng;
    String moneda,monedaSimbolo;

    public interface ListItemClick{
        void onListItemClick(int clickedItem);

    }

    public AdaptadorListaIng(ArrayList<Ingrediente> ing, ListItemClick listener, BBDDHelper helper, ListaIngredientes listaIng){
        this.ing = ing;
        this.helper = helper;
        ingOnClickListener = listener;
        this.listaIng = listaIng;
        SharedPreferences ajustes = MainActivity.context.getSharedPreferences("com.zifu.mendibil", Context.MODE_PRIVATE);
        moneda = ajustes.getString("moneda","euro");
        switch (moneda){
            case "euro":
                monedaSimbolo = "€";
                break;
            case "dolar":
                monedaSimbolo = "$";
                break;
            case "libra":
                monedaSimbolo = "£";
                break;
            case "yen":
                monedaSimbolo = "¥";
                break;
            case "yuan":
                monedaSimbolo = "¥";
                break;
            default:
                monedaSimbolo = "€";
        }
    }

    public void filtro(ArrayList<Ingrediente> p){
        this.ing = new ArrayList<Ingrediente>();
        this.ing.addAll(p);
        notifyDataSetChanged();
    }



    public class ingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvListaIngNombre, tvListaIngPrecio, tvListaIngProveedor,tvListaIngId;
        public ImageButton btnDeleteIngList;
        public ingViewHolder(View itemView){
            super(itemView);
            tvListaIngNombre = itemView.findViewById(R.id.tvListaIngNombre);
            tvListaIngPrecio = itemView.findViewById(R.id.tvListaIngPrecio);
            tvListaIngProveedor = itemView.findViewById(R.id.tvListaIngProveedor);
            tvListaIngId = itemView.findViewById(R.id.tvListaIngId);
            //btnDeleteIngList = itemView.findViewById(R.id.btnDeleteListIng);
            itemView.setOnClickListener(this);
        }
        void bind(int listaIndex){
            Ingrediente i = ing.get(listaIndex);
            tvListaIngNombre.setText(i.getNombre());
            tvListaIngPrecio.setText("Precio: " + i.getPrecio() + monedaSimbolo +" / " + i.getFormato());
            tvListaIngProveedor.setText("Proveedor: " + i.getProveedor());
            tvListaIngId.setText(""+i.getId());

        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            ingOnClickListener.onListItemClick(clickedItem);
        }
    }





    @NonNull
    @Override
    public ingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context mContext = parent.getContext();
        int layoutIdListIng = R.layout.lista_ing_row;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachToParentRapido = false;
        View view = inflater.inflate(layoutIdListIng,parent,attachToParentRapido);

        ingViewHolder viewHolder = new ingViewHolder(view);


        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ingViewHolder holder, int position) {




        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return ing.size();
    }
}
