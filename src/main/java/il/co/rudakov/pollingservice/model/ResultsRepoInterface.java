package il.co.rudakov.pollingservice.model;

import java.util.Map;

public interface ResultsRepoInterface {

    Map<String, Integer> getSnapshot();

    public void setSnapshot(Map<String, Integer> snapshot);

    public Map<String, Integer> getThresholdSeedResults();

    public void setThresholdSeedResults(Map<String, Integer> thresholdSeedResults);

    public Map<String, Integer> getFirstRoundResults();

    public void setFirstRoundResults(Map<String, Integer> firstRoundResults);

    public Map<String, Integer> getSecondRoundResults();

    public void setSecondRoundResults(Map<String, Integer> secondRoundResults);

    public Map<String, Integer> getSurplusMandates();

    public void setSurplusMandates(Map<String, Integer> surplusMandates);

    public Map<String, Double> getReminders();

    public void setReminders(Map<String, Double> reminders);

    public Map<String, Double> getInitUnionReminders();

    public void setInitUnionReminders(Map<String, Double> initUnionReminders);

    public Map<String, String> getAgreements();

    public void setAgreements(Map<String, String> agreements);

    public Map<String, Integer> getFinalResults();

    public void setFinalResults(Map<String, Integer> finalResults);

    public double getInitRate();

    public void setInitRate(double initRate);

    public double getCurrentRate();

    public void setCurrentRate(double currentRate);

    public int getTotalVotes();

    public void setTotalVotes(int totalVotes);
}
