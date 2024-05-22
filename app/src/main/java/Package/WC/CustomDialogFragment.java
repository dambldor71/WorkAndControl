package Package.WC;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

public class CustomDialogFragment extends DialogFragment {
    TextView zigzag;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        String zagolo = getArguments().getString("zagol");
        String razrab = getArguments().getString("razrab");
        return builder
                .setTitle(zagolo)
                .setMessage(razrab)
                .create();
    }
}