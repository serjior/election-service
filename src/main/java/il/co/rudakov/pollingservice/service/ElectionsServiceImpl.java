package il.co.rudakov.pollingservice.service;

import il.co.rudakov.pollingservice.error_hadler.Exception400;
import il.co.rudakov.pollingservice.error_hadler.Exception409;
import il.co.rudakov.pollingservice.model.ModelInterface;
import il.co.rudakov.pollingservice.model.ResultsRepoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope("prototype")
public class ElectionsServiceImpl implements ElectionsServiceInterface {

    @Autowired
    private ModelInterface model;
    @Autowired
    ResultsRepoInterface repo;

    public ElectionsServiceImpl() {}

    @Override
    public boolean vote(String partyName) {
        if(model.getVotes() == null || model.getVotes().isEmpty())
            throw new Exception409("Elections has not been launched yet!");
        if(!model.getVotes().containsKey(partyName))
            throw new Exception400("Party not exists. Check the list of parties first!");


        model.getVotes().put(partyName, model.getVotes().get(partyName) + 1);

        return true;
    }


    @Override
    public Map<String, Integer> getResults(Map<String, Integer> votesSnapshot,
                                           Map<String, String> agreements,
                                           double threshold) {

        repo.setSnapshot(votesSnapshot);
        repo.setAgreements(agreements);


        // I. GETTING PARTIES PASSED ELECTION THRESHOLD ============================

        // Getting votes threshold
        int totalVotes = repo.getSnapshot()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
        double passBarrier = totalVotes*threshold;

        // Parties seed will return ( Map < Union , Votes > )
        repo.setThresholdSeedResults(getElectionsParticipants(votesSnapshot, agreements, passBarrier));


        // II. FIRST ROUND MANDATES DISTRIBUTION =====================================

        // Counting First round mandate Rate (cost of the mandate on the First round)
        repo.setInitRate(
                votesSnapshot
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum()*1.0/120);

        // Getting first round results ( Map < Union , Mandates > ) and storing them
        repo.setFirstRoundResults(firstRoundDistribution(repo.getThresholdSeedResults(), repo.getInitRate()));

        // FIRST ROUND MANDATES DISTRIBUTION ENDS =====================================


        // III. SECOND ROUND MANDATES DISTRIBUTION ====================================
        // Second round results are Map < Participants, Surplus Mandates > - additional mandates gained by participants

        // Calculating mandates leftovers after the first round
        int leftovers = 120 - repo.getFirstRoundResults()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        if(leftovers == 0 && agreements.isEmpty()) {
            // current state of the surplus mandates distribution (only 0 values) will be the second round results
            repo.getSecondRoundResults().putAll(repo.getSurplusMandates());
            return repo.getFirstRoundResults();
        }

        while(leftovers > 0){
            repo.setCurrentRate(repo.getReminders()
                    .values()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .min().getAsDouble());

            // Starting competition for the next surplus mandate - beneficiary look up algorithm
            String[] result = surplusMandateCompetition(repo.getReminders(),
                    repo.getThresholdSeedResults(),
                    //leftovers,
                    repo.getCurrentRate());
            // adding surplus mandate to the winner
            repo.getSurplusMandates().put(result[0], repo.getSurplusMandates().get(result[0]) +1);
            // reducing winner's
            repo.getReminders().put(result[0], repo.getReminders().get(result[0]) - repo.getCurrentRate());
            repo.getReminders().remove(result[1]);
            // if the winner is the last participant with reminder he gains all the surplus mandates left by the moment
            if(repo.getReminders().size() == 1){
                repo.getSurplusMandates().put(result[0], leftovers);
                break;
            }
            leftovers--;
        }
        // current state of the surplus mandates distribution (only 0 values) will be the second round results
        repo.getSecondRoundResults().putAll(repo.getSurplusMandates());

        // SECOND ROUND MANDATES DISTRIBUTION ENDS ===========================================


        // III. THIRD ROUND MANDATES DISTRIBUTION ===========================================
        // will return final Elections results

        // combining results of the First and Second rounds
        Map<String, Integer> thirdRoundResults = new HashMap<>();
        repo.getFirstRoundResults()
                .entrySet()
                .stream()
                /*.filter(entry -> entry.getValue() > 0)*/
                .forEach(entry -> {
                    thirdRoundResults.put(entry.getKey(), repo.getSecondRoundResults().get(entry.getKey()) + entry.getValue());
                });

        // if there is no any agreement no need to go through the Third round distribution
        if (agreements.isEmpty()) {
            repo.getFirstRoundResults().putAll(thirdRoundResults);
            return thirdRoundResults;
        }

        thirdRoundResults
                .entrySet()
                .stream()
                .forEach(entry -> {
                    if(!entry.getKey().contains(";"))
                        repo.getFinalResults().put(entry.getKey(), entry.getValue());
                    else if(entry.getValue() != 0){
                        String[] parties = entry.getKey().split(";");
                        String party1 = parties[0];
                        String party2 = parties[1];

                        // recovering these parties reminders in the First round
                        double devision1 = repo.getSnapshot().get(party1)*1.0/repo.getInitRate();
                        double devision2 = repo.getSnapshot().get(party2)*1.0/repo.getInitRate();
                        int integerMandades1 = (int)devision1;
                        int integerMandades2 = (int)devision2;
                        double reminder1 = (devision1 - integerMandades1)*repo.getInitRate();
                        double reminder2 = (devision2 - integerMandades2)*repo.getInitRate();
                        repo.setCurrentRate(Math.min(reminder1, reminder2));
                        Map<String, Double> unionReminders = new HashMap<>();
                        unionReminders.put(party1, reminder1);
                        unionReminders.put(party2, reminder2);
                        int surplus = repo.getSecondRoundResults().get(entry.getKey());
                        // just in case block didn't get any surplus mandates
                        repo.getSurplusMandates().put(party1, 0);
                        repo.getSurplusMandates().put(party2, 0);

                        while(surplus > 0){
                            System.out.println("reminders:");
                            System.out.println(party1);
                            System.out.println(unionReminders.get(party1));
                            System.out.println(party2);
                            System.out.println(unionReminders.get(party2));
                            System.out.println("surplus");
                            System.out.println(surplus);
                            if(unionReminders.get(party1) <= 0){
                                repo.getSurplusMandates().put(party2, surplus);
                                break;
                            }
                            if(unionReminders.get(party2) <=0 ){
                                repo.getSurplusMandates().put(party1, surplus);
                                break;
                            }

                            // Starting competition for the next surplus mandate - beneficiary look up algorithm
                            String result[] = surplusMandateCompetition(unionReminders,repo.getSnapshot() /*repo.getThresholdSeedResults()*/, repo.getCurrentRate());
                            // adding surplus mandate to the winner
                            repo.getSurplusMandates().put(result[0], repo.getSurplusMandates().get(result[0]) +1);
                            // reducing winner's reminder
                            unionReminders.put(result[0], unionReminders.get(result[0]) - repo.getCurrentRate());
                            surplus--;
                        }
                        repo.getFinalResults().put(party1, repo.getSurplusMandates().get(party1) + integerMandades1);
                        repo.getFinalResults().put(party2, repo.getSurplusMandates().get(party2) + integerMandades2);
                    }
                });

        return repo.getFinalResults();
    }


