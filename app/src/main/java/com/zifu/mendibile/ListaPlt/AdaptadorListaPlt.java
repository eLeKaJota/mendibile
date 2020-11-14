package com.zifu.mendibile.ListaPlt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.Modelos.Plato;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaPlt extends RecyclerView.Adapter<AdaptadorListaPlt.pltViewHolder> {
    private List<Plato> plt;
    final private AdaptadorListaPlt.ListItemClick pltOnClickListener;
    final BBDDHelper helper;
    ListaPlatos listaPlt;

    public interface ListItemClick{
        void onListItemClick(int clickedItem);

    }

    public AdaptadorListaPlt(ArrayList<Plato> plt, AdaptadorListaPlt.ListItemClick listener, BBDDHelper helper, ListaPlatos listaPlt){
        this.plt = plt;
        this.helper = helper;
        pltOnClickListener = listener;
        this.listaPlt = listaPlt;
    }




    public class pltViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvListaPltNombre, tvListaPltCoste, tvListaPltId;
        public ImageButton btnDeletePltList;
        public pltViewHolder(View itemView){
              super(itemView);
              tvListaPltNombre = itemView.findViewById(R.id.tvListaPlatoNombre);
              tvListaPltCoste = itemView.findViewById(R.id.tvListaPlatoCoste);
              tvListaPltId = itemView.findViewById(R.id.tvListaPlatoId);
              //btnDeletePltList = itemView.findViewById(R.id.btnDeleteListPlato);
              itemView.setOnClickListener(this);
        }
        void bind(int listaIndex){
            Plato i = plt.get(listaIndex);

            tvListaPltNombre.setText(i.getNombre());
            tvListaPltCoste.setText("Precio: " + (double)Math.round(i.getCoste()*100)/100 + "€");
            tvListaPltId.setText(""+i.getId());

//            btnDeletePltList.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listaPlt.borrarPlato(TablaPlato.NOMBRE_COLUMNA_1,tvListaPltId.getText().toString(),TablaPlato.NOMBRE_TABLA);
//                    listaPlt.borrarPlato(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2,tvListaPltId.getText().toString(),TablaPlatoIngredientePeso.NOMBRE_TABLA);
//                }
//            });




        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            pltOnClickListener.onListItemClick(clickedItem);
        }
    }





    @NonNull
    @Override
    public AdaptadorListaPlt.pltViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context mContext = parent.getContext();
        int layoutIdListPlt = R.layout.lista_plato_row;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachToParentRapido = false;
        View view = inflater.inflate(layoutIdListPlt,parent,attachToParentRapido);

        AdaptadorListaPlt.pltViewHolder viewHolder = new AdaptadorListaPlt.pltViewHolder(view);


        return viewHolder;

    }



    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaPlt.pltViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return plt.size();
    }
}
