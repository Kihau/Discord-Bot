package utilities;

import deskort.MessageProcessor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

//ffmpeg
public class Converter{
    static Runtime run = Runtime.getRuntime();
    static float TARGET_SAMPLE_RATE = 48000f;
    static String outputPath = "";
    static int extensionInt;
    static String extensionString;
    final static int SHORTEST_AUDIO_FILE_LENGTH = 5;
    final static int ELITE_NUMBER = 1337;
    final static int WAVE = 0;
    final static int SND = 1;
    final static int AIFF = 2;
    final static int AIFC = 3;
    /**
     * @param
     * @return true if succeeds
     */
    static String givenPath = "";
    public static boolean convertMP3ToSND(String fileToConvertPath, String outputPath, int format){
        extensionInt = format;
        getExtension();
        try{
            run.exec("ffmpeg -i " + fileToConvertPath + " -ar " + TARGET_SAMPLE_RATE + " " + outputPath);
        }catch (IOException ioException){
            ioException.printStackTrace();
            System.err.println("Failed to execute conversion");
            return false;
        }
        return true;
    }

    private static void getExtension(){
        switch (extensionInt){
            case WAVE:
                extensionString = "wav";
                break;
            case SND:
                extensionString = "snd";
                break;
            case AIFF:
                extensionString = "aiff";
                break;
            case AIFC:
                extensionString = "aifc";
                break;
        }
    }

    public static boolean convert(String fileToConvertPath, int format){
        extensionInt = format;
        getExtension();
        if(extensionString.equals("")) return false;

        givenPath = fileToConvertPath;
        int fileIndex = getFileIndex();
        if(fileIndex < 0){
            return false;
        }
        StringBuilder newFileName = new StringBuilder(fileToConvertPath.substring(fileIndex));
        int dotIndex = getDotIndex(newFileName);
        if(dotIndex < 0){
            return false;
        }
        int begin = dotIndex+1;
        int end = newFileName.length();
        newFileName.replace(begin, end, extensionString);

        try{
  /*          String secondMYSQLCommand = "-u frisk -p";
            String[] commandsMYSQL = {"powershell", " start F:\\Program Files (x86)\\ffmpeg-master-latest-win64-gpl-shared\\bin\\ffmpeg.exe", secondMYSQLCommand};
            Process proc = run.exec(commandsMYSQL);
*/          String inputArgument = " -i " + fileToConvertPath;
            String sampleRateArgument = " -ar " + (int)(TARGET_SAMPLE_RATE);
            String channelsArgument = "-ac 2";
            String outputArgument =  "\"q" + newFileName + "\"";
            String [] arguments = {outputArgument," ", sampleRateArgument, " " , inputArgument};
            String[] commandsFFMPEG = {"ffmpeg", argsToOneString(arguments)};
            ProcessBuilder procBuilder = new ProcessBuilder(commandsFFMPEG);
            procBuilder.redirectErrorStream(true);
            procBuilder.directory(new File("F:\\Program Files (x86)\\ffmpeg-master-latest-win64-gpl-shared\\bin"));
            System.out.println("Working directory: " + procBuilder.directory().getAbsolutePath());
            Process proc = procBuilder.start();
            sleep(500);

            InputStream in = proc.getInputStream();
            final Scanner scanner = new Scanner(in);
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("scanner stream");
                    while (scanner.hasNextLine()) {
                        System.out.println(scanner.nextLine());
                    }
                    scanner.close();
                }
            }).start();

            if (proc == null){
                System.err.println("Process is null");
                return false;
            }
            InputStream inputStream = proc.getInputStream();
            //InputStream errorStream = procBuilder.getErrorStream();
            String inputStr = MessageProcessor.streamToString(50_000, inputStream);
            String anyStr = errorStreamToString(50_000, proc.getErrorStream());
            if (inputStr != null){
                System.out.println(inputStr);
            }
            if (anyStr != null){
                System.out.println(inputStr);
            }
            System.out.println("Finished request");
        }catch (IOException ioException){
            ioException.printStackTrace();
            System.err.println("Failed to execute conversion");
            return false;
        }
        return true;
    }

    private static String argsToOneString(String[] arguments){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<arguments.length; i++){
            sb.append(arguments[i]);
        }
        return sb.toString();
    }

    private static String errorStreamToString(int size, InputStream inputStream){
        String output;
        byte [] buffer = new byte[size];
        try {
            int offset = 0;
            while (inputStream.available() != 0) {
                int available = inputStream.available();
                offset+=inputStream.read(buffer,offset,available);
            }
            output = bytesToStr(buffer,offset);
            return output;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("ioe at read");
        }
        return null;
    }
    private static String bytesToStr(byte [] bytes, int offset){
        char [] charArr = new char[offset];
        for(int i = 0; i<offset; i++){
            charArr[i] = (char)bytes[i];
        }
        return new String(charArr);
    }

    private static void sleep(int time){
        try{
            Thread.sleep(time);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private static int getDotIndex(StringBuilder sb){
        return sb.lastIndexOf(".");
    }

    private static int getFileIndex(){
        int lastSlash = givenPath.lastIndexOf('\\');
        if(lastSlash == -1){
            lastSlash = givenPath.lastIndexOf('/');
        }
        if(lastSlash == -1 || givenPath.length() - lastSlash < SHORTEST_AUDIO_FILE_LENGTH){
            return -1;
        }
        return lastSlash + 1;
    }

    public static void main(String[] args){
        String inputPath = "\"C:\\Users\\Frisk\\Desktop\\audio\\insomnia.mp3\"";
        boolean resultSample = Converter.convert(inputPath, WAVE);
        System.out.println(resultSample);

        /*boolean resultSND = Converter.convertMP3ToSND(inputPath,Converter.WAVE);
        System.out.println(resultSND);*/
    }

}
