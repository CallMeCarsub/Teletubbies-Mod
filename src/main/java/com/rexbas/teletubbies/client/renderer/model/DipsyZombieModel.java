package com.rexbas.teletubbies.client.renderer.model;

import com.rexbas.teletubbies.entity.monster.DipsyZombieEntity;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DipsyZombieModel extends TeletubbyZombieModel<DipsyZombieEntity> {
	public ModelRenderer stick;

	public DipsyZombieModel() {
		stick = new ModelRenderer(this);
		stick.setPos(0.0F, 24.0F, 0.0F);
		stick.addBox(null, -1.0F, -38.0F, -1.0F, 2, 6, 2, 0.0F, 27, 20);
		
		head.addChild(stick);
	}
}
