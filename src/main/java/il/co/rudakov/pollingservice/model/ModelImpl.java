package il.co.rudakov.pollingservice.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Scope("singleton")
public class ModelImpl implements ModelInterface {

    private Map<String, Integer> votes;
    private Map<String, String> agreements = new HashMap<>();


    public ModelImpl() {
       votes = new ConcurrentHashMap<>();
    }

    @Override
    public boolean addVote(String partyName) {
        if(!votes.containsKey(partyName))
            return false;
        votes.put(partyName,votes.get(partyName)+1);
        return true;
    }

    @Override
    public Map<String, Integer> getVotes() {
        return votes;
    }

    @Override
    public boolean setVotes(Map<String, Integer> votes) {
        if(votes instanceof ConcurrentHashMap) {
            this.votes = votes;
            return true;
        }
        if(votes instanceof Map) {
            this.votes = new ConcurrentHashMap<>(votes);
            return true;
        }
        return false;

    }

    @Override
    public boolean addAgreement(String partyName1, String partyName2) {
        if(agreements.containsKey(partyName1) || agreements.containsKey(partyName2))
            return false;
        agreements.put(partyName1, partyName2);
        agreements.put(partyName2, partyName1);
        return true;
    }

    @Override
    public Map<String, String> getAgreements() {
        return agreements;
    }

}
