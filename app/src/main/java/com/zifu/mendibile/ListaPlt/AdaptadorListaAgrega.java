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
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaAgrega extends RecyclerView.Adapter<AdaptadorListaAgrega.ingViewHolder> {
    public List<Ingrediente> ing;
    final private ListItemClick ingOnClickListener;
    final BBDDHelper helper;
    AgregaPlato agregaPlato;

    public interface ListItemClick{
        void onListItemClick(int clickedItem);

    }

    public AdaptadorListaAgrega(ArrayList<Ingrediente> ing, ListItemClick listener, BBDDHelper helper, AgregaPlato agregaPlato){
       // this.ing = ing;
        this.helper = helper;
        ingOnClickListener = listener;
        this.agregaPlato = agregaPlato;
    }




    public class ingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvListaIngNombre;
        public ImageButton btnAgregaIngPlt;
        public ingViewHolder(View itemView){
            super(itemView);
            tvListaIngNombre = itemView.findViewById(R.id.tvAgregaIngNombre);
            btnAgregaIngPlt = itemView.findViewById(R.id.btnAgregaIngPlt);
            itemView.setOnClickListener(this);
        }
        void bind(int listaIndex){
            Ingrediente i = agregaPlato.ingredientes.get(listaIndex);
            tvListaIngNombre.setText(i.getNombre());

            btnAgregaIngPlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agregaPlato.ingAgregados.add(i);
                    agregaPlato.ingredientes.remove(listaIndex);

                    agregaPlato.adaptadorAgregado.notifyItemInserted(agregaPlato.ingAgregados.size() -1);
                    notifyDataSetChanged();
                    //notifyItemRemoved(listaIndex -1 );
                }
            });




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
        int layoutIdListIng = R.layout.lista_agrega_ing_plato;
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
        return agregaPlato.ingredientes.size();
    }
}
