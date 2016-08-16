package com.postmeapp;

import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.graphics.*;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.*;
import android.widget.*;

import java.io.*;
import java.security.*;

public class Utils {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    public static final String[] CREDENTIALS = new String[]{
            "bembe83@libero.it:5a105e8b9d40e1329780d62ea2265d8a",
            "armix88@gmail.com:5a105e8b9d40e1329780d62ea2265d8a",
            "rino.falzone@gmail.com:5a105e8b9d40e1329780d62ea2265d8a"
    };

    public static String getTag(){ return "CartUlina";}

    public static String getDbFolder(Activity activity) { return Environment.getExternalStorageDirectory()
                                                +"/"+activity.getString(R.string.dbfolder); }

    public static String getDbName(Activity activity) { return activity.getString(R.string.dbname); }

    public static Bitmap resizeBitmap(Bitmap bMap, int newWidth, int newHeight)
    {
        int width  = bMap.getWidth();
        int height = bMap.getHeight();

        float fScaleW = ((float) newWidth)/width;
        float fScaleH = ((float) newHeight)/height;

        Matrix mScale = new Matrix();
        mScale.postScale(fScaleW,fScaleH);

        return Bitmap.createBitmap(bMap, 0, 0, width, height, mScale, false);
    }

    public static Boolean isEmail(String strEmail)
    {
        boolean result = false;

        if(strEmail!= null)
            result = android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();

        return result;
    }

    public static void sendEmail(Activity activity, String strFrom, String strTo, String strCC,
                                 String strSubject, String strMessage, File fAttachment)
    {
        Intent emailIntent;
        String strAttchType, strMailType = "text/plain";

        try{
            emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));

            if(fAttachment!=null && fAttachment.exists()) {
                strAttchType = fAttachment.getName().substring(fAttachment.getName().lastIndexOf(".")+1);
                strMailType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(strAttchType);
                if(strMailType == null) {
                    strMailType = "*/*";
                    Log.e(getTag(),"Unable to identify the file type");
                }
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fAttachment));
            }

            emailIntent.setType(strMailType);

            if(strTo!= null && isEmail(strTo))
                emailIntent.putExtra(Intent.EXTRA_EMAIL, strTo);
            else
                throw new Exception("To address not valid");

            if(strCC!= null && isEmail(strCC))
                emailIntent.putExtra(Intent.EXTRA_EMAIL, strCC);

            if(strSubject!= null)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, strSubject);
            else
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");

            if(strMessage!= null) {
                if (strMessage.contains("<HTML>"))
                    emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, strMessage);
                else
                    emailIntent.putExtra(Intent.EXTRA_TEXT, strMessage);
            }else
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

            activity.startActivity(emailIntent);

        }catch (Exception e)
        {
            Log.e(getTag(),"sendEmail() exception: "+e);
        }
    }

    public static void saveBitmap(Bitmap bToSave, File file)
    {
        OutputStream outStream;
        try {

            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            outStream = new FileOutputStream(file);
            bToSave.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            Log.e(getTag(),"SaveBitmap() exception: "+e);
        }
    }

    public static String md5(String strToHash)
    {
        MessageDigest digest;
        String strRet;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(strToHash.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            strRet = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(getTag(),"MD5 fails:" + e);
            strRet = null;
        }
        return strRet;
    }

    public static void saveDatasToBitmap(MainActivity mActivity)
    {
        Bitmap returnedBitmap;
        RelativeLayout ltoSave;

        int iPostCardWidth, iPostCardHeight;

        try {

            iPostCardWidth = (int) mActivity.getResources().getDimension(R.dimen.postcard_width);
            iPostCardHeight = (int) mActivity.getResources().getDimension(R.dimen.postcard_height);

            ltoSave = (RelativeLayout) mActivity.findViewById(R.id.PostcardBack);

            ltoSave.setDrawingCacheEnabled(true);
            returnedBitmap = ltoSave.getDrawingCache();

            mActivity.setDati(Utils.resizeBitmap(returnedBitmap,
                    iPostCardWidth,
                    iPostCardHeight
                    )
            );

        }catch (Exception e)
        {
            Log.e(getTag(),"saveDatasToBitmap() exception:"+e);
        }
    }

    public static String retrieveCredentials(Activity activity, String strUsername)
    {
        String strRet = null;
        String strSQL = "SELECT email||':'||password AS credential FROM user_anag WHERE email = ?";
        SQLiteUtils sql = null;
        Cursor cur;

        Boolean LocalDb = Boolean.valueOf(activity.getString(R.string.LocalDb).toLowerCase());

        try {
            if(LocalDb) {
                sql = new SQLiteUtils(activity,
                                      getDbFolder(activity),
                                      getDbName(activity)
                );

                sql.Connect();
                cur = sql.Load(strSQL, new String[]{strUsername});

                if (cur != null && cur.getCount() > 0) {
                    cur.moveToFirst();
                    strRet = cur.getString(cur.getColumnIndex("credential"));
                } else
                    strRet = null;
            }
            else
            {
                for (String credential : CREDENTIALS) {
                    String[] pieces = credential.split(":");
                    if (pieces[0].equals(strUsername)) {
                        strRet = credential;
                    }
                }
            }
        }catch (Exception e)
        {
            Log.e(getTag(), "retrieveCredentials() exception: "+e);
        }
        finally {
            if(sql!=null)
                sql.Disconnect();
        }
        return strRet;
    }

    public static void checkLocalDBExist(Activity activity, String dbFullName, boolean update)
    {
        File db = new File(dbFullName);
        FileOutputStream outDB;
        InputStream inDB;

        if(!db.exists() || update)
        {
            try {

                if(update){
                    Log.i(getTag(), "Update Existing db");
                    db.delete();
                    db.createNewFile();
                }else {
                    db.createNewFile();
                    Log.i(getTag(), "DB doesn't exists. DB creation.");
                }

                outDB = new FileOutputStream(db);
                inDB = activity.getAssets().open(activity.getString(R.string.dbname));

                int read = inDB.read();

                while(read!=-1){
                    outDB.write(read);
                    read = inDB.read();
                }

                outDB.flush();
                outDB.close();
                inDB.close();

            } catch (Exception e) {
                Log.e(getTag(),"checkDBExist() Exception: "+e);
                db.delete();
            }
        }
    }

    public static void checkDBExist(Activity activity, Boolean update)
    {
        Boolean LocalDb = Boolean.valueOf(activity.getString(R.string.LocalDb).toLowerCase());

        if(LocalDb) {
            String strDbFullName = getDbFolder(activity) + "/" +getDbName(activity);
            strDbFullName = strDbFullName.replace("//", "/");

            checkLocalDBExist(activity, strDbFullName, update);
        }
    }
}
