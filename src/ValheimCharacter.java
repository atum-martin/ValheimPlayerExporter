import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;

public class ValheimCharacter {
	
	private int version, dataVersion, inventoryVersion, skillsVersion;
	private int kills, gender;
	private int deaths, crafts, builds, numberOfWorlds;
	public List<ValheimWorld> worlds = new LinkedList<ValheimWorld>();
	private String name, startSeed, guardianPower, beard, hair;
	private float maxHp, Hp, Stamina, TimeSinceDeath, GuardianPowerCooldown;
	private boolean isFirstSpawn;
	private long Id;
	private byte[] extraData;
	private ValheimPos skinColor, hairColor;
	public List<ValheimItem> inventory = new LinkedList<ValheimItem>();
	
	public List<ValheimFood> food = new LinkedList<ValheimFood>();
	public List<ValheimSkill> skills = new LinkedList<ValheimSkill>();
	

	public List<String> recipes = new LinkedList<String>();
	public List<String> materials = new LinkedList<String>();
	public List<String> tutorials = new LinkedList<String>();
	public List<String> uniques = new LinkedList<String>();
	public List<String> trophies = new LinkedList<String>();
	public List<Integer> biomes = new LinkedList<Integer>();

	
	public List<AbstractMap.SimpleEntry<String, Integer>> stations = new LinkedList<AbstractMap.SimpleEntry<String, Integer>>();
	public List<AbstractMap.SimpleEntry<String, String>> texts = new LinkedList<AbstractMap.SimpleEntry<String, String>>();
	
	public String toString() {
		return name+" "+kills+" "+deaths;
	}
	
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		System.out.println("kills"+kills);
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		System.out.println("deaths"+deaths);
		this.deaths = deaths;
	}

	public int getCrafts() {
		return crafts;
	}

	public void setCrafts(int crafts) {
		this.crafts = crafts;
	}

	public int getBuilds() {
		return builds;
	}

	public void setBuilds(int builds) {
		this.builds = builds;
	}

	public int getNumberOfWorlds() {
		return numberOfWorlds;
	}

	public void setNumberOfWorlds(int numberOfWorlds) {
		this.numberOfWorlds = numberOfWorlds;
	}

	public void addWorld(ValheimWorld world) {
		worlds.add(world);
		
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public boolean isFirstSpawn() {
		return isFirstSpawn;
	}

	public void setFirstSpawn(boolean isFirstSpawn) {
		this.isFirstSpawn = isFirstSpawn;
	}

	public float getGuardianPowerCooldown() {
		return GuardianPowerCooldown;
	}

	public void setGuardianPowerCooldown(float guardianPowerCooldown) {
		GuardianPowerCooldown = guardianPowerCooldown;
	}

	public float getTimeSinceDeath() {
		return TimeSinceDeath;
	}

	public void setTimeSinceDeath(float timeSinceDeath) {
		TimeSinceDeath = timeSinceDeath;
	}

	public float getStamina() {
		return Stamina;
	}

	public void setStamina(float stamina) {
		Stamina = stamina;
	}

	public float getHp() {
		return Hp;
	}

	public void setHp(float hp) {
		Hp = hp;
	}

	public float getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(float maxHp) {
		this.maxHp = maxHp;
	}

	public int getInventoryVersion() {
		return inventoryVersion;
	}

	public void setInventoryVersion(int inventoryVersion) {
		this.inventoryVersion = inventoryVersion;
	}

	public int getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(int dataVersion) {
		this.dataVersion = dataVersion;
	}

	public String getStartSeed() {
		return startSeed;
	}

	public void setStartSeed(String startSeed) {
		this.startSeed = startSeed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGuardianPower() {
		return guardianPower;
	}

	public void setGuardianPower(String guardianPower) {
		this.guardianPower = guardianPower;
	}

	public void addToInventory(ValheimItem item) {
		inventory.add(item);
		
	}

	public String getBeard() {
		return beard;
	}

	public void setBeard(String beard) {
		this.beard = beard;
	}

	public String getHair() {
		return hair;
	}

	public void setHair(String hair) {
		this.hair = hair;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public ValheimPos getSkinColor() {
		return skinColor;
	}

	public void setSkinColor(ValheimPos skinColor) {
		this.skinColor = skinColor;
	}

	public ValheimPos getHairColor() {
		return hairColor;
	}

	public void setHairColor(ValheimPos hairColor) {
		this.hairColor = hairColor;
	}

	public int getSkillsVersion() {
		return skillsVersion;
	}

	public void setSkillsVersion(int skillsVersion) {
		this.skillsVersion = skillsVersion;
	}

	public void addFood(ValheimFood valheimFood) {
		food.add(valheimFood);
		
	}

	public void addSkill(ValheimSkill valheimSkill) {
		skills.add(valheimSkill);
		
	}
	
	public void addRecipe(String readStr) {
		recipes.add(readStr);
	}

	public void addStation(String readStr, int int1) {
		stations.add(new AbstractMap.SimpleEntry<String, Integer>(readStr, int1));
	}

	public void knownMaterials(String readStr) {
		materials.add(readStr);
	}

	public void shownTutorials(String readStr) {
		tutorials.add(readStr);
	}

	public void addUniques(String readStr) {
		uniques.add(readStr);
	}

	public void addTrophies(String readStr) {
		trophies.add(readStr);
	}

	public void addBiome(int int1) {
		biomes.add(int1);
	}

	public void addText(String readStr, String readStr2) {
		texts.add(new AbstractMap.SimpleEntry<String, String>(readStr, readStr2));
	}


	public byte[] getExtraData() {
		return extraData;
	}


	public void setExtraData(byte[] extraData) {
		this.extraData = extraData;
	}
	
	


}
