package cz.cvut.fel.agents.pdv.exclusion;

import cz.cvut.fel.agents.pdv.clocked.ClockedMessage;
import cz.cvut.fel.agents.pdv.clocked.ClockedProcess;

import java.util.*;

public class ExclusionPrimitive {

    /**
     * Stavy, ve kterych se zamek muze nachazet.
     */
    enum AcquisitionState {
        RELEASED,    // Uvolneny   - Proces, ktery vlastni aktualni instanci ExclusionPrimitive o pristup do kriticke
                     //              sekce nezada

        WANTED,      // Chteny     - Proces, ktery vlastni aktualni instanci ExclusionPrimitive zada o pristup do
                     //              kriticke sekce. Ten mu ale zatim nebyl odsouhlasen ostatnimi procesy.

        HELD         // Vlastneny  - Procesu bylo prideleno pravo pristupovat ke sdilenemu zdroji.
    }

    private final ClockedProcess owner;             // Proces, ktery vlastni aktualni instanci ExclusionPrimitive

    private final String criticalSectionName;       // Nazev kriticke sekce. POZOR! V aplikaci se muze nachazet vice kritickych
                                                    // sekci s ruznymi nazvy!
    private AcquisitionState state;                 // Aktualni stav zamku (vzhledem k procesu 'owner').
                                                    // state==HELD znamena, ze proces 'owner' muze vstoupit do kriticke sekce
    private int time;

    private final String[] allAccessingProcesses;   // Seznam vsech procesu, ktere mohou chtit pristupovat ke kriticke sekci
                                                    // s nazvem 'criticalSectionName' (a tak spolurozhoduji o udelovani pristupu)

    private final List<Request> requestList;

    private final Map<String, Boolean> processMap;

    // Doplnte pripadne vlastni datove struktury potrebne pro implementaci
    // Ricart-Agrawalova algoritmu pro vzajemne vylouceni

    public ExclusionPrimitive(ClockedProcess owner, String criticalSectionName, String[] allProcesses) {
        this.owner = owner;
        this.criticalSectionName = criticalSectionName;
        this.allAccessingProcesses = allProcesses;

        // Na zacatku je zamek uvolneny
        this.state = AcquisitionState.RELEASED;
        this.time = this.owner.getTime();
        this.requestList = new ArrayList<>();
        this.processMap = new HashMap<>();
    }

    /**
     * Metoda pro zpracovani nove prichozi zpravy
     *
     * @param message    prichozi zprava
     * @return 'true', pokud je zprava 'm' relevantni pro aktualni kritickou sekci.
     */
    public boolean accept(ClockedMessage message) {

        if (message instanceof Request) {
            Request request = (Request) message;
            if (request.criticalSectionName.equals(this.getName())) {
                
                boolean addRequest = this.isHeld();
                if (!addRequest && this.state.equals(AcquisitionState.WANTED)) {
                    if (this.time < request.sentOn) {
                        addRequest = true;
                    } else if (this.time == request.sentOn) {
                        int thisID = Integer.parseInt(this.owner.id.replaceAll("\\D", ""));
                        int senderID = Integer.parseInt(request.sender.replaceAll("\\D", ""));
                        if(thisID < senderID) {
                            addRequest = true;
                        }
                    }
                }

                if (addRequest) {
                    requestList.add(request);
                } else {
                    this.owner.send(request.sender, new Confirmation(this.getName()));
                }
                return true;
            }
        }

        if(message instanceof Confirmation ) {
            Confirmation confirmation = (Confirmation) message;
            if (this.getName().equals(confirmation.criticalSectionName)) {
                this.processMap.put(confirmation.sender, true);

                boolean skip = false;
                for (Map.Entry<String, Boolean> entry : this.processMap.entrySet()) {
                    boolean canEnter = entry.getValue();
                    if (!canEnter) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    this.state = AcquisitionState.HELD;
                }
                return true;
            }
        }
        return false;
    }

    public void requestEnter() {
        this.time = this.owner.getTime();
        for(String process : this.allAccessingProcesses) {
            if (!process.equals(this.owner.id)) {
                this.owner.send(process, new Request(this.getName()));
                this.processMap.put(process, false);
            }
        }
        this.state = AcquisitionState.WANTED;
    }

    public void exit() {
        this.state = AcquisitionState.RELEASED;
        for(Request request : this.requestList) {
            if (request.criticalSectionName.equals(this.getName())) {
                this.owner.send(request.sender, new Confirmation(this.getName()));
            }
        }
        this.requestList.clear();
    }

    public String getName() {
        return criticalSectionName;
    }

    public boolean isHeld() {
        return state == AcquisitionState.HELD;
    }

}
