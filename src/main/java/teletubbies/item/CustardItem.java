package teletubbies.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;
import teletubbies.Teletubbies;
import teletubbies.config.FoodConfig;
import teletubbies.init.ModItems;

public class CustardItem extends Item {
	
	private static final Food CUSTARD_FOOD = new Food.Builder()
			.hunger(FoodConfig.CUSTARD_HUNGER.get())
			.saturation(FoodConfig.CUSTARD_SATURATION.get().floatValue())
			.build();
	
	public CustardItem() {
		super(new Item.Properties()
				.food(CUSTARD_FOOD)
				.maxStackSize(1)
				.group(Teletubbies.ITEMGROUP));
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
		super.onItemUseFinish(stack, world, entity);
		return new ItemStack(ModItems.BOWL.get());
	}
}
