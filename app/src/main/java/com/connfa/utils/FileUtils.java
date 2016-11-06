package com.connfa.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created on 23.12.2014.
 */
public class FileUtils {
    public static boolean writeBitmapToStorage(String name, Bitmap data, Context context) {
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
            return data.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToFileToStorage(String name, String data, Context theContext) {
        FileWriter fileWritter = null;
        try {
            File file = getFileWithName(name, theContext);

            File parentDirectory = file.getParentFile();
            if (!parentDirectory.exists()) {
                parentDirectory.mkdirs();
            }

            file.createNewFile();
            fileWritter = new FileWriter(file);
            fileWritter.write(data);
            fileWritter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWritter != null) {
                try {
                    fileWritter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteStoredFile(String name, Context theContext) {
        try {
            File file = getFileWithName(name, theContext);
            deleteFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDataStorageDirectory(Context theContext) {
        File dataStorageDirectory = getDataFilesDirectory(theContext);
        deleteFile(dataStorageDirectory);
    }

    public static String readFromStoredFile(String name, Context theContext) {
        FileInputStream fin = null;
        try {
            File fl = getFileWithName(name, theContext);
            fin = new FileInputStream(fl);
            String ret = convertStreamToString(fin);
            //Make sure you close all streams.
            fin.close();
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Bitmap readBitmapFromStoredFile(String name, Context theContext) {
        FileInputStream fin = null;
        try {
            File fl = getFileWithName(name, theContext);
            fin = new FileInputStream(fl);
            return BitmapFactory.decodeStream(fin);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Bitmap readBitmapFromStoredFile(String name, int reqWidth, int reqHeight, Context theContext) {
        L.e("Reading bitmap:" + name);
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
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

    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    private static File getFileWithName(String theName, Context theContext) throws IOException {
//
//        String state = Environment.getExternalStorageState();
//
//        File storageDir;
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            storageDir = theContext.getExternalCacheDir();
//        } else {
//            storageDir = theContext.getCacheDir();
//        }
//        return new File(storageDir,cleanFileName(theName));

        //We have to store fiels in permanent directory
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
        L.e("Sample size:" + inSampleSize);
        return inSampleSize;
    }
}
