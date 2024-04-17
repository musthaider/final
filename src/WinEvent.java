import java.io.*;

public class WinEvent extends Event {


    private String winMessage = "You won, game saved to ";
   
    public WinEvent() {
    }

    @Override
    void trigger() {
        GameState instance = GameState.instance();
        try {
            instance.store(instance.DEFAULT_SAVE_FILE);
        } catch (IOException e) {}
        System.out.println(winMessage + instance.DEFAULT_SAVE_FILE);
    }
}
