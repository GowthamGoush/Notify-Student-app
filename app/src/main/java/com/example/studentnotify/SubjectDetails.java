package com.example.studentnotify;

public class SubjectDetails {

    private String Subject;
    private String Content;
    private String Description;
    private Boolean Expanded;
    private int Attended,Bunked;

    public SubjectDetails(String subject, String content, String description, int attended, int bunked) {
        Subject = subject;
        Content = content;
        Description = description;
        Attended = attended;
        Bunked = bunked;
        Expanded = false;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getAttended() {
        return Attended;
    }

    public void setAttended(int attended) {
        Attended = attended;
    }

    public int getBunked() {
        return Bunked;
    }

    public void setBunked(int bunked) {
        Bunked = bunked;
    }

    public Boolean getExpanded() {
        return Expanded;
    }

    public void setExpanded(Boolean expanded) {
        Expanded = expanded;
    }

}
