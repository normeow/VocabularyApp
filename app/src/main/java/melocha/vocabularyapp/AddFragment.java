package melocha.vocabularyapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFragment extends android.support.v4.app.Fragment {

    public interface OnBtnWordsListListener{
        void onBtnWordsListClick();
    }

    public  interface OnWordsChangeListener {
        void onWordsChanged();
    }

    private Button addBtn;
    private Button clearBtn;
    private EditText engEditText;
    private EditText rusEditText;

    OnBtnWordsListListener onBtnWordsListListener;
    OnWordsChangeListener onBtnAddClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        engEditText = (EditText)view.findViewById(R.id.engWordEdiTxt);
        rusEditText = (EditText)view.findViewById(R.id.rusWordEdiTxt);
        addBtn = (Button)view.findViewById(R.id.btnAddItem);
        clearBtn = (Button)view.findViewById(R.id.btnClear);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eTxt = engEditText.getText().toString();
                String rTxt = rusEditText.getText().toString();
                if (!(rTxt.isEmpty() || eTxt.isEmpty())) {
                    EngRusPair engRusPair = new EngRusPair(eTxt, rTxt);
                    MainActivity.db.addEngRusPair(engRusPair);
                    rusEditText.setText("");
                    engEditText.setText("");
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
                rusEditText.setText("");
                engEditText.setText("");
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