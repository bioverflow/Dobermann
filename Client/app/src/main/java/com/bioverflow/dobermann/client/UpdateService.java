package com.bioverflow.dobermann.client;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UpdateService extends Service {
    private static final String APK_NAME = "Dobermann.apk";
    private static final String FOLDER_NAME = "Dobermann";
    private static final String APK_URI = "https://bioverflow.github.io/Dobermann/contents/Dobermann.apk";
    private static final String APK_VERSION = "https://bioverflow.github.io/Dobermann/contents/Dobermann.json";

    private String jsonData;
    private String currentAppVersion;
    private String jsonAppVersion;
    private String jsonAppName;
    private VersionComparator versionComparator;

    private SharedPreferences sharedPreferences;

    public UpdateService() {
        /// Get json string from JSON file in website
        jsonData = getJsonData();

        /// Parse downloaded file
        parseJSONString();

        /// Get current app if exist
        currentAppVersion = getCurrentAppVersion();

        /// If version of current app is outdated
        if(validateAppVersion(currentAppVersion, jsonAppVersion) == false) {
            /// Create an installation folder
            createFolder();
            /// Check if folder is created succesfuly
            if(existInstallationFolder()){
                /// Download new apk from website
                downloadApk();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String getCurrentAppVersion(){
        this.sharedPreferences = this.getSharedPreferences("ClientSettings", this.MODE_PRIVATE);
        return this.sharedPreferences.getString("CurrentVersion", "");
    }

    private boolean validateAppVersion(String currentVersion, String fileVersion){
        boolean isUpdated = true;
        if(jsonAppVersion.length() > 0){
            versionComparator= new VersionComparator();
            int result = versionComparator.compare(currentVersion, fileVersion);
            if (result < 0) isUpdated = false;
            if (result > 0) isUpdated = true;
        }
        return  isUpdated;
    }

    ///https://stackoverflow.com/questions/17794974/create-folder-in-android
    private void createFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + FOLDER_NAME);

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private boolean existInstallationFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + FOLDER_NAME);
        boolean success = true;
        if (folder.exists()) {
            return true;
        }
        else {
            return false;
        }
    }

    /// https://stackoverflow.com/questions/33849963/cannot-resolve-symbol-getjsonobject-in-android-studio/33855487
    private void parseJSONString(){
        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(jsonData);
            JSONObject uniObject = mainObject.getJSONObject("app");
            jsonAppVersion = uniObject.getString("version");
            jsonAppName = uniObject.getString("name");
        } catch (Exception e) {
            // Do something else on failure
        }

    }

    ///https://stackoverflow.com/questions/13196234/simple-parse-json-from-url-on-android-and-display-in-listview#answer-43012984
    protected String getJsonData() {
        try {
            URL Url = new URL(APK_VERSION);
            HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            line = sb.toString();
            connection.disconnect();
            is.close();
            sb.delete(0, sb.length());
            return line;
        } catch (Exception e) {
            return null;
        }
    }

    ///https://stackoverflow.com/questions/15213211/update-an-android-app-without-google-play
    private String downloadApk() {
        String path = APK_NAME;
        try {
            URL url = new URL(APK_URI);
            URLConnection connection = url.openConnection();
            connection.connect();

            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(path);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("YourApp", "Well that didn't work out so well...");
            Log.e("YourApp", e.getMessage());
        }
        return path;
    }

    private void publishProgress(int progress){
        if(progress>=100){
            upgradeApp();
        }
    }

    private void upgradeApp() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(new File(APK_NAME)),
                "application/vnd.android.package-archive" );
        Log.d("Lofting", "About to install new .apk");
        this.startActivity(i);
    }
}
