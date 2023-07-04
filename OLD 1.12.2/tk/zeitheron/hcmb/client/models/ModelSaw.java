package tk.zeitheron.hcmb.client.models;

import com.zeitheron.hammercore.client.model.ModelSimple;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelSaw
		extends ModelSimple<ItemStack>
{
	public ModelRenderer shape1;
	public ModelRenderer shape2;
	public ModelRenderer shape2_1;
	public ModelRenderer shape2_2;
	public ModelRenderer shape2_3;
	public ModelRenderer shape2_4;
	public ModelRenderer shape9;

	public ModelSaw()
	{
		super(64, 32, (ResourceLocation) null);
		this.shape1 = new ModelRenderer(this, 0, 0);
		this.shape1.setRotationPoint(-5.02f, 0.0f, 0.05f);
		this.shape1.addBox(0.0f, 0.0f, 0.0f, 12, 1, 1, 0.0f);
		this.shape2_4 = new ModelRenderer(this, 42, 0);
		this.shape2_4.setRotationPoint(9.8f, -0.7f, 0.0f);
		this.shape2_4.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.0f);
		this.setRotateAngle(this.shape2_4, 0.0f, 0.0f, -0.03769911f);
		this.shape9 = new ModelRenderer(this, 0, 2);
		this.shape9.setRotationPoint(-4.8f, 2.4f, 0.4f);
		this.shape9.addBox(0.0f, 0.0f, 0.0f, 11, 1, 1, 0.0f);
		this.shape2_1 = new ModelRenderer(this, 30, 0);
		this.shape2_1.setRotationPoint(6.0f, -0.4f, 0.0f);
		this.shape2_1.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.0f);
		this.setRotateAngle(this.shape2_1, 0.0f, 0.0f, -0.045553092f);
		this.shape2_2 = new ModelRenderer(this, 34, 0);
		this.shape2_2.setRotationPoint(9.9f, -0.7f, 0.0f);
		this.shape2_2.addBox(0.0f, 0.0f, 0.0f, 1, 3, 1, 0.0f);
		this.setRotateAngle(this.shape2_2, 0.0f, 0.0f, 1.4915584f);
		this.shape2 = new ModelRenderer(this, 26, 0);
		this.shape2.setRotationPoint(-5.5f, 0.6f, 0.0f);
		this.shape2.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.0f);
		this.shape2_3 = new ModelRenderer(this, 38, 0);
		this.shape2_3.setRotationPoint(10.0f, 2.39f, 0.0f);
		this.shape2_3.addBox(0.0f, 0.0f, 0.0f, 1, 3, 1, 0.0f);
		this.setRotateAngle(this.shape2_3, 0.0f, 0.0f, 1.4915584f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.shape1.offsetX, this.shape1.offsetY, this.shape1.offsetZ);
		GlStateManager.translate(this.shape1.rotationPointX * f5, this.shape1.rotationPointY * f5, this.shape1.rotationPointZ * f5);
		GlStateManager.scale(1.0, 0.9, 0.9);
		GlStateManager.translate(-this.shape1.offsetX, -this.shape1.offsetY, -this.shape1.offsetZ);
		GlStateManager.translate(-this.shape1.rotationPointX * f5, -this.shape1.rotationPointY * f5, -this.shape1.rotationPointZ * f5);
		this.shape1.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.shape2_4.offsetX, this.shape2_4.offsetY, this.shape2_4.offsetZ);
		GlStateManager.translate(this.shape2_4.rotationPointX * f5, this.shape2_4.rotationPointY * f5, this.shape2_4.rotationPointZ * f5);
		GlStateManager.scale(1.0, 2.05, 1.0);
		GlStateManager.translate(-this.shape2_4.offsetX, -this.shape2_4.offsetY, -this.shape2_4.offsetZ);
		GlStateManager.translate(-this.shape2_4.rotationPointX * f5, -this.shape2_4.rotationPointY * f5, -this.shape2_4.rotationPointZ * f5);
		this.shape2_4.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.shape9.offsetX, this.shape9.offsetY, this.shape9.offsetZ);
		GlStateManager.translate(this.shape9.rotationPointX * f5, this.shape9.rotationPointY * f5, this.shape9.rotationPointZ * f5);
		GlStateManager.scale(1.0, 1.0, 0.1);
		GlStateManager.translate(-this.shape9.offsetX, -this.shape9.offsetY, -this.shape9.offsetZ);
		GlStateManager.translate(-this.shape9.rotationPointX * f5, -this.shape9.rotationPointY * f5, -this.shape9.rotationPointZ * f5);
		this.shape9.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.shape2_1.offsetX, this.shape2_1.offsetY, this.shape2_1.offsetZ);
		GlStateManager.translate(this.shape2_1.rotationPointX * f5, this.shape2_1.rotationPointY * f5, this.shape2_1.rotationPointZ * f5);
		GlStateManager.scale(1.0, 2.05, 1.0);
		GlStateManager.translate(-this.shape2_1.offsetX, -this.shape2_1.offsetY, -this.shape2_1.offsetZ);
		GlStateManager.translate(-this.shape2_1.rotationPointX * f5, -this.shape2_1.rotationPointY * f5, -this.shape2_1.rotationPointZ * f5);
		this.shape2_1.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.shape2_2.offsetX, this.shape2_2.offsetY, this.shape2_2.offsetZ);
		GlStateManager.translate(this.shape2_2.rotationPointX * f5, this.shape2_2.rotationPointY * f5, this.shape2_2.rotationPointZ * f5);
		GlStateManager.scale(1.0, 1.0, 1.1);
		GlStateManager.translate(-this.shape2_2.offsetX, -this.shape2_2.offsetY, -this.shape2_2.offsetZ);
		GlStateManager.translate(-this.shape2_2.rotationPointX * f5, -this.shape2_2.rotationPointY * f5, -this.shape2_2.rotationPointZ * f5);
		this.shape2_2.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.shape2.offsetX, this.shape2.offsetY, this.shape2.offsetZ);
		GlStateManager.translate(this.shape2.rotationPointX * f5, this.shape2.rotationPointY * f5, this.shape2.rotationPointZ * f5);
		GlStateManager.scale(1.0, 1.45, 1.0);
		GlStateManager.translate(-this.shape2.offsetX, -this.shape2.offsetY, -this.shape2.offsetZ);
		GlStateManager.translate(-this.shape2.rotationPointX * f5, -this.shape2.rotationPointY * f5, -this.shape2.rotationPointZ * f5);
		this.shape2.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.shape2_3.offsetX, this.shape2_3.offsetY, this.shape2_3.offsetZ);
		GlStateManager.translate(this.shape2_3.rotationPointX * f5, this.shape2_3.rotationPointY * f5, this.shape2_3.rotationPointZ * f5);
		GlStateManager.scale(1.0, 1.0, 1.1);
		GlStateManager.translate(-this.shape2_3.offsetX, -this.shape2_3.offsetY, -this.shape2_3.offsetZ);
		GlStateManager.translate(-this.shape2_3.rotationPointX * f5, -this.shape2_3.rotationPointY * f5, -this.shape2_3.rotationPointZ * f5);
		this.shape2_3.render(f5);
		GlStateManager.popMatrix();
	}
}