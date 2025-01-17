package com.rexbas.teletubbies.entity.monster;

import com.rexbas.teletubbies.init.TeletubbiesItems;
import com.rexbas.teletubbies.inventory.container.handler.TinkyWinkyBagItemHandler;
import com.rexbas.teletubbies.item.TinkyWinkyBagItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.server.ServerLifecycleHooks;

public class TinkyWinkyZombieEntity extends TeletubbyZombieEntity {

	public TinkyWinkyZombieEntity(EntityType<? extends Zombie> type, Level world) {
		super(type, world);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(random, difficulty);
		int i = this.random.nextInt(10);
		switch (i) {
		case 0:
			ItemStack stack = new ItemStack(TeletubbiesItems.TINKYWINKY_BIB.get());
			int damage = this.random.nextInt(stack.getMaxDamage() - 5 + 1) + 5;
			stack.setDamageValue(damage);
			this.setItemSlot(EquipmentSlot.CHEST, stack);
			break;
		case 1:
			ItemStack bag = new ItemStack(TeletubbiesItems.TINKYWINKY_BAG.get());
			TinkyWinkyBagItemHandler handler = (TinkyWinkyBagItemHandler) bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);

			LootParams lootParams = new LootParams.Builder((ServerLevel) level())
					.withParameter(LootContextParams.ORIGIN, this.position())
					.withParameter(LootContextParams.THIS_ENTITY, this)
					.create(LootContextParamSets.GIFT);

			LootTable table = ServerLifecycleHooks.getCurrentServer().getLootData().getLootTable(TinkyWinkyBagItem.LOOTTABLE);
			handler.fillInventory(table, lootParams);

			this.setItemSlot(EquipmentSlot.MAINHAND, bag);
			break;
		}
	}
}