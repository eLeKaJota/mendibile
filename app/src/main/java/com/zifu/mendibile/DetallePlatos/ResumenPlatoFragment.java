package com.zifu.mendibile.DetallePlatos;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zifu.mendibile.R;

/**
 * A simple {@link Fragment} subclass.

 */
public class ResumenPlatoFragment extends Fragment  {
    public static final String ARG_OBJECT = "object";
    private View vista;
    //private View vista;
    private DetallePlatos detalle;
    private ImageView fotoPlato;

    public ResumenPlatoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resumen_plato, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        detalle = (DetallePlatos) getActivity();
        fotoPlato = (ImageView) view.findViewById(R.id.ivFotoPlatoDetalle);
        this.vista = view;
        Bundle args = getArguments();
        ((TextView) view.findViewById(R.id.tvDetallePltNombre))
                .setText("Nombre: " + detalle.plato.getNombre());

        if(detalle.plato.getFoto() != null && !detalle.plato.getFoto().equals("null")){
            fotoPlato.setScaleType(ImageView.ScaleType.CENTER_CROP);
            fotoPlato.setImageURI(Uri.parse(detalle.plato.getFoto()));
        }
        actualizaCoste();

    }

    public void actualizaCoste(){
        ((TextView) vista.findViewById(R.id.tvDetallePltCoste))
                .setText("Coste: " +(double)Math.round( detalle.plato.getCoste()*100)/100 + "€");
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        actualizaCoste();
//    }
}