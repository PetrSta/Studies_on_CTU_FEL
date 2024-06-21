package cz.cvut.fel.agents.pdv.student;

import cz.cvut.fel.agents.pdv.dsand.Message;

import java.io.Serializable;

class HeartbeatMessage extends Message implements Serializable {
    int term;
    String leader;

    HeartbeatMessage(int term, String leader) {
        this.term = term;
        this.leader = leader;

    }
}

