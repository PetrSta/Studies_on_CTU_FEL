package cz.cvut.fel.agents.pdv.swim;

import cz.cvut.fel.agents.pdv.dsand.Message;
import cz.cvut.fel.agents.pdv.dsand.Pair;

import java.util.*;
import java.util.Map.Entry;

public class ActStrategy {
    private final int maxDelayForMessages;
    private final List<String> otherProcesses;
    private final Map<String, Integer> lastPinged;
    private final Map<String, ArrayList<String>> pingRequests;
    private final List<String> sentPings;
    private String thisProcess = null;
    private int time = 0;
    private int index;

    public ActStrategy(int maxDelayForMessages, List<String> otherProcesses,
           int ignoredTimeToDetectKilledProcess, int ignoredUpperBoundOnMessages) {
        this.maxDelayForMessages = maxDelayForMessages;
        this.otherProcesses = otherProcesses;

        this.lastPinged = new HashMap<>();
        this.pingRequests = new HashMap<>();
        this.sentPings = new ArrayList<>();

        Random random = new Random();
        this.index = random.nextInt(otherProcesses.size());
    }

    enum instances {
        RequestMessage,
        PingMessage,
        DeadProcessMessage,
        AcknowledgmentMessage
    }

    public List<Pair<String, Message>> act(Queue<Message> inbox, String disseminationProcess) {
        Set<Pair<String, Message>> outbox = new HashSet<>();

        while(!inbox.isEmpty()) {
            Message message = inbox.remove();

            if (Objects.equals(thisProcess, null)) {
                thisProcess = message.recipient;
            }

            instances instance = instances.valueOf(message.getClass().getSimpleName());

            switch (instance) {
                case PingMessage:
                    PingMessage ping = (PingMessage) message;
                    String pingRecipient = ping.recipient;
                    outbox.add(new Pair<>(ping.sender, new AcknowledgmentMessage(pingRecipient)));
                    break;

                case RequestMessage:
                    RequestMessage request = (RequestMessage) message;
                    String requestProcessID = request.getProcessID();
                    String requestSender = request.sender;

                    if (pingRequests.containsKey(requestProcessID)) {
                        if (!(pingRequests.get(requestProcessID).contains(requestSender))) {
                            pingRequests.get(requestProcessID).add(requestSender);
                        }
                    } else {
                        pingRequests.put(requestProcessID, new ArrayList<>());
                        pingRequests.get(requestProcessID).add(requestSender);
                    }

                    outbox.add(new Pair<>(requestProcessID, new PingMessage(requestProcessID)));
                    break;

                case DeadProcessMessage:
                    DeadProcessMessage deadProcess = (DeadProcessMessage) message;
                    String deadProcessID = deadProcess.getProcessID();
                    otherProcesses.remove(deadProcessID);
                    break;

                case AcknowledgmentMessage:
                    AcknowledgmentMessage acknowledgment = (AcknowledgmentMessage) message;
                    String acknowledgmentProcessID = acknowledgment.getProcessID();

                    if (pingRequests.containsKey(acknowledgmentProcessID)) {
                        for (String requester : pingRequests.get(acknowledgmentProcessID)) {
                            outbox.add(new Pair<>(requester, acknowledgment));
                        }
                    }

                    lastPinged.remove(acknowledgmentProcessID);
                    pingRequests.remove(acknowledgmentProcessID);
                    break;
            }
        }

        List<String> deadProcesses = new ArrayList<>();

        for(Entry<String, Integer> lastPing : lastPinged.entrySet()) {
            String process = lastPing.getKey();
            Integer lastTime = lastPing.getValue();

            if(time > lastTime + (6 * maxDelayForMessages)) {
                outbox.add(new Pair<>(disseminationProcess, new PFDMessage(process)));
                deadProcesses.add(process);
                continue;
            }

            if(!sentPings.contains(process) && time > lastTime + 3 * maxDelayForMessages) {
                List<String> copiedNeighbors = otherProcesses;
                Collections.shuffle(copiedNeighbors);
                int count = 0;

                for (String copiedNeighbor : copiedNeighbors) {
                    if(count == 9) {
                        break;
                    }
                    outbox.add(new Pair<>(copiedNeighbor, new RequestMessage(process)));
                    count ++;
                }

                sentPings.add(process);
            }
        }

        for (String deadProcess : deadProcesses) {
            lastPinged.remove(deadProcess);
        }

        if(time % 2 == 0) {
            index = (index + 1) % otherProcesses.size();
            String nextNeighbor = otherProcesses.get(index);

            while(pingRequests.containsKey(nextNeighbor)) {
                index = (index + 1) % otherProcesses.size();
                nextNeighbor = otherProcesses.get(index);
            }

            lastPinged.put(nextNeighbor, time);
            outbox.add(new Pair<>(nextNeighbor, new PingMessage(nextNeighbor)));
        }

        time += 1;
        return new ArrayList<>(outbox);
    }
}
