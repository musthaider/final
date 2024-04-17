abstract class Event {

    private String description = "";
    
    public String getDescription() {
		return description;
	}

    public void setDescription(String description) {
		this.description = description;
	}

    abstract void trigger() throws Item.NoItemException;
	
}
