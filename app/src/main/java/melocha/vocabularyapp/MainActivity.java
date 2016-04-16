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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchFragment.SearchFragmentListener, AddFragment.OnBtnWordsListListener, AddFragment.OnWordsChangeListener, EditFragmentDialog.EditDialogListener {
    //todo поиск слов и сравнение в выпадающем листе

    private android.support.v4.app.FragmentManager fragmentManager;
    public static MySQLHelper db;
    private static ArrayList<EngRusPair> allVocabularyPairs;
    private static ArrayList<String> allVocabularyStrings;
    private TabLayout tl;
    private ViewPager vp;


    public static ArrayList<EngRusPair> getAllVocabularyPairs() {
        allVocabularyPairs = db.getAllPairs();
        return allVocabularyPairs;
    }

    public static ArrayList<String> getAllVocabularyStrings() {
        allVocabularyPairs = getAllVocabularyPairs();
        return pairsToString(allVocabularyPairs);
    }

    public  static int getCountPairs(){
        allVocabularyStrings = getAllVocabularyStrings();
        return allVocabularyStrings.size();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MySQLHelper(this);
        allVocabularyPairs = db.getAllPairs();
        allVocabularyStrings =  pairsToString(allVocabularyPairs);

        setContentView(R.layout.activity_main);
        vp = (ViewPager)findViewById(R.id.viewPager);
        tl = (TabLayout)findViewById(R.id.tl);
        vp.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0)
                    return new AddFragment();
                else
                    return new WordsListFragment();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_search:
                //todo Search
                openSearch(null);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSearch(ArrayList<EngRusPair> arrayList){
        DialogFragment dialog = SearchFragment.newInstance(arrayList);
        dialog.show(getSupportFragmentManager(), "SearchFragment");
    }


    @Override
    public void onBtnWordsListClick() {
    }

    @Override
    public void onWordsChanged() {

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

    //todo isRussian
    public static boolean isRussian(String string){
        return true;
    }

    //todo isEnglish
    public static boolean isEnglish(String string){
        return true;
    }

    @Override
    public void onDialogPositiveClockListener(String searchString) {
        //todo search only in eng-rus table ??
        ArrayList<EngRusPair> list = new ArrayList<EngRusPair>();
        int i = 0;
        for (String t : allVocabularyStrings){
            if (t.toLowerCase().contains(searchString.toLowerCase())) {
                list.add(allVocabularyPairs.get(i));
            }
            i += 1;
        }

        openSearch(list);
    }
}
