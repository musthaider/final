public class WoundEvent extends Event {

	private int damage;
	
	public WoundEvent(int damage) {
		this.damage = damage;
	}

	@Override
	public void trigger() {
		int playerHealth = GameState.instance().getPlayerHealth();
		GameState.instance().setPlayerHealth(playerHealth - damage);
			
	}

}
