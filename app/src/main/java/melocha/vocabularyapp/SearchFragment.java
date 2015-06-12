package melocha.vocabularyapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Admin on 19.04.2015.
 */
public class SearchFragment extends DialogFragment{
    public interface SearchFragmentListener{
        public void onDialogPositiveClockListener(String searchString);

    }

    public static SearchFragment newInstance(ArrayList<EngRusPair> arrayList){
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        if (arrayList != null) {

            args.putParcelableArrayList("PairList", arrayList);
            args.putBoolean("IsEmpty", false);
        }
        else{
            args.putBoolean("IsEmpty", true);
        }
        fragment.setArguments(args);

        return fragment;
    }

    private EditText mEditText;
    private ListView mListView;
    private ArrayList<String> list;
    private ArrayList<EngRusPair> engRusPairs;
    private ArrayAdapter<String> aa;
    private SearchFragmentListener mListener;

    private void changeList(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!getArguments().getBoolean("IsEmpty")){
            engRusPairs = getArguments().getParcelableArrayList("PairList");
            list = new ArrayList<String>();
            for (EngRusPair item : engRusPairs){
                list.add(item.toString());
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search_fragment_dialog, null);
        mEditText = (EditText)view.findViewById(R.id.search_edit_text);

        if (list != null) {
            aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        }
        else
        {   //todo say that find nothing
            //aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>())
        }
        mListView = (ListView)view.findViewById(R.id.search_listview);
        mListView.setAdapter(aa);
        registerForContextMenu(mListView);

        builder.setView(view)
                .setMessage("Looking for...")
                .setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClockListener(mEditText.getText().toString());

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
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
        //todo update listview
        switch (id){
            case 0 :
                MainActivity.db.deleteItem(engRusPairs.get(id));
                break;
            case 1 :
                android.support.v4.app.DialogFragment dialog = EditFragmentDialog.newInstance(engRusPairs.get(id));
                dialog.show(getFragmentManager(),"editFragmentShow");
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (SearchFragmentListener)activity;
    }
}
