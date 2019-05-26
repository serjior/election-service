package il.co.rudakov.pollingservice.controller;

import il.co.rudakov.pollingservice.service.ElectionsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class VoteThread extends Thread{

    @Autowired
    ElectionsServiceInterface service;

    private String partyName;

    @Autowired
    public VoteThread() {  }

    @Override
    public void run(){
/*        if(this.partyName == null || this.partyName.equals(""))
            throw  new Exception400("Party name cannot be empty or null!");
        if(service.getParties() == null || service.getParties().isEmpty())
            throw new Exception409("Elections has not been launched yet!");
        if(!service.getParties().contains(this.partyName))
            throw new Exception400("Party not exists. Check the list of parties first!");*/

        service.vote(this.partyName);
    }

    public VoteThread setPartyName(String partyName) {

        this.partyName = partyName;
        return this;
    }
}
