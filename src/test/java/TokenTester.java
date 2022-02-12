import org.junit.Test;
import bot.utilities.PseudoBotTokenGenerator;

public class TokenTester{
    @Test
    public void test1(){
        String token = PseudoBotTokenGenerator.generateBotToken();
        System.out.println(token);
    }
    @Test
    public void test2(){
        String token = PseudoBotTokenGenerator.generateBotToken();
        System.out.println(token);
    }
}
