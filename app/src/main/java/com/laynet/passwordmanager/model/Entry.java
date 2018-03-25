package com.laynet.passwordmanager.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alain on 20/03/2018.
 */

public class Entry implements Parcelable {
    public int id = 0;
    public String name = "";
    public String login = "";
    public String password = "";

    public Entry(){}

    public Entry(int id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    protected Entry(Parcel in) {
        id = in.readInt();
        name = in.readString();
        login = in.readString();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(login);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };
}
