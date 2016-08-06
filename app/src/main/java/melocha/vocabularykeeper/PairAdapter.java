package melocha.vocabularykeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tatiana on 19/04/2016.
 */
public class PairAdapter extends ArrayAdapter<EngRusPair> {
    public interface ICallback{
        void onEditClick(EngRusPair pair);
        void onDeleteClick(EngRusPair pair);
    }
    ICallback listener;
    private List<EngRusPair> pairs;
    public PairAdapter(Context context, int resource, List<EngRusPair> pairs) {
        super(context, resource, pairs);
        listener = (ICallback)context;
        this.pairs = pairs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       EngRusPair item = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        ((TextView)convertView.findViewById(R.id.text1)).setText(item.getEngWord());
        ((TextView)convertView.findViewById(R.id.text2)).setText(item.getRusWord());

        ImageView delbtn = (ImageView)convertView.findViewById(R.id.del_imv);
        ImageView editbtn = (ImageView)convertView.findViewById(R.id.editimv);
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(pairs.get(position));
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditClick(pairs.get(position));
            }
        });

        return convertView;
    }

}