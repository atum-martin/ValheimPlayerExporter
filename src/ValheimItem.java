
public class ValheimItem {
	
	private String name, crafterName;
	private int stack, quality, variant;
	private long crafterId;
	private ValheimItemPos pos;
	private float durability;
	private boolean equipped;

	public ValheimItem(String name, int stack, float durability, ValheimItemPos pos, boolean equipped, int quality, int variant, long crafterId, String crafterName) {
		this.name = name;
		this.stack = stack;
		this.durability = durability;
		this.pos = pos;
		this.equipped = equipped;
		this.quality = quality;
		this.variant = variant;
		this.crafterId = crafterId;
		this.crafterName = crafterName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCrafterName() {
		return crafterName;
	}

	public void setCrafterName(String crafterName) {
		this.crafterName = crafterName;
	}

	public int getStack() {
		return stack;
	}

	public void setStack(int stack) {
		this.stack = stack;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getVariant() {
		return variant;
	}

	public void setVariant(int variant) {
		this.variant = variant;
	}

	public long getCrafterId() {
		return crafterId;
	}

	public void setCrafterId(long crafterId) {
		this.crafterId = crafterId;
	}

	public ValheimItemPos getPos() {
		return pos;
	}

	public void setPos(ValheimItemPos pos) {
		this.pos = pos;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

	public float getDurability() {
		return durability;
	}

	public void setDurability(float durability) {
		this.durability = durability;
	}

}
