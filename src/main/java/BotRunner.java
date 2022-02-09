import deskort.Bot;

class BotRunner{

    public static void main(String[] args){
        try{
            boolean init = Bot.initialize();
            if(!init){
                return;
            }

        }catch (InterruptedException iExc){}
        System.out.println("Connected to: " + Bot.getServers().getServerNames());
        System.out.println("Threads active: " + Thread.activeCount());

        Thread chatThread = new Thread(() -> Bot.getActions().chatWithBot("bot"));
        chatThread.start();

    }
}
