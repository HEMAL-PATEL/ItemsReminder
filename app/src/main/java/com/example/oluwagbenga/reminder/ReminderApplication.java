package com.example.oluwagbenga.reminder;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.oluwagbenga.talentbaseitemreminder.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReminderApplication extends Application {

    public static final String TAG = ReminderApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        File file = getPrivateAlbumStorageDir(this, "TalentbaseReminder");
        try{
            saveMP3();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void saveMP3() throws IOException{
        InputStream in = getResources().openRawResource(R.raw.notif);
        File file = new File(getPrivateAlbumStorageDir(this, "TalentbaseReminder"),
                "notify_tone.mp3");
        FileOutputStream out = new FileOutputStream(file);

        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }

    public File getPrivateAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }
}
