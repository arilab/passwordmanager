package com.laynet.passwordmanager.persist;

import com.google.gson.Gson;
import com.laynet.passwordmanager.model.Entry;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alain on 28/03/2018.
 */

public class EntryReader {

    public List<Entry> read(Reader reader) {
        Gson gson = new Gson();
        Entry[] entriesArray = gson.fromJson(reader, Entry[].class);
        if (entriesArray == null) {
            Entry intialEntry = new Entry();
            intialEntry.id = 1;
            intialEntry.name = "Password Manager";
            intialEntry.login = "Alain";
            intialEntry.password = "toto12";
            List<Entry> entries = new ArrayList<>();
            entries.add(intialEntry);
            return entries;
        }
        return Arrays.asList(entriesArray);
    }

}
