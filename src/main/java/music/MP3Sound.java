package music;

import fr.delthas.javamp3.Sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class MP3Sound{
    protected Path path;
    private Sound sound;
    protected MP3Sound(String filePath) throws IOException{
        path = Paths.get(filePath);
        if(!Files.exists(path)){
            throw new IOException("File doesn't exist");
        }
    }
    private void createSound(){
        if(sound != null){
            return;
        }
        try{
            sound = new Sound(new BufferedInputStream(Files.newInputStream(path)));
        }catch (IOException ioException){
            ioException.printStackTrace();
            System.err.println("Unlucky");
        }
    }

    public Sound getSound(){
        createSound();
        return sound;
    }
}
