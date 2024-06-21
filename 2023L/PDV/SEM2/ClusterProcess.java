package cz.cvut.fel.agents.pdv.student;

import cz.cvut.fel.agents.pdv.dsand.Message;
import cz.cvut.fel.agents.pdv.raft.RaftProcess;
import cz.cvut.fel.agents.pdv.raft.messages.*;

import java.util.*;
import java.util.function.BiConsumer;

import static java.lang.StrictMath.max;

public class ClusterProcess extends RaftProcess<Map<String, String>> {
    enum State {
        Follower,
        Candidate,
        Leader
    }

    private int getElectionTimeout() {
        int minimum = 5 * networkDelays;
        int maximum = 10 * networkDelays;
        return (int)((Math.random() * (maximum  - minimum)) + minimum);
    }
    // ostatni procesy v clusteru
    private final List<String> otherProcessesInCluster;
    // maximalni spozdeni v siti
    private final int networkDelays;

    private State processState;
    private int currentTerm;
    private String votedForProcess;
    private final Log processLog;
    private int logicalTime = 0;
    private Integer timeToElect;
    private String leader;
    private final Set<String> votesForThisProcess;
    private final Map<String, Integer> voteTimeouts;
    private final Map<String, Integer> hearthBeatTimeouts;
    private final boolean GRANTED = true;
    private final boolean NOT_GIVEN = false;

    public ClusterProcess(String id, Queue<Message> inbox, BiConsumer<String, Message> outbox,
                          List<String> otherProcessesInCluster, int networkDelays) {
        super(id, inbox, outbox);
        this.networkDelays = max(1, networkDelays);
        this.otherProcessesInCluster = otherProcessesInCluster;

        // at the beginning there is no leader and everyone is follower
        processState = State.Follower;
        leader = null;
        currentTerm = 0;
        // create log and containers for info about other processes
        processLog = new Log();
        votesForThisProcess = new HashSet<>();
        voteTimeouts = new HashMap<>();
        hearthBeatTimeouts = new HashMap<>();
        // add baseline information
        for (String otherProcess : otherProcessesInCluster) {
            voteTimeouts.put(otherProcess, 0);
            hearthBeatTimeouts.put(otherProcess, 0);
        }
        // set random election timeout
        timeToElect = logicalTime + getElectionTimeout();
    }

    @Override
    public Optional<Map<String, String>> getLastSnapshotOfLog() {
        return Optional.empty();
    }

    @Override
    public String getCurrentLeader() {
        return leader;
    }

    private boolean checkTerm(int messageTerm) {
        if (currentTerm < messageTerm) {
            updateFromOldTerm(messageTerm);
            return true;
        }
        return false;
    }

    private void updateFromOldTerm(int term) {
        // function used to update processes from old term
        processState = State.Follower;
        currentTerm = term;
        votedForProcess = null;
        timeToElect = logicalTime + getElectionTimeout();
    }

    private void newElection() {
        // function used to start new election
        processState = State.Candidate;
        currentTerm += 1;
        timeToElect = logicalTime + getElectionTimeout();

        // votes for self and prepares to receive responses
        votedForProcess = getId();
        votesForThisProcess.clear();

        // send out request for votes from others
        for (String otherProcess : otherProcessesInCluster) {
            requestVotes(otherProcess);
        }
    }

    private void becomeLeader() {
        // function used when process becomes leader
        processState = State.Leader;
        leader = getId();

        for (String otherProcess : otherProcessesInCluster) {
            // leader shouldn't request votes
            voteTimeouts.put(otherProcess, null);
            // we need ti send out heartbeats ASAP
            hearthBeatTimeouts.put(otherProcess, 0);
        }
        // leader should not begin new election
        timeToElect = null;
    }

    private void requestVotes(String recipient) {
        // set timeout when we need to ask process again if we don't get answer
        voteTimeouts.put(recipient, logicalTime + 2 * networkDelays + 1);
        // info to include in message
        int logSize = processLog.getLogListSize();
        int lastLogTerm = processLog.lastLogTerm();
        //send message
        send(recipient, new VoteRequestMessage(currentTerm, lastLogTerm, logSize));
    }

    private void sendOutHeartbeat(String recipient) {
        // function used to manage heartbeat
        // send heartbeat
        send(recipient, new HeartbeatMessage(currentTerm, getId()));
        // new timeout to send heartbeat
        int timeout = logicalTime + 2 * networkDelays + 2;
        hearthBeatTimeouts.put(recipient, timeout);
    }

