package melocha.vocabularyapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AddFragment.OnBtnWordsListListener, AddFragment.OnWordsChangeListener, EditFragmentDialog.EditDialogListener {
    //todo поиск слов и сравнение в выпадающем листе

    private android.support.v4.app.FragmentManager fragmentManager;
    public static MySQLHelper db;
    private static ArrayList<EngRusPair> allVocabularyPairs;
    private static ArrayList<String> allVocabularyStrings;
    private TabLayout tl;
    private ViewPager vp;
    private AddFragment addFragment;
    private WordsListFragment wordsListFragment;
    private static final String TAG = "MainActivity";


    public static ArrayList<EngRusPair> getAllVocabularyPairs() {
        allVocabularyPairs = db.getAllPairs();
        return allVocabularyPairs;
    }

    public static ArrayList<String> getAllVocabularyStrings() {
        allVocabularyPairs = getAllVocabularyPairs();
        return pairsToString(allVocabularyPairs);
    }

    public static int getCountPairs(){
        allVocabularyStrings = getAllVocabularyStrings();
        return allVocabularyStrings.size();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MySQLHelper(this);
        allVocabularyPairs = db.getAllPairs();
        allVocabularyStrings =  pairsToString(allVocabularyPairs);
        addFragment = new AddFragment();;
        wordsListFragment = new WordsListFragment();
        setContentView(R.layout.activity_main);
        vp = (ViewPager)findViewById(R.id.viewPager);
        tl = (TabLayout)findViewById(R.id.tl);
        vp.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0)
                    return addFragment;
                else {
                    return wordsListFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(position == 0)
                    return getResources().getString(R.string.addMode);
                else
                    return getResources().getString(R.string.viewMode);
            }
        });
        tl.setupWithViewPager(vp);
        Log.v(TAG, getResources().getConfiguration().locale.toString());
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.tb));

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            finish();
        }
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

}
