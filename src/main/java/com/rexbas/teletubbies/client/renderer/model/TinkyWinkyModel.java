package com.rexbas.teletubbies.client.renderer.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TinkyWinkyModel<T extends PathfinderMob> extends TeletubbyModel<T> {
	public ModelPart stick;

	public TinkyWinkyModel(ModelPart part, boolean isZombie) {
		super(part, isZombie);
		stick = part.getChild("head").getChild("stick");
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = TeletubbyModel.createMesh();
		PartDefinition def = mesh.getRoot();
		
		def.getChild("head").addOrReplaceChild("stick", CubeListBuilder.create()
				.addBox(null, -1.0F, -34.0F, -1.0F, 2, 2, 2, 27, 20)
				.addBox(null, -2.0F, -35.0F, -1.0F, 4, 1, 2, 27, 24)
				.addBox(null, 1.0F, -36.0F, -1.0F, 2, 1, 2, 27, 27)
				.addBox(null, -3.0F, -36.0F, -1.0F, 2, 1, 2, 35, 20)
				.addBox(null, -3.0F, -37.0F, -1.0F, 1, 1, 2, 37, 23)
				.addBox(null, 2.0F, -37.0F, -1.0F, 1, 1, 2, 37, 26)
				.addBox(null, -3.0F, -38.0F, -1.0F, 6, 1, 2, 27, 27),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}