package bot.deskort;

class RequestFunction{
    private final Runnable runnableFunc;
    private final boolean triggerableByBot;
    private final boolean requiresSudo;

    protected RequestFunction(Runnable runnable, boolean triggerableByBot, boolean requiresSudo){
        runnableFunc = runnable;
        this.triggerableByBot = triggerableByBot;
        this.requiresSudo = requiresSudo;
    }
    protected RequestFunction(Runnable runnable, boolean triggerableByBot){
        runnableFunc = runnable;
        this.triggerableByBot = triggerableByBot;
        this.requiresSudo = false;
    }
    void run(){
        runnableFunc.run();
    }
    public boolean isTriggerableByBot(){
        return triggerableByBot;
    }
    public boolean requiresSudo(){
        return requiresSudo;
    }
}
