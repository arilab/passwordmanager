package com.laynet.passwordmanager.persist;

import android.content.Context;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by alain on 28/03/2018.
 */

public class FileSystem {
    private static final String FILESTORE = "input.json";

    public boolean filestoreExists(Context context) {
        File file = new File(context.getFilesDir(), FILESTORE);
        return file.exists();
    }

    public Reader openInternalFile(Context context) throws IOException {
        File file = new File(context.getFilesDir(), FILESTORE);
        if (!file.exists()) file.createNewFile();
        return new FileReader(file);
    }

    public void writeFile(Context context, String content) throws IOException {
        File file = new File(context.getFilesDir(), FILESTORE);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
    }

}
