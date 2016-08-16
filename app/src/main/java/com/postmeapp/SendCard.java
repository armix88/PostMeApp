package com.postmeapp;

import android.graphics.*;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.File;

public class SendCard extends Fragment implements View.OnClickListener {

    MainActivity mActivity;

    Button bSend;
    EditText etEmail;

    public SendCard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_postcard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etEmail = (EditText) getView().findViewById(R.id.email);
        bSend = (Button) getView().findViewById(R.id.bSend);
        bSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bitmap bitmapReturn;
        Canvas canvas;
        int iPostCardWidth, iPostCardHeight;
        String strEmail;
        File fImage = null;

        try{
            if(mActivity.getFoto() != null) {

                if (mActivity.getDati() == null) {
                    Utils.saveDatasToBitmap(mActivity);
                }

                strEmail = etEmail.getText().toString();

                if (strEmail != null && Utils.isEmail(strEmail)) {
                    iPostCardWidth = (int) getResources().getDimension(R.dimen.postcard_width);
                    iPostCardHeight = (int) getResources().getDimension(R.dimen.postcard_height);
                    bitmapReturn = Bitmap.createBitmap(iPostCardWidth,
                            2 * iPostCardHeight,
                            Bitmap.Config.ARGB_8888
                    );

                    canvas = new Canvas(bitmapReturn);
                    canvas.drawBitmap(mActivity.getFoto(), 0, 0, null);
                    canvas.drawBitmap(mActivity.getDati(), 0, iPostCardHeight, null);

                    fImage = new File(Environment.getExternalStorageDirectory(), "temp_card.jpg");

                    Utils.saveBitmap(bitmapReturn, fImage);

                    Utils.sendEmail(mActivity, "",strEmail, null, "Test Cartulina", "Ciao,\nStampa questa cartolina",fImage);

                } else
                    Toast.makeText(getContext(), "Please insert a valid Email address!!", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(getContext(), "No Picture captured!!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.e(Utils.getTag(),"onClick() exceprion: "+e);
        }
    }
}