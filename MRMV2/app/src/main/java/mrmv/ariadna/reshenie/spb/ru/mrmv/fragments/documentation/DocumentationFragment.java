package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.documentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;

/**
 * Created by kirichenko on 02.09.2015.
 */
public class DocumentationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.find_emk_fragment, container, false);

        return oView;
    }
}
