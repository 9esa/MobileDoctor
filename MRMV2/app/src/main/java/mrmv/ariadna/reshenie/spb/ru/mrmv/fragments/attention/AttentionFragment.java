package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.attention;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;

/**
 * Created by kirichenko on 12.09.2015.
 */
public class AttentionFragment extends Fragment {

    private Button btnSendMessageAttention;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.attention_fragment, null, false);

        getAllViewElements(oView);


        return oView;
    }

    private void getAllViewElements(View oView) {

        btnSendMessageAttention = (Button) oView.findViewById(R.id.btnSendMessageAttention);

        btnSendMessageAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), getActivity().getString(R.string.message_about_attention_send), Toast.LENGTH_LONG).show();

            }
        });

    }
}