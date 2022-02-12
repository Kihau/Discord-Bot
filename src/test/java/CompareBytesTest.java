import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class CompareBytesTest{
    final static int WAVE = 44;
    final static int BASE_LENGTH = 4;
    String [] spaces = {""," ", "  ", "   ", "    ","     "};

    @Test
    public void test026() throws IOException{
        final String PATH026_44 = "C:\\Users\\Frisk\\Desktop\\audio\\026-44.wav";
        final String PATH026_48 = "C:\\Users\\Frisk\\Desktop\\audio\\026-48.wav";
        byte [] bytes44 = Files.readAllBytes(Paths.get(PATH026_44));
        byte [] bytes48 = Files.readAllBytes(Paths.get(PATH026_48));
        System.out.print("44Khz\n");
        for (int i = 0; i < WAVE; i++){
            if(i%4 == 0){
                System.out.print(" | ");
            }
            int byteLength = getLength(bytes44[i]);
            System.out.print(spaces[BASE_LENGTH-byteLength]);
            System.out.print(bytes44[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < WAVE; i++){
            if(i%4 == 0){
                System.out.print(" | ");
            }
            int byteLength = getLength(bytes48[i]);
            System.out.print(spaces[BASE_LENGTH-byteLength]);
            System.out.print(bytes48[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < WAVE; i++){
            if(i%4 == 0){
                System.out.print(" | ");
            }
            int indexLen = getLength(i);
            System.out.print(spaces[BASE_LENGTH-indexLen]);
            System.out.print(i + " ");
        }
        System.out.print("\n48Khz\n");
    }
    private int getLength(byte number){
        if(number == 0) return 1;
        int length = 0;
        if(number<0){
            length++;
            number = (byte) (~number + 1);
        }
        while(number!=0){
            number/=10;
            length++;
        }
        return length;

    }
    private int getLength(int number){
        if(number == 0) return 1;
        int length = 0;
        if(number<0){
            length++;
            number = ~number + 1;
        }
        while(number!=0){
            number/=10;
            length++;
        }
        return length;
    }
    @Test
    public void test2(){

    }
}
