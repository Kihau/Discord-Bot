import org.junit.Test;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AudioFileTest {
    final static AudioFormat TARGET_FORMAT = new AudioFormat(48000f,16,2,true,true);
    final static String USER_HOME = System.getProperty("user.home");
    @Test
    public void testBig(){
        final String PATH = USER_HOME + "\\Downloads\\hiphop-big.wav";
        processAudioFile(PATH);
    }
    @Test
    public void testCasedBig(){
        final String PATH = USER_HOME + "\\Downloads\\hiphop-cased-big.wav";
        processAudioFile(PATH);
    }
    @Test
    public void testCased(){
        final String PATH =  USER_HOME +"\\Downloads\\hiphop-cased.wav";
        processAudioFile(PATH);
    }
    @Test
    public void testNormal(){
        final String PATH = "F:\\Steam\\steamapps\\common\\Counter-Strike Global Offensive\\csgo\\sound\\kodua\\fortnite_emotes\\emote_zippya.wav";
        processAudioFile(PATH);
    }
    @Test
    public void testSwap(){
        final String PATH = USER_HOME +"\\Downloads\\hiphop-swap.wav";
        processAudioFile(PATH);
    }
    @Test
    public void testRev(){
        final String PATH = USER_HOME +"\\Downloads\\hiphop-rev.wav";
        processAudioFile(PATH);
    }
    @Test
    public void wavToSndAttempt(){
        final String PATH = USER_HOME +"\\Downloads\\hiphop.snd";
        processAudioFile(PATH);
    }
    @Test
    public void finalSND(){
        final String PATH = USER_HOME +"\\Downloads\\hiphop-snd-48.snd";
        processAudioFile(PATH);
    }
    @Test
    public void mp3ToWaveToSnd(){
        final String PATH = USER_HOME +"\\Downloads\\026.snd";
        processAudioFile(PATH);
    }

    public static void processAudioFile(String path){
        byte [] bytes = null;
        try{
            bytes = Files.readAllBytes(Paths.get(path));
        }catch (IOException ioException){
            ioException.printStackTrace();
            return;
        }
        Clip clip = null;
        try{
            clip = AudioSystem.getClip();
        }catch (LineUnavailableException e){
            e.printStackTrace();
            return;
        }

        AudioInputStream inputStream = null;
        try{
            inputStream = AudioSystem.getAudioInputStream(new File(path));
        }catch (UnsupportedAudioFileException uafExc){
            System.err.println("Unsupported file format");
            return;
        }catch (IOException ioExc){
            System.err.println("File not found");
        }

        if(clip == null || inputStream == null){
            return;
        }
        try{
            clip.open(inputStream);
        }catch (LineUnavailableException | IOException e){
            System.err.println("Failed to open file");
            return;
        }

        AudioFormat clipInfo = clip.getFormat();

        int seconds = (int) (clip.getFrameLength() / clipInfo.getFrameRate());
        System.out.println(clipInfo + ", " + clipInfo.getChannels() + " channels, " + seconds + " seconds long, " + bytes.length + " bytes in size");

    }


}
