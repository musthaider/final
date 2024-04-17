import java.io.*;

public class WinEvent extends Event {


    private String winMessage = "You won, game saved to ";
   
    public WinEvent() {
    }

    @Override
    void trigger() {
        GameState instance = GameState.instance();
        try {
            instance.store(GameState.DEFAULT_SAVE_FILE);
        } catch (IOException e) {
            System.err.println("Error saving the game: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println(winMessage + GameState.DEFAULT_SAVE_FILE + ".sav");
    }
}
