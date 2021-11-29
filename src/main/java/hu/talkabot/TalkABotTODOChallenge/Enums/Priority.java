package hu.talkabot.TalkABotTODOChallenge.Enums;

public enum Priority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");


    String priority;
    Priority(String priority){
        this.priority = priority;
    }

    public String getPriority(){
        return priority;
    }
}
