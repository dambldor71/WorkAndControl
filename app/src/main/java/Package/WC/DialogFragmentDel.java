package Package.WC;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class DialogFragmentDel extends DialogFragment {
    private Removable removable;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        removable = (Removable) context;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String code = getArguments().getString("code");
        final String text = getArguments().getString("text");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Выберите действие")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Вы действительно хотите удалить смену " + code + "?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removable.remove(text);
                    }
                })
                .setNegativeButton("Отмена", null)
                .create();
    }
}