package il.co.rudakov.pollingservice.service;

import java.util.Map;

public interface ManagementServiceInterface {

    void addParty(String partyName);
    void addAgreement(String partyName1, String partyName2);
    void emulatePolling(Map<String, Integer> emulation);
}
