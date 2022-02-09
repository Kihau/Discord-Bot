package utilities;

import deskort.Bot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Servers {

    private Guild currentGuild;
    private static JDA jdaInterface;
    public Servers(){
        jdaInterface = Bot.getInterface();
        List<Guild> guilds = getServers();
        if(guilds.size() < 1){
            System.err.println("Bot is not on any server");
            System.exit(0);
        }
        currentGuild = guilds.get(0);
    }

    public Guild getServer(long id){
        return jdaInterface.getGuildById(id);
    }
    public Guild getServer(String partialName){
        List<Guild> servers = getServers();
        for(int i = 0; i<servers.size(); i++){
            if(servers.get(i).getName().contains(partialName)){
                currentGuild = servers.get(i);
                return currentGuild;
            }
        }
        return currentGuild;
    }
    public Guild getServerIgnoreCase(String partialName){
        partialName = partialName.toLowerCase(Locale.ROOT);
        List<Guild> servers = getServers();
        for(int i = 0; i<servers.size(); i++){
            if(servers.get(i).getName().toLowerCase(Locale.ROOT).contains(partialName)){
                currentGuild = servers.get(i);
                return currentGuild;
            }
        }
        return currentGuild;
    }
    public List<Guild> getServers(){
        return jdaInterface.getGuilds();
    }
    public List<String> getServerNames(){
        List<Guild> guilds = getServers();
        int numberOfGuilds = guilds.size();
        List<String> serverNames = new ArrayList<>(numberOfGuilds);
        for(int i = 0; i < numberOfGuilds; i++){
            serverNames.add(guilds.get(i).getName());
        }
        return serverNames;
    }
}
