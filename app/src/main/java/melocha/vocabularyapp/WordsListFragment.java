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

    public interface OnWordDeleteListener{
        void onWordDelete(EngRusPair pair);
    }
    AddFragment.OnWordsChangeListener onWordsChangeListener;
    OnWordDeleteListener onWordDeleteListener;
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, "Delete item");
        menu.add(0, 1, 0, "Edit item");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id = (int)(adapterContextMenuInfo.id);
        switch (item.getItemId())
        {
            case MENU_DELETE:
                onWordDeleteListener.onWordDelete(pairs.get(id));
                updateList();
                onWordsChangeListener.onWordsChanged();
                break;
            case MENU_EDIT:
                android.support.v4.app.DialogFragment dialog = EditFragmentDialog.newInstance(pairs.get(id));
                dialog.show(getFragmentManager(),"editFragmentShow");
                updateList();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onWordsChangeListener = (AddFragment.OnWordsChangeListener)activity;
        onWordDeleteListener = (OnWordDeleteListener)activity;
        mActivity = activity;
        Log.v(TAG, "onAttach");
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            onWordsChangeListener = (AddFragment.OnWordsChangeListener) context;
            onWordDeleteListener = (OnWordDeleteListener) context;
            mActivity = (Activity)context;
        }
        Log.v(TAG, "onAttach");
    }
}
