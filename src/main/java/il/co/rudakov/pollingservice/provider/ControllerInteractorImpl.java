package il.co.rudakov.pollingservice.provider;

import il.co.rudakov.pollingservice.error_hadler.Exception409;
import il.co.rudakov.pollingservice.model.ModelInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ControllerInteractorImpl implements ControllerInteractorInterface{

    @Autowired
    ModelInterface model;

    @Override
    public Map<String, Integer> getVotes() {
        HashMap<String, Integer> res = new HashMap<>();
        if(model.getVotes().isEmpty() || model.getVotes() == null)
            throw new Exception409("Elections has not been launched yet!");
        res.putAll(model.getVotes());
        return res;
    }

    @Override
    public Map<String, String> getAgreements() {
        HashMap<String, String> res = new HashMap<>();
        res.putAll(model.getAgreements());
        return res;
    }
}