    private void voteRequest(VoteRequestMessage voteRequestMessage) {
        // function used to handle VoteRequestMessages
        int requestTerm = voteRequestMessage.term;
        // check if we are in the correct term
        boolean skip = checkTerm(requestTerm);

        if(!skip) {
            boolean vote = NOT_GIVEN;
            // conditions to check before we can grant our vote
            boolean correctTerm = currentTerm == requestTerm;
            boolean canVoteForThisProcess = votedForProcess == null || votedForProcess.equals(voteRequestMessage.sender);
            boolean upToDate = voteRequestMessage.lastLogTerm > processLog.lastLogTerm() ||
                voteRequestMessage.lastLogTerm == processLog.lastLogTerm() && voteRequestMessage.logSite >= processLog.getLogListSize();

            // if the conditions are met we can grant our vote
            if (correctTerm && canVoteForThisProcess && upToDate) {
                vote = GRANTED;
                votedForProcess = voteRequestMessage.sender;
                timeToElect = logicalTime + getElectionTimeout();
            }
            // send our vote
            send(voteRequestMessage.sender, new ResponseToVoteRequestMessage(currentTerm, vote));
        }
    }

    private void responseToVoteRequest(ResponseToVoteRequestMessage responseToVoteRequestMessage) {
        // function used to handle response to voteRequestMessages
        int responseTerm = responseToVoteRequestMessage.term;
        // check if we are in the correct term
        boolean skip = checkTerm(responseTerm);

        if(!skip) {
            // if we are currently candidate in election and are in the correct term
            if (processState == State.Candidate && currentTerm == responseTerm) {
                // we don't need to ask for vote from this process anymore
                voteTimeouts.put(responseToVoteRequestMessage.sender, null);
                // if the response granted us a vote add it to map
                if (responseToVoteRequestMessage.vote) {
                    votesForThisProcess.add(responseToVoteRequestMessage.sender);
                }
            }
        }

    }

    private void heartbeat(HeartbeatMessage heartbeatMessage) {
        // function used to handle heartbeatMessage
        int heartbeatTerm = heartbeatMessage.term;
        // check if we are in the correct term
        boolean skip = checkTerm(heartbeatTerm);

        if(!skip) {
            // if we are in the correct term
            if (currentTerm == heartbeatTerm) {
                // since we got heartbeat from leader we must be follower
                processState = State.Follower;
                // set our leader
                leader = heartbeatMessage.leader;
                // reset election timeout
                timeToElect = logicalTime + getElectionTimeout();
            }
        }
    }

    @Override
    public void act() {
        // increment logicalTime
        logicalTime++;

        // check if the conditions for need election are met and if so start it
        if (timeToElect != null && (processState == State.Follower || processState == State.Candidate) && timeToElect < logicalTime) {
            newElection();
        }

        // check if we got enough votes and if so we are the new leader
        if (processState == State.Candidate && votesForThisProcess.size() + 1 > otherProcessesInCluster.size() / 2) {
            becomeLeader();
        }

        // actions needed to be done for every other process in cluster
        for (String otherProcess : otherProcessesInCluster) {
            // check if we shouldn't have gotten response to our vote request already, if so ask again
            boolean timeToRequestVote = false;
            if(voteTimeouts.get(otherProcess) != null) {
                timeToRequestVote = voteTimeouts.get(otherProcess) <= logicalTime;
            }
            if (processState == State.Candidate && timeToRequestVote) {
                requestVotes(otherProcess);
            }

            // check if we shouldn't send out new heartbeat, if so do it
            boolean heartbeatCheck = hearthBeatTimeouts.get(otherProcess) <= logicalTime;
            if(processState == State.Leader && heartbeatCheck) {
                sendOutHeartbeat(otherProcess);
            }
        }

        // deal with messages in inbox
        for (Message message : inbox) {
            if (message instanceof VoteRequestMessage) {
                VoteRequestMessage voteRequestMessage;
                voteRequestMessage = (VoteRequestMessage) message;
                voteRequest(voteRequestMessage);
            }
            else if (message instanceof ResponseToVoteRequestMessage) {
                ResponseToVoteRequestMessage responseToVoteRequestMessage;
                responseToVoteRequestMessage = (ResponseToVoteRequestMessage) message;
                responseToVoteRequest(responseToVoteRequestMessage);
            }
            else if (message instanceof HeartbeatMessage) {
                HeartbeatMessage heartbeatMessage;
                heartbeatMessage = (HeartbeatMessage) message;
                heartbeat(heartbeatMessage);
            }
            else if (message instanceof ClientRequestWhoIsLeader) {
                ClientRequestWhoIsLeader clientRequest = (ClientRequestWhoIsLeader) message;
                send(clientRequest.sender, new ServerResponseLeader(clientRequest.getRequestId(), getCurrentLeader()));
            }
        }
        // clear junk from inbox -> currently some message types are not handled, code for elections only
        inbox.clear();
    }
}
