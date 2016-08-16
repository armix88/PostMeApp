package com.postmeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ImageView;

public class FillCard extends Fragment implements View.OnClickListener{

//    Button bSaveData;
    ImageView iStamp;

    MainActivity mActivity;

     public FillCard() {
         // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.data_collect,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iStamp = (ImageView) getView().findViewById(R.id.iStamp);
        iStamp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) { Utils.saveDatasToBitmap(mActivity); }

}
