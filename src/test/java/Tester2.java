import javazoom.jl.decoder.*;
import javazoom.jl.player.Player;
import bot.music.MP3Decoder;

import java.io.*;

public class Tester2{
    public static final String pathToFile = "C:\\Users\\Frisk\\Desktop\\audio\\High enough.mp3";
    public static void main(String[] args) throws IOException, JavaLayerException{
        MP3Decoder mp3Decoder = new MP3Decoder(pathToFile);
        int bytes = mp3Decoder.decodeEntireMP3();
        System.out.println(bytes);

    }
    private static void playerTest() throws JavaLayerException, FileNotFoundException{
        FileInputStream stream = new FileInputStream(pathToFile);
        //InputStream is = Files.newInputStream(Paths.get("C:\\Users\\Frisk\\Desktop\\audio\\High enough.mp3"));
        Player player = new Player(stream);
        player.play();
    }
}
