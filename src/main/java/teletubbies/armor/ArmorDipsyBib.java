package teletubbies.armor;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import teletubbies.Teletubbies;

public class ArmorDipsyBib extends ItemArmor {

	private final String name = "dipsybib";

	public ArmorDipsyBib(ArmorMaterial material, int renderId, EntityEquipmentSlot slot) {
		super(material, renderId, slot);
		this.setCreativeTab(Teletubbies.tabTeletubbies);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setMaxStackSize(1);
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if(stack.getItem() == Teletubbies.dipsyBib) {
        	return "teletubbies:textures/armor/dipsybib.png";
        }
        else {
        	return null;
        }
    }
	
    @Override
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {
    	return stack2.isItemEqual(new ItemStack(Teletubbies.dipsyStick)) || super.getIsRepairable(stack1, stack2);
    }
}