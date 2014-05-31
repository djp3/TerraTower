package edu.uci.ics.luci.TerraTower;

public class TTEventCreateWorld extends TTEvent {

	private String name;
	private String password;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public TTEventCreateWorld(String worldName, String password){
		super(TTEvent.TTEventType.CREATE_WORLD);
		
		this.setName(worldName);
		this.setPassword(password);
	}
	
	void set(TTEventCreateWorld ttEvent){
		super.set(ttEvent);
		
		this.setName(ttEvent.getName());
		this.setPassword(ttEvent.getPassword());
	}
}
