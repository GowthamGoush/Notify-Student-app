package com.example.studentnotify;

public class pdfDetails {

    String pdfName,pdfUrl;

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

}
