package il.co.rudakov.pollingservice.controller;

import il.co.rudakov.pollingservice.controller.dto.EmulatePollingDto;
import il.co.rudakov.pollingservice.controller.dto.ResponseMessage;
import il.co.rudakov.pollingservice.service.ManagementServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin
public class ManagementController {

    @Autowired
    ManagementServiceInterface admin;


    public ManagementController() {
    }

    @PostMapping("/admin/party/add")
    public ResponseEntity<ResponseMessage> addParty(@RequestParam (value = "name", required = true) String party){
        admin.addParty(party.trim().toUpperCase());

        return new ResponseEntity<>(new ResponseMessage("Party has been added!"), HttpStatus.OK);
    }

    @PostMapping("/admin/agreement/add")
    public ResponseEntity<ResponseMessage> addAgreement(@RequestParam(value="p1") String partyName1,
                            @RequestParam (value = "p2") String partyName2){
        admin.addAgreement(partyName1.trim().toUpperCase(), partyName2.trim().toUpperCase());
        return new ResponseEntity<>(new ResponseMessage("Agreement has been added!"), HttpStatus.OK);
    }

    @PostMapping("/admin/emulate")
    public ResponseEntity<ResponseMessage> emulatePolling(@RequestBody EmulatePollingDto[] request){

        if(request == null || request.length == 0)
            return new ResponseEntity<>(new ResponseMessage("Emulation can not be null or empty!"), HttpStatus.BAD_REQUEST);

        Map<String, Integer> parties = new ConcurrentHashMap<>();
        for (EmulatePollingDto party: request ) {
            parties.put(party.getName().trim().toUpperCase(), party.getVotes());

        }
        admin.emulatePolling(parties);
        return new ResponseEntity<>(new ResponseMessage("Emulation finished. Now you can check results!"), HttpStatus.OK);
    }
    @DeleteMapping("/admin/agreements/drop")
    public ResponseEntity<ResponseMessage> dropAgreements(){
        admin.dropAgreements();
        return new ResponseEntity<>(new ResponseMessage("OK"), HttpStatus.OK);
    }

}
