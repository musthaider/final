public class ScoreCommand extends Command {
    private int score;

    ScoreCommand() {
        this.score = GameState.instance().getPlayerScore();
    }

    String execute() {
        String scoreMessage = null;
        String rank = "";

        if (score < 10) {
            rank = "Wayward Wanderer";
        } else if (score >= 10 && score < 30) {
            rank = "Aspiring Adventurer";
        } else if (score >= 30 && score < 60) {
            rank = "Brave Battler";
        } else if (score >= 60 && score < 100) {
            rank = "Hero of the Realms";
        } else if (score >= 100) {
            rank = "Legend of Zork";
        }

        scoreMessage = "You have accumulated " + score + " points. This gives you a rank of " + rank + ".";

        return scoreMessage + "\n";
    }
}

