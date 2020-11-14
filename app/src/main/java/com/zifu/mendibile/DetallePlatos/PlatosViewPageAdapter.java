package com.zifu.mendibile.DetallePlatos;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

public class PlatosViewPageAdapter extends FragmentStateAdapter {
    public PlatosViewPageAdapter(@NonNull FragmentActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = new ResumenPlatoFragment();
        Fragment fragment2 = new IngredientesPlatoFragment();
        Fragment fragment3 = new RecetaPlatoFragment();

        if (position == 0) return fragment;
        if (position == 1) return fragment2;
        if (position == 2) return fragment3;


//        Bundle args = new Bundle();
//        args.putInt(ResumenPlatoFragment.ARG_OBJECT, position + 1);
//        fragment.setArguments(args);



        return null;

    }



    @Override
    public int getItemCount() {
        return 3;
    }


}
