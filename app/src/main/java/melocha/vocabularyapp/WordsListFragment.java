package melocha.vocabularyapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private ArrayAdapter<String> aa;
    private ListView mListView;
    private ArrayList<EngRusPair> pairs;
    private ArrayList<String> strings;

    final int MENU_DELETE = 0;
    final int MENU_EDIT = 1;

    AddFragment.OnWordsChangeListener onWordsChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wordslist_fragment, container, false);
        mListView = (ListView)view.findViewById(R.id.wordsList);
        updateList();
        registerForContextMenu(mListView);
        return view;
    }

    public void updateList(){
        pairs = MainActivity.getAllVocabularyPairs();
        strings = MainActivity.getAllVocabularyStrings();
        aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strings);
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
                MainActivity.db.deleteItem(pairs.get(id));
                updateList();
                onWordsChangeListener.onWordsChanged();
                break;
            case MENU_EDIT:

                //todo EDIT item
               // Bundle bundle = new Bundle();
               // bundle.putString("EngWord", pairs.get(id).getEngWord());
               // bundle.putStringArrayList("RusWordsList", pairs.get(id).getRusWords());

                android.support.v4.app.DialogFragment dialog = EditFragmentDialog.newInstance(pairs.get(id));
                dialog.show(getFragmentManager(),"editFragmentShow");
                updateList();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onWordsChangeListener = (AddFragment.OnWordsChangeListener)activity;
    }
}
