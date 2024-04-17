public class ScoreEvent extends Event {
    
    private int points;

    public ScoreEvent(int points) {
		this.points = points;
	}

    @Override
	void trigger() {
		int currentScore = GameState.instance().getPlayerScore();
		int newScore = currentScore + points;
		GameState.instance().setPlayerScore(newScore);
	}

}
