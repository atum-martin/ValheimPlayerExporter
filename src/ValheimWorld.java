

public class ValheimWorld {

	private long worldId;
	private boolean hasCustomSpawnPoint, hasLogoutPoint, hasDeathPoint;
	private ValheimPos spawnPoint, logoutPoint, deathPoint, homePoint;
	
	private transient byte[] mapData = null;
	
	

	public ValheimWorld(long worldId) {
		this.setWorldId(worldId);
	}


	public boolean isHasCustomSpawnPoint() {
		return hasCustomSpawnPoint;
	}


	public void setHasCustomSpawnPoint(boolean hasCustomSpawnPoint) {
		this.hasCustomSpawnPoint = hasCustomSpawnPoint;
	}


	public ValheimPos getSpawnPoint() {
		return spawnPoint;
	}


	public void setSpawnPoint(ValheimPos spawnPoint) {
		this.spawnPoint = spawnPoint;
	}


	public ValheimPos getLogoutPoint() {
		return logoutPoint;
	}


	public void setLogoutPoint(ValheimPos logoutPoint) {
		this.logoutPoint = logoutPoint;
	}


	public ValheimPos getDeathPoint() {
		return deathPoint;
	}


	public void setDeathPoint(ValheimPos deathPoint) {
		this.deathPoint = deathPoint;
	}


	public ValheimPos getHomePoint() {
		return homePoint;
	}


	public void setHomePoint(ValheimPos homePoint) {
		this.homePoint = homePoint;
	}


	public boolean isHasLogoutPoint() {
		return hasLogoutPoint;
	}


	public void setHasLogoutPoint(boolean hasLogoutPoint) {
		this.hasLogoutPoint = hasLogoutPoint;
	}


	public boolean isHasDeathPoint() {
		return hasDeathPoint;
	}


	public void setHasDeathPoint(boolean hasDeathPoint) {
		this.hasDeathPoint = hasDeathPoint;
	}


	public void setMapData(byte[] readData) {
		mapData = readData;
		
	}


	public byte[] getMapData() {
		return mapData;
	}


	public long getWorldId() {
		return worldId;
	}


	public void setWorldId(long worldId) {
		this.worldId = worldId;
	}

}
