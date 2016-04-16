package melocha.vocabularyapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Admin on 19.04.2015.
 */
public class EditFragmentDialog extends DialogFragment {
    AddFragment.OnWordsChangeListener onWordsChangeListener;
    public interface EditDialogListener{
        void onDialogPositiveClick(EngRusPair pair);
       void onDialogNegativeClick();
    }

    String engWord;
    String rusWords;
    int id;
    EditDialogListener mListener;
    TextView engTV, rusTV;

    public  static EditFragmentDialog newInstance(EngRusPair pair)
    {
        EditFragmentDialog dialog = new EditFragmentDialog();
        Bundle bundle = new Bundle();
        //todo передавать экземпляр класса целиком, EngRusWord наследует Parcelable
        bundle.putString("EngWord", pair.getEngWord());
        bundle.putString("RusWords", pair.getRusWord());
        bundle.putInt("Id", pair.getId());
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        engWord = getArguments().getString("EngWord");
        rusWords = getArguments().getString("RusWords");
        id = getArguments().getInt("Id");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_fragment_dialog, null);

        engTV = (TextView) view.findViewById(R.id.engEdit);
        engTV.setText(engWord);

        rusTV = (TextView) view.findViewById(R.id.rusEdit);
        rusTV.setText(rusWords);

        builder.setMessage(R.string.edit_item)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(new EngRusPair(engTV.getText().toString(), rusTV.getText().toString(), id));

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onWordsChangeListener = (AddFragment.OnWordsChangeListener)activity;
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EditDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
