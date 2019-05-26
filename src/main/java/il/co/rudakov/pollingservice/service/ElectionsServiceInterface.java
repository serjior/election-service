package il.co.rudakov.pollingservice.service;

import java.util.Map;
import java.util.Set;

public interface ElectionsServiceInterface {

    boolean vote(String partyName);
    Map<String, Integer> getThresholdSeedRoundResults();
    Map<String, Integer> getFirstRoundResults();
    Map<String, Integer> getSecondRoundResults();
    Map<String, Integer> getResults(Map<String, Integer> snapshot, Map<String, String> agreements, double threshold);
    Map<String, String> getAgreements();
    Set<String> getParties();


}
