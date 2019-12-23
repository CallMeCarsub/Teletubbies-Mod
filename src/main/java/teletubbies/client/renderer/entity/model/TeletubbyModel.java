package teletubbies.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.CreatureEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TeletubbyModel<T extends CreatureEntity> extends BipedModel<T> {
	public RendererModel leftEar;
	public RendererModel rightEar;
	
	public TeletubbyModel() {
		leftEar = new RendererModel(this);
		leftEar.setRotationPoint(-6.8333F, 19.5F, -0.3333F);
		leftEar.cubeList.add(new ModelBox(leftEar, 56, 21, 1.8333F, -26.5F, 0.3333F, 1, 3, 1, 0.0F, false));
		leftEar.cubeList.add(new ModelBox(leftEar, 56, 25, 1.8333F, -25.5F, -1.6667F, 1, 3, 2, 0.0F, false));
		leftEar.cubeList.add(new ModelBox(leftEar, 56, 30, 1.8333F, -26.5F, -0.6667F, 1, 1, 1, 0.0F, false));

		rightEar = new RendererModel(this);
		rightEar.setRotationPoint(6.8333F, 19.5F, -0.3333F);
		rightEar.cubeList.add(new ModelBox(rightEar, 56, 21, -2.8333F, -26.5F, 0.3333F, 1, 3, 1, 0.0F, false));
		rightEar.cubeList.add(new ModelBox(rightEar, 56, 25, -2.8333F, -25.5F, -1.6667F, 1, 3, 2, 0.0F, false));
		rightEar.cubeList.add(new ModelBox(rightEar, 56, 30, -2.8333F, -26.5F, -0.6667F, 1, 1, 1, 0.0F, false));
		
		this.bipedHead.addChild(leftEar);
		this.bipedHead.addChild(rightEar);
	}
}
