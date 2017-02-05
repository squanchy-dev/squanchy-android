package net.squanchy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import timber.log.Timber;

public class FileUtils {

    public static boolean writeBytesToStorage(String name, byte[] data, Context context) {
        if (data == null) {
            return false;
        }
        FileOutputStream out = null;
        try {
            File file = getFileWithName(name, context);

            File parentDirectory = file.getParentFile();
            if (!parentDirectory.exists()) {
                parentDirectory.mkdirs();
            }

            out = new FileOutputStream(file);
            out.write(data);
            out.flush();
            return true;
        } catch (Exception e) {
            Timber.log(Log.INFO, e, "Error while writing to file");
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Timber.log(Log.INFO, e, "Error while closing bitmap file");
            }
        }
    }

    public static void deleteStoredFile(String name, Context theContext) {
        try {
            File file = getFileWithName(name, theContext);
            deleteFile(file);
        } catch (IOException e) {
            Timber.log(Log.INFO, e, "Error while deleting file");
        }
    }

    public static void deleteDataStorageDirectory(Context theContext) {
        File dataStorageDirectory = getDataFilesDirectory(theContext);
        deleteFile(dataStorageDirectory);
    }

    public static Bitmap readBitmapFromStoredFile(String name, int reqWidth, int reqHeight, Context theContext) {
        FileInputStream fin = null;
        try {
            File fl = getFileWithName(name, theContext);

            BitmapFactory.Options options = new BitmapFactory.Options();
            fin = new FileInputStream(fl);
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fin, null, options);
            fin.close();

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            fin = new FileInputStream(fl);

            return BitmapFactory.decodeStream(fin, null, options);
        } catch (IOException | Error e) {
            Timber.log(Log.INFO, e, "Error while reading bitmap from file");
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    Timber.log(Log.INFO, e, "Error while closing bitmap file");
                }
            }
        }
        return null;
    }

    private static void deleteFile(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteFile(child);
            }
        }
        fileOrDirectory.delete();
    }

    private static File getFileWithName(String theName, Context theContext) throws IOException {
        // We have to store fiels in permanent directory
        File storageDir = getDataFilesDirectory(theContext);
        return new File(storageDir, cleanFileName(theName));
    }

    private static File getDataFilesDirectory(Context theContext) {
        File storageDir = theContext.getFilesDir();
        return new File(storageDir, "databaseFiles");
    }

    private static String cleanFileName(String fileName) {
        return fileName.replaceAll("[|?*<\":>+\\[\\]/']", "-");
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
