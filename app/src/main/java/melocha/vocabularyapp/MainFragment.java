package melocha.vocabularyapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 21.03.2015.
 */
public class MainFragment extends android.support.v4.app.Fragment {

    public LowFragment mLowFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mLowFragment = new LowFragment();
        fragmentTransaction.add(R.id.top_fragment_container, new TopFragment());
        fragmentTransaction.add(R.id.low_fragment_container,  mLowFragment);
        fragmentTransaction.commit();
        return view;
    }
}
