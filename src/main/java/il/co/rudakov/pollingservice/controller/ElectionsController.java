package il.co.rudakov.pollingservice.controller;


import il.co.rudakov.pollingservice.controller.dto.ResponseMessage;
import il.co.rudakov.pollingservice.error_hadler.Exception409;
import il.co.rudakov.pollingservice.provider.ControllerInteractorInterface;
import il.co.rudakov.pollingservice.service.ElectionsServiceInterface;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin
public class ElectionsController {

    @Autowired
    ElectionsServiceInterface service;
    @Autowired
    ObjectFactory<VoteThread>  threadFactory;
    @Autowired
    ControllerInteractorInterface modelInteractor;

    @PostMapping("/vote")
    public ResponseEntity<ResponseMessage> vote(@RequestParam(value="party", required = true) String partyName){
        service.vote(partyName.trim().toUpperCase());
        return new ResponseEntity<>(new ResponseMessage("Vote accepted!"), HttpStatus.OK);

    }

    @GetMapping("/results")
    public ResponseEntity<Map<String, Integer>> getResults(){
        return new ResponseEntity<>(service.getResults(modelInteractor.getVotes(),
                modelInteractor.getAgreements(),
                0.035),
                HttpStatus.OK);
    }

    @GetMapping("/results/thresholdpassed")
    public ResponseEntity<Map<String, Integer>> getThresholdPassedResults(){
        Map<String, Integer> results = service.getThresholdSeedRoundResults();
        if(results == null || results.isEmpty())
            throw new Exception409("Results have not been calculated yet!");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/results/firstround")
    public ResponseEntity<Map<String, Integer>> getFirstRoundResults(){
        Map<String, Integer> results = service.getFirstRoundResults();
        if(results == null || results.isEmpty())
            throw new Exception409("Results have not been calculated yet!");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/results/secondround")
    public ResponseEntity<Map<String, Integer>> getSecondRoundResults(){
        Map<String, Integer> results = service.getSecondRoundResults();
        if(results == null || results.isEmpty())
            throw new Exception409("Results have not been calculated yet!");
        return new ResponseEntity<>(service.getSecondRoundResults(), HttpStatus.OK);
    }

    @GetMapping("/agreements")
    public ResponseEntity<Map<String, String>> getAgreements(){
        return new ResponseEntity<>(service.getAgreements(), HttpStatus.OK);
    }

    @GetMapping("/parties")
    public ResponseEntity<Set<String>> getParties(){
        return new ResponseEntity<>(service.getParties(), HttpStatus.OK);
    }


}
