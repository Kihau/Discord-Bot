package music;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AudioTrack{
    protected final static short WAVE_OFFSET = 44;
    protected final static short MILLIS_20 = 20;
    protected final static boolean CONVERSION_ENABLED = true;
    protected final static float SAMPLE_RATE_OFFSET = 500f;
    protected final static float TARGET_SAMPLE_RATE = 48000f;
    protected final static AudioFormat TARGET_FORMAT = new AudioFormat(TARGET_SAMPLE_RATE, 16, 2, true,true);

    private byte [] songBytes;
    private int length;
    //private int offset;
    private double fragmentsOf20Ms;

    private final String PATH;
    private AudioInputStream audioStream;
    private volatile AudioFormat audioInfo;
    private boolean isBigEndian;
    private double lengthSeconds;
    private int audioBytesLength;

    protected AudioTrack(String path){
        this.PATH = path;

        try{
            songBytes = Files.readAllBytes(Paths.get(path));
            boolean wasConvertedFromMp3 = false;
            if(audioFileFormat(PATH).equals("mp3") || audioFileFormat(PATH).equals("mp4")){
                if(CONVERSION_ENABLED){
                    System.out.println("-Attempting mp3 conversion-");
                    convertStreamToRawAudio();
                    wasConvertedFromMp3 = true;
                }
            }
            if(!wasConvertedFromMp3){
                audioStream = AudioSystem.getAudioInputStream(new File(path));
            }
            target48Hz();
        }catch (UnsupportedAudioFileException uafExc){
            System.err.println("Unsupported file format");

        }catch(IOException ioExc){
            System.err.println("IO error/Path doesn't exist");
            return;
        }

        audioBytesLength = songBytes.length;
        initAudio();
        displayClipInfo();
    }

    private void convertStreamToRawAudio(){
        //audioFileLength / (frameSize * frameRate)
        MP3Decoder mp3Decoder;
        MP3Sound mp3Sound;
        try{
            mp3Sound = new MP3Sound(PATH);
            mp3Decoder = new MP3Decoder(PATH);
        }catch (IOException ioException){
            ioException.printStackTrace();
            return;
        }

        int decodedSamples = mp3Decoder.decodeEntireMP3();
        System.out.println("Decoded samples: " + decodedSamples);
        System.out.println("ByteArrInputStream size: " + mp3Decoder.getByteArrayOutputStream().size());
        songBytes = mp3Decoder.getStreamBytes();

        audioStream = new AudioInputStream(mp3Decoder.getByteArrInStream(), mp3Sound.getSound().getAudioFormat(), decodedSamples/2);
    }

    private void initAudio(){
        audioInfo = audioStream.getFormat();
        isBigEndian = audioInfo.isBigEndian();
        lengthSeconds = audioStream.getFrameLength() / audioInfo.getFrameRate();
        fragmentsOf20Ms = lengthSeconds * 1000 / MILLIS_20;
        length = (int) ((double) audioBytesLength / fragmentsOf20Ms);
        fragmentsOf20Ms = (int) fragmentsOf20Ms;
        if (length % 2 == 1){
            length--;
            System.out.println("Made default buffer length even: " + length);
        }
        targetBigEndianness();
    }

    //inputStream = new AudioInputStream(new ByteArrayInputStream(new byte[]{}), AudioTrack.TARGET_FORMAT, length);
    private void target48Hz() throws IOException{
        if(CONVERSION_ENABLED){
            AudioFormat sourceFormat = audioStream.getFormat();
            float currentSampleRate = sourceFormat.getSampleRate();
            if(currentSampleRate<TARGET_SAMPLE_RATE-SAMPLE_RATE_OFFSET || currentSampleRate>TARGET_SAMPLE_RATE+SAMPLE_RATE_OFFSET){
                System.out.println("-Targeting 48KHz-");
                //AudioFormat resampledAudioFormat = new AudioFormat(TARGET_SAMPLE_RATE, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), true, true);
                boolean isSupported = AudioSystem.isConversionSupported(TARGET_FORMAT, sourceFormat);
                if(!isSupported){
                    System.err.println("-Unable to convert-");
                    return;
                }

                System.out.println("Frame length before conversion to 48Khz: " + audioStream.getFrameLength());
                audioStream = AudioSystem.getAudioInputStream(TARGET_FORMAT, audioStream);
                AudioFormat convertedFormat = audioStream.getFormat();

                long newFrameLengthOfAudioStream = audioStream.getFrameLength();
                if(newFrameLengthOfAudioStream <= 1){
                    System.out.println("Frame length: " + newFrameLengthOfAudioStream + ", stream was not released? make sure you have tritonus library installed");
                }
                int size = (int) (newFrameLengthOfAudioStream* convertedFormat.getFrameSize());

                System.out.println("New audioStream frame_length*frame_size = " + size);
                //lengthSeconds = audioStream.getFrameLength() / convertedFormat.getFrameRate();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream(size);
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

    public void displayClipInfo(){
        System.out.println(audioInfo + ", " + audioInfo.getChannels() + " channels, " + audioFileFormat(PATH) + " format");
        System.out.printf("Length(seconds): %.2f \n", lengthSeconds);
        System.out.println("Number of 20ms parts " + fragmentsOf20Ms);
        System.out.println("How many arr parts: " + length);
        System.out.println("Byte array size (audio file size): " + songBytes.length + " in MBs " + String.format("%.2f", (double) songBytes.length / 1048576L));
        System.out.println("Size of raw audio in input stream: " + audioStream.getFrameLength() * audioStream.getFormat().getFrameSize());
    }
    //flips endianness
    private void targetBigEndianness(){
        //getAudioInputStream() conversion may turn it big endian even if little endian was requested, which would lead to distorted audio,
        if(!isBigEndian){
            //modifies buffer's underlying array
            for (int i = WAVE_OFFSET, arrLen = songBytes.length -1; i < arrLen; i+=2){
                byte temp = songBytes[i];
                songBytes[i] = songBytes[i+1];
                songBytes[i+1] = temp;
            }
            isBigEndian = true;
            System.out.println("Each two consequent bytes were swapped to maintain big endian order");
        }
    }

    private String audioFileFormat(String path){
        int index = path.lastIndexOf('.') + 1;
        if(index<1) return "";
        return path.substring(index);
    }
    protected byte[] getSongBytes(){
        return songBytes;
    }
    protected int getBaseLength(){
        return length;
    }
    protected double fragmentsOf20Ms(){
        return fragmentsOf20Ms;
    }
    protected boolean isBigEndian(){
        return isBigEndian;
    }
    /*protected int getOffset(){
        return offset;
    }*/
}
