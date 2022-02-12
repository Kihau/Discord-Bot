import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Bit24Test{
    protected final static float TARGET_SAMPLE_RATE = 48000f;
    protected static byte[] songBytes;
    private final static String HOME_DIR = System.getProperty("user.home");
    private final static String DEFAULT_AUDIO_PATH = HOME_DIR + "\\Desktop\\audio\\";
    protected final static AudioFormat TARGET_FORMAT = new AudioFormat(TARGET_SAMPLE_RATE, 16, 2, true,true);
    protected final static String PATH  = DEFAULT_AUDIO_PATH + "beat.wav";
    protected static AudioInputStream audioStream;

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException{
        audioStream = AudioSystem.getAudioInputStream(new File(PATH));
        System.out.println("FrameLength: " + audioStream.getFrameLength());
        target48Hz();
    }
    private static void target48Hz() throws IOException{
        if(true){
            AudioFormat sourceFormat = audioStream.getFormat();
            float currentSampleRate = sourceFormat.getSampleRate();
            if(currentSampleRate<TARGET_SAMPLE_RATE-500f || currentSampleRate>TARGET_SAMPLE_RATE+500f){
                System.out.println("-Targeting 48KHz-");
                //AudioFormat resampledAudioFormat = new AudioFormat(TARGET_SAMPLE_RATE, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), true, true);
                boolean isSupported = AudioSystem.isConversionSupported(TARGET_FORMAT, sourceFormat);
                if(!isSupported){
                    System.err.println("-Unable to convert-");
                    return;
                }

                audioStream = AudioSystem.getAudioInputStream(TARGET_FORMAT, audioStream);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                int bytesWritten = 0;
                try{
                    bytesWritten = AudioSystem.write(audioStream, AudioFileFormat.Type.AIFF, bytes);
                }catch (IOException ioException){
                    System.err.println("Caught an error at writing");
                }
                System.out.println("Bytes written: " + bytesWritten);
                songBytes = bytes.toByteArray();
            }
        }
    }
}