    /*----------------------------------- ELECTION ROUNDS -----------------------------------------------------------*/

    // I. THRESHOLD SEED STAGE
    // Reduced snapshot will be returned ( Map < Union , Votes > )


    // Getting valid participants (unions and parties) valid for the the First round distribution
    private Map<String, Integer>getElectionsParticipants(Map<String, Integer> snapshot,
                                                         Map<String, String> agreements,
                                                         double threshold){
        Map<String, Integer> result = new HashMap<>();

        if(agreements == null || agreements.isEmpty()) {
            snapshot
                    .entrySet()
                    .stream()
                    .filter(entry -> (entry.getValue() > threshold))
                    .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
            return result;
        }

        snapshot
                .entrySet()
                .stream()
                .filter(entry -> (entry.getValue() > threshold))
                .forEach(entry ->{
                    String unionParty1 = entry.getKey();
                    if(!agreements.containsKey(unionParty1))
                        result.put(entry.getKey(), entry.getValue());
                    else{
                        String unionParty2 = agreements.get(unionParty1);
                        if(snapshot.get(unionParty2) > threshold){
                            Map.Entry<String, Integer> union = unionCreate(unionParty1, unionParty2, snapshot);
                            result.put(union.getKey(), union.getValue());
                        }
                    }
                });
        return result;
    }

