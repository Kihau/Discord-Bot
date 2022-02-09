package music;

import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.Nullable;
import utilities.FileSeeker;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class AudioPlayer implements AudioSendHandler{

    private final static HashSet<String> SUPPORTED_FORMATS = new HashSet<>(Arrays.asList("wav","mp3","snd","aiff","aifc","au","flac"));
    private final static String HOME_DIR = System.getProperty("user.home");
    public static File AUDIO_FILES_DIR = new File(HOME_DIR);
    private boolean looping = false;

    final static int NUM_OF_SONGS = 30;
    static HashMap<String,AudioTrack> fileNamesToSongs = new HashMap<>(NUM_OF_SONGS);

    private AudioTrack audioTrack;
    private ByteBuffer audioBuffer;

    private int methodCalls = 0;
    protected int offset = 0;
    protected int length = 0;
    volatile protected double fragmentsOf20Ms = 0;

    protected AudioManager audioManager;
    volatile private boolean playing = false;

    public AudioPlayer(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public void setPlaying(boolean flag){
        if(audioBuffer == null){
            System.err.println("Audio buffer null");
            return;
        }
        this.playing = flag;
    }

    /**
     * invert looping value
     * @return new status
     */
    public boolean switchLooping(){
        looping ^= true;
        return looping;
    }

    public static boolean isExtensionSupported(String extension){
        return SUPPORTED_FORMATS.contains(extension);
    }

    public AudioTrack getCurrentAudioTrack(){
        return this.audioTrack;
    }
    /**
     * @param trackName - track key
     * @return false if track doesn't exist
     */
    public boolean setAudioTrack(String trackName){
        if(fileNamesToSongs.containsKey(trackName)){
            this.audioTrack = fileNamesToSongs.get(trackName);
        }else{
            FileSeeker fileSeeker = new FileSeeker(trackName, AUDIO_FILES_DIR.getAbsolutePath());
            String containedPath = fileSeeker.findContainingPath();
            if(containedPath.isEmpty()){
                return false;
            }else{
                this.audioTrack = new AudioTrack(containedPath);
                fileNamesToSongs.put(trackName, audioTrack);
            }

        }
        loadTrack();
        return true;
    }


    private void loadTrack(){
        byte [] bytes = audioTrack.getSongBytes();
        if(bytes == null){
            System.err.println("Underlying array null");
            return;
        }
        this.audioBuffer = ByteBuffer.wrap(bytes);
        //offset = audioTrack.getOffset();
        rewindTrack();
        fragmentsOf20Ms = audioTrack.fragmentsOf20Ms();
        length = audioTrack.getBaseLength();
    }


    public void rewindTrack(){
        offset = 0;
        methodCalls = 0;
    }


    @Override
    public boolean canProvide(){
        return playing;
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio(){
        methodCalls++;

        if(methodCalls >= fragmentsOf20Ms){
            playing = false;
            System.out.println("Finished after " + methodCalls + " calls");
            if(looping){
                rewindTrack();
                playing = true;
            }
        }

        audioBuffer.position(offset);
        audioBuffer.limit(offset+=length);

        return audioBuffer;
    }
    //encodes audio to opus if not Opus
    @Override
    public boolean isOpus(){
        return false;
    }
}
