import java.util.Scanner;

class HealthCommand extends Command {

    HealthCommand() {}

    String execute() {
        int currentHealth = GameState.instance().getPlayerHealth();
        String healthMessage = "";
   
        if(currentHealth > 75) {
            healthMessage = "You feel as mighty as a dragon, unstoppable and fierce.\nHealth: " + currentHealth;
        }
        else if(currentHealth > 50) {
            healthMessage = "You're in good shape, ready to tackle any dungeon.\nHealth: " + currentHealth;
        }
        else if(currentHealth > 25) {
            healthMessage = "You've seen better days. Time to watch your back and perhaps find a potion.\nHealth: " + currentHealth;
        }
        else {
            healthMessage = "Warning: Grave danger ahead! You're teetering on the edge of the abyss.\nHealth: " + currentHealth;
        }

        return healthMessage + "\n";
    } 
}


