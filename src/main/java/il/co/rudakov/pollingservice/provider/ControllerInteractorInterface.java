package il.co.rudakov.pollingservice.provider;

import java.util.Map;

public interface ControllerInteractorInterface {


    Map<String, Integer> getVotes();
    Map<String, String> getAgreements();
}
