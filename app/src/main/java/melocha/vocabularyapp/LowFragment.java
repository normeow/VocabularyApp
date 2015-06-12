package melocha.vocabularyapp;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 11.03.2015.
 */
public class LowFragment extends android.support.v4.app.Fragment{

    public interface OnFullWindClick {
        public void onFullWind();
    }

    public void resetAdapter(){
        pagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    OnFullWindClick onFullWindClick;

    static final String TAG = "myLogs";

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.low_fragment, null);

        pager = (ViewPager)view.findViewById(R.id.pager);
        resetAdapter();
        return view;
    }


    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return MainActivity.getCountPairs();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lowfragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
   public void onAttach(Activity activity) {
       super.onAttach(activity);
       onFullWindClick = (OnFullWindClick)activity;
   }
}

