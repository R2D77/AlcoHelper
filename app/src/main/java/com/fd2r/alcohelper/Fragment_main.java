package com.fd2r.alcohelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by roman on 23.12.15.
 */
public class Fragment_main extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main, container, false);

        final Button bEP = (Button) rootView.findViewById(R.id.bEntryPoint);
        final Button bBMB = (Button) rootView.findViewById(R.id.bBringMeBack);

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bEntryPoint:
                        bBMB.setEnabled(true);
                        bBMB.setVisibility(View.VISIBLE);
                        bEP.setEnabled(false);
                        bEP.setVisibility(View.GONE);
                        ((MapsActivity)getActivity()).fromFragmentEP();
                        break;

                    case R.id.bBringMeBack:
                        ((MapsActivity)getActivity()).fromFragmentBMB();
                        break;
                }
            }
        };

        bEP.setOnClickListener(ocl);
        bBMB.setOnClickListener(ocl);

        return rootView;
    }

}
