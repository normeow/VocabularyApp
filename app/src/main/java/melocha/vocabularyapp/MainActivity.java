package melocha.vocabularyapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AddFragment.OnBtnWordsListListener, AddFragment.OnWordsChangeListener, EditFragmentDialog.EditDialogListener, AddFragment.OnItemAddListener, WordsListFragment.OnWordDeleteListener {
    //todo поиск слов и сравнение в выпадающем листе
    private ArrayList<Fragment> fragmenents;
    private android.support.v4.app.FragmentManager fragmentManager;
    private static MySQLHelper db;
    private TabLayout tl;
    private ViewPager vp;
    private AddFragment addFragment;
    private WordsListFragment wordsListFragment;
    private static final String TAG = "MainActivity";


    public static ArrayList<EngRusPair> getAllVocabularyPairs() {
        return db.getAllPairs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        Log.v(TAG, "Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MySQLHelper(this);
        addFragment = new AddFragment();
        wordsListFragment = new WordsListFragment();
        fragmenents = new ArrayList<>();
        fragmenents.add(addFragment);
        fragmenents.add(wordsListFragment);
        vp = (ViewPager) findViewById(R.id.viewPager);
        tl = (TabLayout) findViewById(R.id.tl);
        vp.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmenents.get(position);
            }

            @Override
            public int getCount() {
                return fragmenents.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0)
                    return getResources().getString(R.string.addMode);
                else
                    return getResources().getString(R.string.viewMode);
            }
        });
        tl.setupWithViewPager(vp);
        //Log.v(TAG, getResources().getConfiguration().locale.toString());
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.tb));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("viewpagerid" , vp.getId() );
    }

    public static ArrayList<String> pairsToString(ArrayList<EngRusPair> pairsArray){
        ArrayList<String> res = new ArrayList<String>();
        for (EngRusPair pair : pairsArray){
            res.add(pair.toString());
        }
        return res;
    }

    @Override
    public void onBtnWordsListClick() {
    }

    @Override
    public void onWordsChanged() {
        wordsListFragment.updateList();
    }

    @Override
    public void onDialogPositiveClick(EngRusPair pair) {
        db.updateRusEngPair(pair);
        onWordsChanged();
        Toast toast = Toast.makeText(this, "Item successfully edited", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public void onItemAdd(EngRusPair pair) {
       db.addEngRusPair(pair);
    }

    @Override
    public void onWordDelete(EngRusPair pair) {
        db.deleteItem(pair);
    }

    @Override
    protected void onDestroy() {
        vp.setAdapter(null);
        super.onDestroy();
    }
}
