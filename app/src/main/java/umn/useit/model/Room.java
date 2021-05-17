package umn.useit.model;

public class Room {

    boolean status;
    String solver;
    String poster;
    String problemTitle;
    String problemId;
    long problemTime;
    public Room() {
    }

    public Room(String solver, String poster, String problemTitle, boolean status, long problemTime, String problemId) {
        this.solver = solver;
        this.poster = poster;
        this.problemTitle = problemTitle;
        this.status = status;
        this.problemTime = problemTime;
        this.problemId = problemId;
    }
    public String getProblemId(){return problemId;}

    public void setProblemId(String problemId){
        this.problemId = problemId;
    }

    public String getSolver() {
        return solver;
    }

    public String getPoster() {
        return poster;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public long getProblemTime() {
        return problemTime;
    }
}
