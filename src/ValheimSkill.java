
public class ValheimSkill {
	private int skillName;
	private float level, data;
	
	public ValheimSkill(int skillName, float level, float data) {
		this.setSkillName(skillName);
		this.setLevel(level);
		this.setData(data);
	}
	public int getSkillName() {
		return skillName;
	}
	public void setSkillName(int skillName) {
		this.skillName = skillName;
	}
	public float getLevel() {
		return level;
	}
	public void setLevel(float level) {
		this.level = level;
	}
	public float getData() {
		return data;
	}
	public void setData(float data) {
		this.data = data;
	}

}
