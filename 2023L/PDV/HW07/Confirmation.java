package cz.cvut.fel.agents.pdv.exclusion;

import cz.cvut.fel.agents.pdv.clocked.ClockedMessage;
public class Confirmation extends ClockedMessage {
    public String criticalSectionName;
    public Confirmation(String criticalSectionName) {
        this.criticalSectionName = criticalSectionName;
    }
}
