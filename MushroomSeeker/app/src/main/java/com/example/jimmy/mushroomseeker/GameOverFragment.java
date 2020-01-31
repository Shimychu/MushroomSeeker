package com.example.jimmy.mushroomseeker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Jimmy on 2/18/2018.
 */

public class GameOverFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create View
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.gameovermessagelayout,null);

        //Button Listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getActivity().finish();
            }
        };

        //BuildAlert Dialogbox
        return new AlertDialog.Builder(getActivity())
                .setTitle("Mushrooms Found!")
                .setView(v)
                .setPositiveButton(R.string.return_to_menu,listener)
                .create();

    }
}
