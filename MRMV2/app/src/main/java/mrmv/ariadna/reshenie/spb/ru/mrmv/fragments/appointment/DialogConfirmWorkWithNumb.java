package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;

/**
 * Created by kirichenko on 04.09.2015.
 */
public class DialogConfirmWorkWithNumb extends DialogFragment{

    public static String KEY_FOR_TAKE_TITLE = "key_for_take_title";
    public static String KEY_FOR_TAKE_MESSAGE = "key_for_take_message";

    private TextView tvMessageDialog;

    private Button btnYes, btnNo;

    private String sTitle, sMessage;

    private IDialogConfirmWorkWithNumb IDialogConfirmWorkWithNumb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(getArguments() != null){
            sTitle =  getArguments().getString(KEY_FOR_TAKE_TITLE);
            sMessage =  getArguments().getString(KEY_FOR_TAKE_MESSAGE);
        }

        getDialog().setTitle(sTitle);

        View oView = inflater.inflate(R.layout.confirm_dialog_numbers, null, false);

        tvMessageDialog = (TextView)oView.findViewById(R.id.tvMessageDialog);

        btnYes = (Button) oView.findViewById(R.id.btnYes);
        btnNo = (Button) oView.findViewById(R.id.btnNo);

        tvMessageDialog.setText(sMessage);

        setClickListeners();

        return oView;
    }

    private void setClickListeners(){

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IDialogConfirmWorkWithNumb != null) {
                    IDialogConfirmWorkWithNumb.clickYes();
                }
                getDialog().dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IDialogConfirmWorkWithNumb != null) {
                    IDialogConfirmWorkWithNumb.clickNo();
                }
                getDialog().dismiss();
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
        //getDialog().dismiss();
       // getDialog().cancel();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public void setInterfaceForReturn(IDialogConfirmWorkWithNumb IDialogConfirmWorkWithNumb) {
        this.IDialogConfirmWorkWithNumb = IDialogConfirmWorkWithNumb;
    }
}
