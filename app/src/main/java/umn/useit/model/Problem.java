package umn.useit.model;

import java.util.Date;

public class Problem {

    private String TitleProblem, DescProblem, poster;
    private String date;
    private int id, seen;

    Problem(){}

    public Problem(String titleProblem, String descProblem, String poster, String date, int seen, int id) {
        TitleProblem = titleProblem;
        DescProblem = descProblem;
        this.poster = poster;
        this.date = date;
        this.seen = seen;
        this.id = id;
    }

    public String getTitleProblem() {
        return TitleProblem;
    }

    public void setTitleProblem(String titleProblem) {
        TitleProblem = titleProblem;
    }

    public String getDescProblem() {
        return DescProblem;
    }

    public void setDescProblem(String descProblem) {
        DescProblem = descProblem;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) { this.poster = poster; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


