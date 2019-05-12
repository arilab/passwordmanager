package com.laynet.passwordmanager.persist;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laynet.passwordmanager.model.Entry;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alain on 28/03/2018.
 */

public class EntryPersistence {

    public List<Entry> read(Context context) throws IOException {
        Reader fileReader = new FileSystem().openInternalFile(context);
        Gson gson = new Gson();
        Entry[] entriesArray = gson.fromJson(fileReader, Entry[].class);
        if (entriesArray == null) return new ArrayList<>();
        return Arrays.asList(entriesArray);
    }

    public void write(Context context, List<Entry> entries) throws IOException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Entry>>(){}.getType();
        String json = gson.toJson(entries, listType);
        new FileSystem().writeFile(context, json);
    }

}
