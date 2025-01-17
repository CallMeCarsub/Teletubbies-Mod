package com.rexbas.teletubbies;

import com.rexbas.teletubbies.client.gui.screen.inventory.ControlPanelScreen;
import com.rexbas.teletubbies.client.gui.screen.inventory.CustardMachineScreen;
import com.rexbas.teletubbies.client.gui.screen.inventory.TinkyWinkyBagScreen;
import com.rexbas.teletubbies.client.gui.screen.inventory.ToastMachineScreen;
import com.rexbas.teletubbies.config.Config;
import com.rexbas.teletubbies.init.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Teletubbies.MODID)
public class Teletubbies {
    public static final String MODID = "teletubbies";
	
	public Teletubbies() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);		
		
		TeletubbiesBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TeletubbiesBlocks.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		TeletubbiesEntityTypes.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		TeletubbiesItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TeletubbiesItems.CREATIVE_TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TeletubbiesSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TeletubbiesWorldGen.STRUCTURE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		TeletubbiesWorldGen.FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
		TeletubbiesContainers.CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

		Config.loadConfig(Config.COMMON_SPEC, FMLPaths.CONFIGDIR.get().resolve("teletubbies-common.toml").toString());
		Config.loadConfig(Config.CLIENT_SPEC, FMLPaths.CONFIGDIR.get().resolve("teletubbies-client.toml").toString());
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(TeletubbiesEntityTypes::registerPlacement);
	}

	private void setupClient(final FMLClientSetupEvent event) {
		MenuScreens.register(TeletubbiesContainers.TINKYWINKY_BAG_CONTAINER.get(), TinkyWinkyBagScreen::new);
		MenuScreens.register(TeletubbiesContainers.CONTROL_PANEL_CONTAINER.get(), ControlPanelScreen::new);
		MenuScreens.register(TeletubbiesContainers.TOAST_MACHINE_CONTAINER.get(), ToastMachineScreen::new);
		MenuScreens.register(TeletubbiesContainers.CUSTARD_MACHINE_CONTAINER.get(), CustardMachineScreen::new);
	}
}