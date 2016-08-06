package melocha.vocabularykeeper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AddFragment.OnWordsChangeListener, EditFragmentDialog.EditDialogListener, AddFragment.OnItemAddListener, PairAdapter.ICallback {

    private ArrayList<Fragment> fragmenents;
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
    public void onWordsChanged() {
        wordsListFragment.updateList();
    }

    @Override
    public void onDialogPositiveClick(EngRusPair pair) {
        db.updateRusEngPair(pair);
        onWordsChanged();
        Toast toast = Toast.makeText(this, getResources().getString(R.string.toast_edited), Toast.LENGTH_SHORT);
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
    protected void onDestroy() {
        vp.setAdapter(null);
        super.onDestroy();
    }

    @Override
    public void onEditClick(EngRusPair pair) {
        android.support.v4.app.DialogFragment dialog = EditFragmentDialog.newInstance(pair);
        dialog.show(getSupportFragmentManager(), "editFragmentShow");
    }

    @Override
    public void onDeleteClick(final EngRusPair pair) {
        boolean deleted = false;
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.delete_item_titlt))
                .setMessage(getResources().getString(R.string.delete_item_msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteItem(pair);
                        sayItemDeleted();
                        wordsListFragment.updateList();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .show();

    }

    private void sayItemDeleted(){
            Toast toast = Toast.makeText(this, getResources().getString(R.string.toast_deleted), Toast.LENGTH_SHORT);
    }

}
