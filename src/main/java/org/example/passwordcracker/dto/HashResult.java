package org.example.passwordcracker.dto;

public class HashResult {
    private String md5;
    private String sha256;

    public HashResult(String md5, String sha256) {
        this.md5 = md5;
        this.sha256 = sha256;
    }

    public String getMd5() {
        return md5;
    }

    public String getSha256() {
        return sha256;
    }
}