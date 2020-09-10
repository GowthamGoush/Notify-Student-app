package com.example.studentnotify;

import android.net.Uri;

public class pdfDetails {

    private String pdfName;
    private String pdfUrl;
    private Uri filePath;
    private String fileKey;

    public pdfDetails() {

    }

    public pdfDetails(String pdfName, String pdfUrl) {
        this.pdfName = pdfName;
        this.pdfUrl = pdfUrl;
    }

    public String getPdfName() {
        return pdfName;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public Uri getFilePath() {
        return filePath;
    }

    public void setFilePath(Uri filePath) {
        this.filePath = filePath;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
