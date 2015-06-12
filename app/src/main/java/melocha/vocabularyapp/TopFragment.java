package melocha.vocabularyapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin on 16.03.2015.
 */
public class TopFragment extends android.support.v4.app.Fragment {

    public interface OnBtnWordsListListener{
        public void onBtnWordsListClick();
    }

    public  interface OnWordsChangeListener {
        public void onWordsChanged();
    }

    private Button addBtn;
    private Button clearBtn;
    private Button openWholeVocabularyBtn;
    private EditText engTxt;
    private EditText rusTxt;

    OnBtnWordsListListener onBtnWordsListListener;
    OnWordsChangeListener onBtnAddClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_fragment, container, false);
        engTxt = (EditText)view.findViewById(R.id.engWordEdiTxt);
        rusTxt = (EditText)view.findViewById(R.id.rusWordEdiTxt);
        openWholeVocabularyBtn = (Button)view.findViewById(R.id.openVocabularyBtn);
        addBtn = (Button)view.findViewById(R.id.btnAddItem);
        clearBtn = (Button)view.findViewById(R.id.btnClear);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eTxt = engTxt.getText().toString();
                String rTxt = rusTxt.getText().toString();
                //todo использовать новый конструктор и не сплитить непосредственно тут
                ArrayList<String> rusWords = new ArrayList<String>(Arrays.asList(rTxt.trim().split("[;]+")));
                if (!(rTxt.isEmpty() || eTxt.isEmpty())) {
                    EngRusPair engRusPair = new EngRusPair(eTxt, rusWords);
                    MainActivity.db.addEngRusPair(engRusPair);
                    rusTxt.setText("");
                    engTxt.setText("");
                    Toast toast = Toast.makeText(getActivity(), "New item successfully added", Toast.LENGTH_SHORT);
                    toast.show();
                    onBtnAddClickListener.onWordsChanged();
                }
                else {
                    Toast toast = Toast.makeText(getActivity(), "Fill both fields", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rusTxt.setText("");
                engTxt.setText("");
            }
        });

        openWholeVocabularyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnWordsListListener.onBtnWordsListClick();


            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onBtnWordsListListener = (OnBtnWordsListListener)activity;
        onBtnAddClickListener = (OnWordsChangeListener)activity;
    }
}
