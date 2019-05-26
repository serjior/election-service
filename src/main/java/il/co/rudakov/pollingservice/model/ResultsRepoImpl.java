package il.co.rudakov.pollingservice.model;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ResultsRepoImpl implements ResultsRepoInterface{

    // current elections state snapshot
    Map<String, Integer> snapshot;
    // parties passed threshold seed
    public Map<String, Integer> thresholdSeedResults;
    // results of the first round distribution among participants (unions and parties)
    Map<String, Integer> firstRoundResults;
    // mandates gained by participants(unions and parties) after the second round
    // first round + second round results
    Map<String, Integer> secondRoundResults;
    // surplus mandates gained by participants on the second round
    Map<String, Integer> surplusMandates;
    // current reminder of each union
    Map<String, Double> reminders;
    Map<String, Double> initUnionReminders;
    Map<String, String> agreements;
     Map<String, Integer> finalResults;

    int totalVotes;
    double initRate;
    double currentRate;


    public ResultsRepoImpl() {
        agreements = new HashMap<>();
        secondRoundResults = new HashMap<>();
        finalResults  = new HashMap<>();
        initUnionReminders = new HashMap<>();
    }

    public Map<String, Integer> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Map<String, Integer> snapshot) {
        this.snapshot = snapshot;
    }

    public Map<String, Integer> getThresholdSeedResults() {
        return thresholdSeedResults;
    }

    public void setThresholdSeedResults(Map<String, Integer> thresholdSeedResults) {
        this.thresholdSeedResults = thresholdSeedResults;
    }

    public Map<String, Integer> getFirstRoundResults() {
        return firstRoundResults;
    }

    public void setFirstRoundResults(Map<String, Integer> firstRoundResults) {
        this.firstRoundResults = firstRoundResults;
    }

    public Map<String, Integer> getSecondRoundResults() {
        return secondRoundResults;
    }

    public void setSecondRoundResults(Map<String, Integer> secondRoundResults) {
        this.secondRoundResults = secondRoundResults;
    }

    public Map<String, Integer> getSurplusMandates() {
        return surplusMandates;
    }

    public void setSurplusMandates(Map<String, Integer> surplusMandates) {
        this.surplusMandates = surplusMandates;
    }

    public Map<String, Double> getReminders() {
        return reminders;
    }

    public void setReminders(Map<String, Double> reminders) {
        this.reminders = reminders;
    }

    public Map<String, Double> getInitUnionReminders() {
        return initUnionReminders;
    }

    public void setInitUnionReminders(Map<String, Double> initUnionReminders) {
        this.initUnionReminders = initUnionReminders;
    }

    public Map<String, String> getAgreements() {
        return agreements;
    }

    public void setAgreements(Map<String, String> agreements) {
        this.agreements = agreements;
    }

    public Map<String, Integer> getFinalResults() {
        return finalResults;
    }

    public void setFinalResults(Map<String, Integer> finalResults) {
        this.finalResults = finalResults;
    }

    public double getInitRate() {
        return initRate;
    }

    public void setInitRate(double initRate) {
        this.initRate = initRate;
    }

    public double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }
}
