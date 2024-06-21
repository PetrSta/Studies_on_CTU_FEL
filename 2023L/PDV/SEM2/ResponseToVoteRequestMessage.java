package cz.cvut.fel.agents.pdv.student;

import cz.cvut.fel.agents.pdv.dsand.Message;

class ResponseToVoteRequestMessage extends Message {
    int term;
    boolean vote;
    ResponseToVoteRequestMessage(int term, boolean vote) {
        this.term = term;
        this.vote= vote;
    }
}
