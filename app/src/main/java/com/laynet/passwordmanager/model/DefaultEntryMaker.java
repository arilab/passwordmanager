package com.laynet.passwordmanager.model;

import java.util.ArrayList;
import java.util.List;

public class DefaultEntryMaker {

    public List<Entry> make() {
        List<Entry> entries = new ArrayList<>();
        Entry entry = new Entry(1, "Password Manager", "Me", "password");
        entries.add(entry);
        return entries;
    }
}
