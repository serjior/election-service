package il.co.rudakov.pollingservice.controller.dto;


public class EmulatePollingDto {

    public String name;
    public Integer votes;

    public EmulatePollingDto() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
