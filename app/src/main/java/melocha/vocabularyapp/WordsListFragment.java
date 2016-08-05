package melocha.vocabularyapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Admin on 11.03.2015.
 */
public class WordsListFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "WordsListFragment";
    private PairAdapter aa;
    private ListView mListView;
    private ArrayList<EngRusPair> pairs;

    private static final int MENU_DELETE = 0;
    private static final int MENU_EDIT = 1;

    Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.wordslist_fragment, container, false);
        mListView = (ListView)view.findViewById(R.id.wordsList);
        updateList();
        registerForContextMenu(mListView);

        return view;
    }


    public void updateList(){
        pairs = MainActivity.getAllVocabularyPairs();
        aa = new PairAdapter(mActivity, android.R.layout.simple_list_item_2, pairs);
        mListView.setAdapter(aa);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity)context;
        }
        Log.v(TAG, "onAttach");
    }

}
