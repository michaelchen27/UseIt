package umn.useit.model;

public class Problem {

    private String TitleProblem, DescProblem, poster;
    private String id;
    private long time;
    private int seen;
    private boolean available;
    private String imgUrl;

    Problem() {
    }

    public Problem(String titleProblem, String descProblem, String poster, long time, int seen, boolean available, String id) {
        this.id = id;
        TitleProblem = titleProblem;
        DescProblem = descProblem;
        this.poster = poster;
        this.time = time;
        this.seen = seen;
        this.available = available;
        this.imgUrl = "";
    }
    public String getId () {return id;}

    public void setId (String id) { this.id = id;}

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

    public String getImgUrl() { return imgUrl; }

    public void setImgUrl (String imgUrl) {
        this.imgUrl = imgUrl;
    }
}


