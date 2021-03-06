package com.freedroid.mozaik.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.freedroid.mozaik.R;
import com.freedroid.mozaik.dialogs_.DialogInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class IO_BitmapImage {

    public static Bitmap readImage(Context context, String name, int maxSize) {
        File source = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            source = new File(Objects.requireNonNull(context).getExternalCacheDir(), name);
        }

        assert source != null;
        return getResizedBitmap(BitmapFactory.decodeFile(source.getPath()), maxSize);//affiche une image compressée pour ne pas trop charger l'application. Cette image est extraite avec BitmapFactory.decodeFile(
    }

    public static Bitmap readImage(File source, int maxSize) {
        return getResizedBitmap(BitmapFactory.decodeFile(source.getPath()), maxSize);//affiche une image compressée pour ne pas trop charger l'application. Cette image est extraite avec BitmapFactory.decodeFile(
    }

    private static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static File saveImage (Context context, Bitmap bitmapSource, String name, boolean a4){
        File dest = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            if(!a4)dest = new File(Objects.requireNonNull(context).getExternalCacheDir(), name);
            else dest = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), name);

            try(FileOutputStream fos = new FileOutputStream(dest)){
                bitmapSource.compress(Bitmap.CompressFormat.JPEG, 100, fos);         //CompressFormat.JPEG autorise à reduire la qualité contrairement à .PNG
                fos.flush();
            } catch (IOException e) { e.printStackTrace(); }

        }

        return dest;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888); //ARGB_8888 Alpha Red Green Blue et chaque pixel enregistré dans 4 octets
        Canvas canvas = new Canvas(returnedBitmap);     //on attribue le Canvas au Bitmap créé
        Drawable bgDrawable = view.getBackground();     //on utilise la couleur du BG de la vue pour le BG de notre drawable
        if (bgDrawable!=null) bgDrawable.draw(canvas);  //on applique ce BG si il existe
        else canvas.drawColor(Color.WHITE);             //sinon on peint en blanc

        view.draw(canvas);                              //enfin on applique la vue au Canvas

        return returnedBitmap;
    }

    public static void deleteCacheFiles(final Context context){
        final int[] nbrFiles = {0};
        final int[] delFiles = {0};

        Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() { //suppression du cache
                try {
                    File[] files = Objects.requireNonNull(context.getExternalCacheDir()).listFiles();
                    assert files != null;
                    nbrFiles[0] = files.length;

                    for (File file : files) {

                        if (file.delete()) delFiles[0]++;
                    }
                } catch (Exception e) {e.printStackTrace();}
            }
        });

        int dif = nbrFiles[0] - delFiles[0]; //vérification de suppression
        if(dif != 0){                        //si tout n'est pas supprimé on previent l'utilisateur
            Intent intent = new Intent(context, DialogInfo.class);
            String str = context.getString(R.string.warning) + context.getString(R.string.oneSpace)
                    + dif + context.getString(R.string.oneSpace)
                    + context.getString(R.string.temporary_files_have_not_been_deleted)
                    + context.getString(R.string.next_line)
                    + context.getString(R.string.you_can_do_it_manually_in_the_application_cache_folder)
                    + context.getString(R.string.oneSpace) + context.getString(R.string.android_data_com_freedroid_mozaik_cache);            intent.putExtra("text", str);
            intent.putExtra("needResult", false);
            context.startActivity(intent);
        }
    }
}