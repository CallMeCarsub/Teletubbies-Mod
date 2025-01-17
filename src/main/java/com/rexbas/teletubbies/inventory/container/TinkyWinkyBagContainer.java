package com.rexbas.teletubbies.inventory.container;

import com.rexbas.teletubbies.init.TeletubbiesContainers;
import com.rexbas.teletubbies.inventory.container.handler.TinkyWinkyBagItemHandler;
import com.rexbas.teletubbies.inventory.container.slot.TinkyWinkyBagSlot;
import com.rexbas.teletubbies.item.TinkyWinkyBagItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

// https://github.com/Flanks255/simplybackpacks/tree/master/src/main/java/com/flanks255/simplybackpacks

public class TinkyWinkyBagContainer extends AbstractContainerMenu {
	public final int numRows = 6;
	private final Inventory playerInventory;
	public ItemStack bag;
	
	public TinkyWinkyBagContainer(final int id, final Inventory playerInventory, FriendlyByteBuf data) {
		this(id, playerInventory,
				playerInventory.player.getMainHandItem().getItem() instanceof TinkyWinkyBagItem
						? playerInventory.player.getMainHandItem()
						: playerInventory.player.getOffhandItem());
    }

	public TinkyWinkyBagContainer(int id, Inventory playerInventory, ItemStack bag) {
		super(TeletubbiesContainers.TINKYWINKY_BAG_CONTAINER.get(), id);
		this.playerInventory = playerInventory;
		this.bag = bag;
		
		TinkyWinkyBagItemHandler handler = (TinkyWinkyBagItemHandler) bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
        handler.load();

		addBagSlots(handler);
		addPlayerSlots();
	}
	
	private void addBagSlots(TinkyWinkyBagItemHandler handler) {
		for (int j = 0; j < this.numRows; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new TinkyWinkyBagSlot(handler, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}
	}
	
	private void addPlayerSlots() {
		int i = (this.numRows - 4) * 18;

		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {		
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			
			IItemHandler handler = bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
					
            ItemStack slotStack = slot.getItem();
			itemstack = slotStack.copy();
			if (index < handler.getSlots()) {
				if (!this.moveItemStackTo(slotStack, handler.getSlots(), this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotStack, 0, handler.getSlots(), false)) {
				return ItemStack.EMPTY;
			}
			
			if (slotStack.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
		}
		return itemstack;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return !bag.isEmpty();
	}

	@Override
	public void clicked(int slot, int dragType, ClickType clickTypeIn, Player player) {
		if (slot >= 0) {
			if (getSlot(slot).getItem().getItem() instanceof TinkyWinkyBagItem)
				return;
		}
		if (clickTypeIn == ClickType.SWAP)
			return;

		if (slot >= 0)
			getSlot(slot).container.setChanged();
		super.clicked(slot, dragType, clickTypeIn, player);
	}
}