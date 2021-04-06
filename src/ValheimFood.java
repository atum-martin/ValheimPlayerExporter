
public class ValheimFood {
	
	private String name;
	private float HpLeft, StaminaLeft;
	public ValheimFood(String name, float HpLeft, float StaminaLeft) {
		this.name = name;
		this.HpLeft = HpLeft;
		this.StaminaLeft = StaminaLeft;
	}
	public float getStaminaLeft() {
		return StaminaLeft;
	}
	public void setStaminaLeft(float staminaLeft) {
		StaminaLeft = staminaLeft;
	}
	public float getHpLeft() {
		return HpLeft;
	}
	public void setHpLeft(float hpLeft) {
		HpLeft = hpLeft;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