    // Combining a union entry out of two parties
    private Map.Entry<String, Integer> unionCreate(String party1,
                                                   String party2,
                                                   Map<String, Integer> snapshot){
        String union =
                party1.hashCode() > party2.hashCode() ?
                        party1 + ";" + party2 :
                        party2 + ";" + party1;
        Integer votes = snapshot.get(party1) + snapshot.get(party2);
        return new AbstractMap.SimpleEntry<>(union, votes);
    }
    // THRESHOLD SEED STAGE ENDS

    // II. FIRST ROUND DISTRIBUTION
    // Map < Union , Mandates> will be returned

    private Map<String, Integer> firstRoundDistribution(Map<String, Integer> thresholdPassed, double rate){
        Map<String, Integer> result = new HashMap<>();
        repo.setReminders(new HashMap<>());
        repo.setSurplusMandates(new HashMap<>());
        // Calculating mandates for every participant passed threshold and accumulating result in resulting map
        // Filling out First round reminders
        // Initializing surplus mandates storage with 0 values
        thresholdPassed
                .entrySet()
                .stream()
                .forEach(party -> {
                    double divisionResult = party.getValue()*1.0/rate;
                    int integerMandates = (int)divisionResult;
                    double reminder = (divisionResult - integerMandates)*rate;
                    result.put(party.getKey(), integerMandates);
                    repo.getReminders().put(party.getKey(), reminder);
                    // storing separately first round reminders for unions
                    repo.getSurplusMandates().put(party.getKey(), 0);
                });
        return result;
    }
    // FIRST ROUND DISTRIBUTION ENDS


    // III. SURPLUS MANDATES DISTRIBUTION ALGORITHM
    // This method will return party who gains additional mandates on the current "trade" session

    private String[] surplusMandateCompetition(Map<String, Double> reminders,
                                             Map<String, Integer> thresholdSeedResults,
                                             //int leftovers,
                                             double currentRate){

        // new mandate Rate calculation (reevaluated cost of a mandate)
         // seeding participants with reminder less than current mandate Rate
        // removing such participants from reminders table
        // and finding top rated participant (who has the maximum reminder)
        double max = 0;
        // result[0] - stores winner, result[1] - stores looser
        String[] result = new String[2];
        for (Map.Entry<String, Double> part : reminders.entrySet()) {
            if(part.getValue() > max) {
                max = part.getValue();
                result[0] = part.getKey();
            }
            if(part.getValue() == max){
                result[0] = thresholdSeedResults.get(result[0]) > thresholdSeedResults.get(part.getKey())
                        ? result[0]
                        : part.getKey();
            }
            if(part.getValue() == currentRate)
                result[1] = part.getKey();
        }

        return result;
    }


    @Override
    public Map<String, String> getAgreements() {
        return model.getAgreements();
    }

    @Override
    public Set<String> getParties() {
        return model.getVotes().keySet();
    }


    @Override
    public Map<String, Integer> getThresholdSeedRoundResults() {
        return repo.getThresholdSeedResults();
    }


    @Override
    public Map<String, Integer> getFirstRoundResults() {
        return repo.getFirstRoundResults();
    }

    @Override
    public Map<String, Integer> getSecondRoundResults() {
        return repo.getSecondRoundResults();
    }

}
