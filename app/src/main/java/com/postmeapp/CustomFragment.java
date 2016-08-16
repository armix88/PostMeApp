package com.postmeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;

public class CustomFragment extends Fragment {

    private static final String ARG_PARAM1 = "Title";
    private static final String ARG_PARAM2 = "Position";
    private static final String ARG_PARAM3 = "Layout_Id";

    private String  strTitle;
    private Integer iPosition;
    private Integer iLayoutId;

    public CustomFragment() {
    }

    public static CustomFragment newInstance(String strTitle, Integer iPosition, Integer iLayoutId) {
        CustomFragment fragment = new CustomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, strTitle);
        args.putInt(ARG_PARAM2, iPosition);
        args.putInt(ARG_PARAM3, iLayoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle(getArguments().getString(ARG_PARAM1));
        setPosition(getArguments().getInt(ARG_PARAM2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View vReturn = inflater.inflate(getArguments().getInt(ARG_PARAM3), container, false);
        return vReturn;
    }

    public String getTitle() {
        return strTitle;
    }

    public void setTitle(String mTitle) {
        this.strTitle = mTitle;
    }

    public Integer getPosition() {
        return iPosition;
    }

    public void setPosition(Integer iPosition) {
        this.iPosition = iPosition;
    }

    public Integer getiLayoutId() {
        return iLayoutId;
    }

    public void setiLayoutId(Integer iLayoutId) {
        this.iLayoutId = iLayoutId;
    }

}
