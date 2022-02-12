import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


public class SoundUtils{
    private final static String HOME_DIR = System.getProperty("user.home");
    private final static int OFFSET = 44;
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException{
        //customTone(48000,1,1);
        //byte [] arr = {15,30};
        byte [] arr = {13,34,56,76};
        System.out.println(Arrays.toString(interpolateByteStream(arr,4)));
        confirmConversion();


    }


    public static void confirmConversion() throws IOException, UnsupportedAudioFileException{
        AudioFormat format2 = new AudioFormat(44100f, 16, 2, true, false);
        AudioFormat format1 = new AudioFormat(48000f, 16, 2, true, false);

        boolean isSupported = AudioSystem.isConversionSupported(format1,format2);

        /*File perfectFile = new File("C:\\Users\\Frisk\\Desktop\\audio\\thursday_48.aiff");
        AudioFileFormat perfectFileFormat = AudioSystem.getAudioFileFormat(perfectFile);*/

        File sourceFile = new File("C:\\Users\\Frisk\\Desktop\\audio\\thursday.aiff");
        AudioInputStream sourceAIS = AudioSystem.getAudioInputStream(sourceFile);
        AudioFileFormat sourceFileFormat = AudioSystem.getAudioFileFormat(sourceFile);

        AudioFormat sourceFormat = sourceFileFormat.getFormat();

        AudioFormat targetFormat = new AudioFormat(
                sourceFormat.getEncoding(),
                48000f,
                sourceFormat.getSampleSizeInBits(),
                sourceFormat.getChannels(),
                sourceFormat.getFrameSize(),
                48000f,
                sourceFormat.isBigEndian());

        AudioInputStream fortyEightKhzInputStream = AudioSystem.getAudioInputStream(targetFormat, sourceAIS);
        System.out.println(fortyEightKhzInputStream.getFrameLength());

        BufferedInputStream bufferedInputStream = new BufferedInputStream(fortyEightKhzInputStream);
        byte [] buffer = new byte[100];
        int actualRead = bufferedInputStream.read(buffer,0,100);
        System.out.println("actual read: " + actualRead);
        System.out.println(Arrays.toString(buffer));


        File outputFile = new File("C:\\Users\\Frisk\\Desktop\\audio\\converted.aiff");
        //FileOutputStream fos = new FileOutputStream(outputFile);
        int writtenBytes = AudioSystem.write(fortyEightKhzInputStream, AudioFileFormat.Type.AIFF, outputFile);
        System.out.println("Bytes written: " + writtenBytes);
    }

    public static float GIVEN_SAMPLE_RATE = 44100f;

    public static byte [] interpolateByteStream(byte [] rawAudio, int extraBytes){
        if(extraBytes<1){
            return rawAudio;
        }
        //[length + ((length-1) * extra)]
        int len = rawAudio.length;
        byte [] buffer = new byte[len*(extraBytes+1) -extraBytes];
        buffer[0] = rawAudio[0];
        for (int i = 0, extraIndex = 1; i < len -1; i++, extraIndex+=extraBytes+1){ ;
            int firstElement = rawAudio[i], lastElement = rawAudio[i+1];
            int n = 2 + extraBytes;
            double preciseDiff =  (lastElement - firstElement) / (double)(n-1);
            int termDifference = (int) preciseDiff;
            if(preciseDiff%1 != 0){
                termDifference = (int) Math.floor(preciseDiff);
            }

            for (int b = extraIndex; b < extraIndex+extraBytes; b++){
                buffer[b] = (byte) (buffer[b-1] + termDifference);
            }
            buffer[extraIndex+extraBytes] = rawAudio[i+1];
        }
        return buffer;
    }
    //upscaler
    public static byte [] interpolate(byte [] rawAudio, int givenSampleRate, int targetSampleRate){
        double factor = targetSampleRate/(double)givenSampleRate;
        if(factor<1 || rawAudio.length<OFFSET){
            System.out.println("Not interpolating");
            return rawAudio;
        }

        int originalLength = rawAudio.length - OFFSET;
        int bufferLength = (int) (originalLength*factor);
        int diff = bufferLength-originalLength;
        // distance at which it's interpolated
        int distance = bufferLength/diff -1;

        return interpolationImpl(rawAudio,bufferLength, distance);

    }
    public static byte [] interpolationImpl(byte [] rawAudio, int bufferLength, int distance){
        byte [] buffer = new byte[bufferLength];
        buffer[0] = rawAudio[OFFSET];
        for(int i = OFFSET, bufferIndex = 0; i< rawAudio.length - distance && bufferIndex<bufferLength - distance - 2; i+=distance, bufferIndex+=distance+1){

            byte midValue = (byte) ((rawAudio[i] + rawAudio[i+1]) / 2);
            buffer[bufferIndex+1] = midValue;
            System.arraycopy(rawAudio,i + 1,buffer,bufferIndex + 2,distance);

        }
        return buffer;
    }


    public static void customTone(int hz, int millis, double vol) throws IOException{
        byte [] buf = Files.readAllBytes(Paths.get(HOME_DIR + "//Desktop//audio//thursday_copy.aiff"));
        for(int i = OFFSET+2; i<buf.length; i++){
            double angle = i / (GIVEN_SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[i] = (byte) (Math.sin(angle) * 127.0 * vol);
        }
        Files.write(Paths.get(HOME_DIR + "//Desktop//audio//converted.aiff"),buf);
    }

    public static void tone(int hz, int millis, double vol) throws LineUnavailableException{

        AudioFormat af = new AudioFormat(GIVEN_SAMPLE_RATE, 16, 2, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);

        sdl.open(af);
        sdl.start();
        byte[] buf = new byte[1];
        for (int i = 0; i < millis * 8; i++){
            double angle = i / (GIVEN_SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
            sdl.write(buf, 0, 1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
}
