package cz.cvut.fel.agents.pdv.student;

import cz.cvut.fel.agents.pdv.raft.messages.IOperation;

import java.util.*;

public class Log {
    private final List<LogInfo> logList;

    Log(){
        logList = new ArrayList<>();
    }

    public int getLogListSize(){
        return logList.size();
    }

    public LogInfo getLogInfo(int index) {
        if(index > getLogListSize() || index < 1) {
            System.err.println("Wrong index passed into getLogEntry in Log class");
            return null;
        }
        return logList.get(index - 1);
    }

    public int getLogTerm(int index) {
        if(index > getLogListSize() || index < 1) {
            System.err.println("Wrong index passed into getLogTerm in Log class");
            return 0;
        }
        return logList.get(index - 1).term;
    }

    public IOperation getLogOperation(int index) {
        if(index > getLogListSize() || index < 1) {
            System.err.println("Wrong index passed into getLogOperation in Log class");
            return null;
        }
        return logList.get(index - 1).operation;
    }

    public int lastLogTerm(){
        if(getLogListSize() <= 0) {
            return 0;
        }
        return getLogTerm(getLogListSize());
    }
}
