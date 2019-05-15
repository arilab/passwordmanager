package com.laynet.passwordmanager.security;

public class MasterPassword {
    private static MasterPassword masterPassword = new MasterPassword();
    private String pwd = "";

    private MasterPassword() {}

    public static MasterPassword getInstance() {
        return masterPassword;
    }

    public void setPassword(String password) {
        this.pwd = password;
    }

    public String getPassword() {
        return pwd;
    }
}
