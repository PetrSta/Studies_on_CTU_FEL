package cz.cvut.fel.agents.pdv.exclusion;

import cz.cvut.fel.agents.pdv.clocked.ClockedMessage;

public class Request extends ClockedMessage {

    public String criticalSectionName;

    public Request (String criticalSectionName) {
        this.criticalSectionName = criticalSectionName;
    }
}
