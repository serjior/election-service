package il.co.rudakov.pollingservice.model;

import java.util.Map;

public interface ModelInterface {

    boolean addVote(String partyName);
    Map<String, Integer> getVotes();
    boolean setVotes(Map<String, Integer> votes);

    boolean addAgreement(String partyName1, String partyName2);
    Map<String, String> getAgreements();
}
