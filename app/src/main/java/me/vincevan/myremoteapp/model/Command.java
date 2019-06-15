package me.vincevan.myremoteapp.model;

public class Command {
    private int id;
    private String title;
    private String desc = "";
    private boolean needPrompt;
    private String promptMessage = "Are you sure?";

    public Command(int id, String title, boolean prompt){
        this.id = id;
        this.title = title;
        this.needPrompt = prompt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isNeedPrompt() {
        return needPrompt;
    }

    public String getPromptMessage() {
        return promptMessage;
    }

    public void setPromptMessage(String promptMessage) {
        this.promptMessage = promptMessage;
    }


    @Override
    public String toString(){
        return this.title;
    }



}
