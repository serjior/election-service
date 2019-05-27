package il.co.rudakov.pollingservice.service;

import il.co.rudakov.pollingservice.error_hadler.Exception409;
import il.co.rudakov.pollingservice.model.ModelInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ManagementServiceImpl implements ManagementServiceInterface{

    @Autowired
    ModelInterface model;

    @Override
    public void addParty(String partyName) {
        if(model.getVotes().containsKey(partyName))
            throw new Exception409("The party already exists!");
        model.getVotes().put(partyName, 0);
    }

    @Override
    public void addAgreement(String partyName1, String partyName2) {
        if(model.getAgreements().containsKey(partyName1) || model.getAgreements().containsKey(partyName2))
            throw new Exception409("One or both parties have been already set agreement previously. Check agreements first!");
        model.addAgreement(partyName1, partyName2);
    }

    @Override
    public void emulatePolling(Map<String, Integer> emulation) {
        model.setVotes(emulation);
    }

    @Override
    public void dropAgreements() {
        model.setAgreements(new HashMap<String, String>());
    }


}
