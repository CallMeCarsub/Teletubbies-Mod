package teletubbies.entity.render;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import teletubbies.entity.model.ModelPo;
import teletubbies.entity.monster.EntityZombiePo;

public class RenderZombiePo extends RenderBiped<EntityZombiePo> {
	
	public RenderZombiePo(RenderManager rendermanager, boolean isZombie, final float scale, float shadowsize) {
		super(rendermanager, new ModelPo(isZombie, scale), shadowsize);
        LayerRenderer<?> layerrenderer = (LayerRenderer)this.layerRenderers.get(0);
        for(int i = 0; i < this.layerRenderers.size(); i++) {
        	LayerRenderer layer = this.layerRenderers.get(i);
        	if(layer instanceof LayerHeldItem) {
        		this.layerRenderers.remove(i);
        	}
        }
        
       LayerHeldItem heldItem = new LayerHeldItem(this) {
        	@Override
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale0) {
        		GlStateManager.pushMatrix();
        		GlStateManager.translate(0.0, 0.15, 0.08);
        		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        		GlStateManager.popMatrix();
        	}

        };
        this.addLayer(heldItem);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
        	protected void initArmor() {
        		this.modelLeggings = new ModelZombie(0.5F, true);
        		this.modelArmor = new ModelZombie(1.0F, true);
        	}
        	
        	@Override
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale0) {
        		GlStateManager.pushMatrix();
        		GlStateManager.translate(0.0, 1.5 - (scale / 0.0625) * 1.5, 0.0);
        		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        		GlStateManager.popMatrix();
        	}
        };
        this.addLayer(layerbipedarmor);
	}
	
	@Override
    public void doRender(EntityZombiePo entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZombiePo entity) {
		return new ResourceLocation("teletubbies:textures/entity/zombiepo.png");
	}
}