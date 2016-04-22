package melocha.vocabularyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tatiana on 19/04/2016.
 */
public class PairAdapter extends ArrayAdapter<EngRusPair> {
    private List<EngRusPair> pairs;
    public PairAdapter(Context context, int resource, List<EngRusPair> pairs) {
        super(context, resource, pairs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       EngRusPair item = getItem(position);
        //// TODO: custom list item with edit and delete buttons coz' context menu is bad idea 
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, null);
        ((TextView)convertView.findViewById(android.R.id.text1)).setText(item.getEngWord());
        ((TextView)convertView.findViewById(android.R.id.text2)).setText(item.getRusWord());

        return convertView;
    }

}