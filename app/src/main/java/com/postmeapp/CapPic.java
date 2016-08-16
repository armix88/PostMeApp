package com.postmeapp;

import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.File;

public class CapPic extends Fragment implements View.OnClickListener {

    public static final int CAMERA_REQUEST = 123;

    ImageView iwFoto;

    File mOutFile;

    MainActivity mActivity;

    public CapPic() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.picture_capt, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iwFoto =(ImageView) mActivity.findViewById(R.id.iPicture);
        iwFoto.setOnClickListener(this);
        if(mActivity.getFoto() == null)
            iwFoto.setImageResource(R.drawable.placeholder_10x15);
        else
            iwFoto.setImageBitmap(Utils.resizeBitmap(mActivity.getFoto(),
                    (int)getResources().getDimension(R.dimen.postcard_width_2),
                    (int)getResources().getDimension(R.dimen.postcard_height_2))
            );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(resultCode, resultCode, data);

        try{
            Thread.sleep(2000);
        }
        catch (Exception e){
            Log.i(Utils.getTag(),"Sleep exception after CameraActivity return");
        }

        if(requestCode == CAMERA_REQUEST)
        {
            try{
                if(mOutFile.exists()) {
                    Bitmap datiPicture = android.provider.MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(),
                            Uri.fromFile(mOutFile));

                    iwFoto.setImageBitmap(Utils.resizeBitmap(datiPicture,
                            (int)getResources().getDimension(R.dimen.postcard_width_2),
                            (int)getResources().getDimension(R.dimen.postcard_height_2)));
                    mActivity.setFoto(Utils.resizeBitmap(datiPicture,
                                                         (int)getResources().getDimension(R.dimen.postcard_width),
                                                         (int)getResources().getDimension(R.dimen.postcard_height)
                                        )
                    );
                    mOutFile.delete();
                }
                else {
                    Log.e(Utils.getTag(), "File " + mOutFile.getAbsolutePath() + mOutFile.getName() + " not created.");
                    Toast.makeText(getContext(),"File " + mOutFile.getAbsolutePath() + mOutFile.getName() + " not created.",Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Log.e(Utils.getTag(),"CapPic.onActivityResult():" +e);
        }
        }
    }

    @Override
    public void onClick(View view) {

        try {
            mOutFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

            if(!mOutFile.exists())
            {
                mOutFile.createNewFile();
            }

            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutFile));
            startActivityForResult(intentCamera, CAMERA_REQUEST);

        }catch (Exception e){
            Log.e(Utils.getTag(), "CapPic.OnClick():"+e.getMessage());
        }
    }
}
