package umn.useit.model;

public class Problem {

    private String TitleProblem, DescProblem, poster;
    private long time;
    private int seen;

    Problem() {
    }

    public Problem(String titleProblem, String descProblem, String poster, long time, int seen) {
        TitleProblem = titleProblem;
        DescProblem = descProblem;
        this.poster = poster;
        this.time = time;
        this.seen = seen;
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

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

}


