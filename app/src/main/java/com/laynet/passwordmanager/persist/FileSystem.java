package com.laynet.passwordmanager.persist;

import android.content.Context;

import com.laynet.passwordmanager.exceptions.CryptoException;
import com.laynet.passwordmanager.security.Crypto;
import com.laynet.passwordmanager.security.MasterPassword;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by alain on 28/03/2018.
 */

public class FileSystem {
    private static final String FILESTORE = "input.json";

    public boolean filestoreExists(Context context) {
        File file = new File(context.getFilesDir(), FILESTORE);
        return file.exists();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void deleteFilestore(Context context) {
        File file = new File(context.getFilesDir(), FILESTORE);
        if (file.exists())
            file.delete();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    Reader openInternalFile(Context context) throws IOException, CryptoException {
        File file = new File(context.getFilesDir(), FILESTORE);
        if (!file.exists()) file.createNewFile();
        RandomAccessFile f = new RandomAccessFile(file, "r");
        byte[] cipherText = new byte[(int)f.length()];
        f.readFully(cipherText);
        return new StringReader(new Crypto().decrypt(new String(cipherText), MasterPassword.getInstance().getPassword()));
    }

    void writeFile(Context context, String content) throws IOException {
        File file = new File(context.getFilesDir(), FILESTORE);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(new Crypto().encrypt(content, MasterPassword.getInstance().getPassword()));
        fileWriter.close();
    }

}
