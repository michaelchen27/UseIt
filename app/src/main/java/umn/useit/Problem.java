package umn.useit;

public class Problem {

    private String TitleProblem, DescProblem, poster, date, seen;

    Problem(){}

    public Problem(String titleProblem, String descProblem, String poster, String date, String seen) {
        TitleProblem = titleProblem;
        DescProblem = descProblem;
        this.poster = poster;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }
}
