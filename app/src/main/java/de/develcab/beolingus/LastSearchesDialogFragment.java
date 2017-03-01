package de.develcab.beolingus;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.List;

/**
 * Created by jb on 01.03.17.
 */

public class LastSearchesDialogFragment extends DialogFragment {
    private static final String TAG = LastSearchesDialogFragment.class.getName();

    public interface LastSearchChosenListener {
        void searchTermChosen(String searchTerm);
    }

    private List<String> searchTerms;
    private LastSearchChosenListener listener;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        searchTerms = args.getStringArrayList(MainActivity.SEARCH_TERMS_ARGUMENT_KEY);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.last_searches_dialog_title)
                .setItems(searchTerms.toArray(new CharSequence[searchTerms.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Log.d(TAG, "chosen: " + which);
                        listener.searchTermChosen(searchTerms.get(which));
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  LastSearchChosenListener) {
            listener = (LastSearchChosenListener)context;
        } else {
            Log.e(TAG, "The given context was not of type LastSearchChosenListener, can't do much with it");
        }
    }
}
