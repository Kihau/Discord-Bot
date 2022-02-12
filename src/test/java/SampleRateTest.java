import org.junit.Test;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class SampleRateTest{
    protected final static float SAMPLE_RATE_OFFSET = 500f;
    protected final static float TARGET_SAMPLE_RATE = 48000f;
    protected final static AudioFormat TARGET_FORMAT = new AudioFormat(TARGET_SAMPLE_RATE, 16, 2, true,true);
    private final static String DEFAULT_AUDIO_PATH = System.getProperty("user.home") + "\\Desktop\\audio\\";
    @Test
    public void test1(){
        byte [] arr = new byte[4556];
        for (int i = 44; i < arr.length; i++){
            arr[i] = (byte) ((Math.random() + 1)* 50D);
        }
        //ratio 48000/44100
        double factor = 1.08;
        int originalLength = arr.length - 44;
        int bufferLength = (int) (originalLength*factor);
        System.out.println("Original length: " + arr.length);
        System.out.println("Bufferlength: " + bufferLength);
        System.out.println(Arrays.toString(arr));
        byte [] result = SoundUtils.interpolationImpl(arr,bufferLength,5);

        System.out.println(Arrays.toString(result));
    }
    @Test
    public void test2() throws IOException{
        byte [] bytes = Files.readAllBytes(Paths.get(DEFAULT_AUDIO_PATH+"026-44.wav"));
        byte [] fileInfo = new byte[44];
        System.arraycopy(bytes, 0, fileInfo, 0, 44);
        byte[] result = SoundUtils.interpolate(bytes,44100,48000);

        byte [] finalArr = new byte[result.length + 44];
        System.arraycopy(fileInfo, 0, finalArr, 0, 44);
        System.arraycopy(result, 0, finalArr, 44, result.length);
        Files.write(Paths.get(DEFAULT_AUDIO_PATH + "shit.wav"),finalArr);
        System.out.println("Success");
    }
    @Test
    public void test3() {
        AudioInputStream sourceStream = null;
        try{
            sourceStream = AudioSystem.getAudioInputStream(new File(DEFAULT_AUDIO_PATH+"thursday.aiff"));
        }catch (UnsupportedAudioFileException e){
            e.printStackTrace();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
        assert sourceStream != null;
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat upscaledSampleFormat = new AudioFormat(TARGET_SAMPLE_RATE, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), true,sourceFormat.isBigEndian());

        boolean support = AudioSystem.isConversionSupported(TARGET_FORMAT,sourceStream.getFormat());
        System.out.println("Support: "+ support);

        System.out.println("Initial: "+ sourceStream.getFrameLength());
        sourceStream = AudioSystem.getAudioInputStream(upscaledSampleFormat,sourceStream);
        long frameLength = sourceStream.getFrameLength();
        int frameSize =  sourceStream.getFormat().getFrameSize();
        System.out.println("Frame length after: "+ frameLength);
        System.out.println("Frame size after: "+ frameSize);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        System.out.println("initial output stream size: " + bytes.size());
        int bytesWrittenToTheOutputStream = 0;
        try{
            bytesWrittenToTheOutputStream = AudioSystem.write(sourceStream, AudioFileFormat.Type.AIFF,new File("C:\\Users\\Frisk\\Desktop\\pusss.aiff"));
        }catch (IOException ioException){
            System.out.println("Caught an error at writing");
        }
        System.out.println("after outputstream size: " + bytes.size());
        System.out.println("bytesWrittenToTheOutputStream: " + bytesWrittenToTheOutputStream);
        byte[] temp = bytes.toByteArray();
        System.out.println(Arrays.toString(temp));

    }
    @Test
    public void test4() {

        AudioInputStream sourceStream = null;

        try{
            sourceStream = AudioSystem.getAudioInputStream(new File(DEFAULT_AUDIO_PATH+"thursday.aiff"));

        }catch (UnsupportedAudioFileException e){
            e.printStackTrace();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
        /*Clip clip;
        try{
            clip = AudioSystem.getClip();
            clip.open(sourceStream);
            clip.drain();
            clip.close();
            while(clip.isOpen()){
                System.out.println("Open");
            }
            clip = null;
        }catch (LineUnavailableException | IOException e){
            e.printStackTrace();
        }*/

        assert sourceStream != null;
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat upscaledSampleFormat = new AudioFormat(TARGET_SAMPLE_RATE, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), true,sourceFormat.isBigEndian());

        boolean support = AudioSystem.isConversionSupported(TARGET_FORMAT,sourceStream.getFormat());
        System.out.println("Support: "+ support);

        System.out.println("Initial: "+ sourceStream.getFrameLength());
        sourceStream = AudioSystem.getAudioInputStream(upscaledSampleFormat,sourceStream);
        long frameLength = sourceStream.getFrameLength();
        int frameSize =  sourceStream.getFormat().getFrameSize();
        float frameRate =  sourceStream.getFormat().getFrameRate();
        System.out.println("Frame length after: "+ frameLength);
        System.out.println("Frame size after: "+ frameSize);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        System.out.println("Initial output stream size: " + bytes.size());
        int bytesWritten = 0;
        try{
            bytesWritten = AudioSystem.write(sourceStream, AudioFileFormat.Type.AIFF,new File("C:\\Users\\Frisk\\Desktop\\pusss.aiff"));
        }catch (IOException ioException){
            System.out.println("Caught an error at writing");
        }
        System.out.println("After output stream size: " + bytes.size());
        System.out.println("Bytes written: " + bytesWritten);
        byte[] temp = bytes.toByteArray();
        System.out.println(Arrays.toString(temp));

    }
    @Test
    public void unsupported(){
        byte [] arr = new byte[123];
        SoundUtils.interpolate(arr,49100,48000);

    }
}
