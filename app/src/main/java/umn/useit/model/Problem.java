package umn.useit.model;

public class Problem {

    private String TitleProblem, DescProblem, poster;
    private long time;
    private int seen;
    private boolean available;

    Problem() {
    }

    public Problem(String titleProblem, String descProblem, String poster, long time, int seen, boolean available) {
        TitleProblem = titleProblem;
        DescProblem = descProblem;
        this.poster = poster;
        this.time = time;
        this.seen = seen;
        this.available = available;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}


