package cz.cvut.fel.agents.pdv.student;

import cz.cvut.fel.agents.pdv.dsand.Message;

class VoteRequestMessage extends Message {
    int term;
    int logSite;
    int lastLogTerm;

    VoteRequestMessage(int term, int logSite, int lastLogTerm) {
        this.term = term;
        this.logSite = logSite;
        this.lastLogTerm = lastLogTerm;
    }
}
