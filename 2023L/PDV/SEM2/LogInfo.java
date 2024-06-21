package cz.cvut.fel.agents.pdv.student;

import cz.cvut.fel.agents.pdv.raft.messages.IOperation;

import java.io.Serializable;

public class LogInfo implements Serializable {
    final int term;
    final IOperation operation;

    LogInfo(int term, IOperation operation) {
        this.term = term;
        this.operation = operation;
    }
}
