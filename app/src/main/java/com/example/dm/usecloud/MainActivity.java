/*now editing*/
package com.example.dm.usecloud;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private DropboxAPI<AndroidAuthSession> mDBApi;
    public static final String DROPBOX_APP_KEY = "hkylq6f7kmbxtno";
    public static final String DROPBOX_APP_SCECRET = "ejwik68xc6xizee";
    public static final String DROPBOX_APP_FOLDER_NAME = "UseCloud";
    public static final Session.AccessType DROPBOX_ACCESS_TYPE = Session.AccessType.APP_FOLDER;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidAuthSession session = buildDropboxSession();
        mDBApi = new DropboxAPI(session);
        mDBApi.getSession().startOAuth2Authentication(this);

        Button btnUpload = (Button) findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date now = new Date();
                    InputStream in = new ByteArrayInputStream(now.toString().getBytes());

                    String filename = String.format("/%tY%tm%td%tH%tM%tS.txt", now, now, now, now, now, now);
                    DropboxAPI.Entry entry = mDBApi.putFile(filename, in, now.toString().getBytes().length, null, null);

                } catch (Exception e) {

                }

            }
        });

        Button btnDown = (Button)findViewById(R.id.btn_down);
        btnDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    String target = "20120525171459.txt"; // 対象ファイル名

                    // 端末の SDカード上のデータ格納先
                    File sdcarddir = Environment.getExternalStorageDirectory();
                    String apppath = sdcarddir.getAbsolutePath()
                            + File.separatorChar
                            + "data"
                            + File.separatorChar
                            + this.getClass().getPackage().getName().replaceAll("[.]", Character.toString(File.separatorChar))
                            + File.separatorChar
                            ;
                    File appDir = new File(apppath);
                    if (!appDir.exists()) {
                        if(!appDir.mkdirs()) {
                            throw new IllegalStateException("おそらく、android.permission.WRITE_EXTERNAL_STORAGE が マニフェストに設定されてないのでは?");
                        }
                    }

                    File file = new File(apppath + File.separatorChar + target);
                    OutputStream os = new FileOutputStream(file);
                    DropboxAPI.DropboxFileInfo dfi = mDBApi.getFile(File.separatorChar + target, null, os, null);


                } catch(Exception e) {

                     }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mDBApi.getSession();
        if (session.authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();
                AccessTokenPair tokens = mDBApi.getSession().getAccessTokenPair();
                storeDropboxKeys(tokens.key, tokens.secret);
            } catch (IllegalStateException e) {

            }
        }
    }


    /**
     * Dropbox アクセスキーを保持しておく
     *
     * @param key
     * @param secret
     */
    private void storeDropboxKeys(String key, String secret) {
        // TODO SharedPreferences に保持しておく実装
    }

    /**
     * Dropbox アクセスキーを取得する
     *
     * @return
     */
    private String[] getStoredDropboxKeys() {
        // TODO SharedPreferences から アクセスキーを取得する実装
        return null;
    }

    /**
     * Dropbox セッションを作成する
     *
     * @return
     */
    private AndroidAuthSession buildDropboxSession() {
        AppKeyPair appKeyPair
                = new AppKeyPair(MainActivity.DROPBOX_APP_KEY, MainActivity.DROPBOX_APP_SCECRET);
        AndroidAuthSession session = null;

        String[] keys = getStoredDropboxKeys();
        if (keys == null) {
            session = new AndroidAuthSession(appKeyPair, MainActivity.DROPBOX_ACCESS_TYPE);
        } else {
            AccessTokenPair accessToken = new AccessTokenPair(keys[0], keys[1]);
            session = new AndroidAuthSession(appKeyPair, MainActivity.DROPBOX_ACCESS_TYPE, accessToken);
        }
        return session;
    }
}