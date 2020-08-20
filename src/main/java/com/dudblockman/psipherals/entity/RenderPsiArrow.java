package com.dudblockman.psipherals.entity;

import com.dudblockman.psipherals.Psipherals;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import vazkii.psi.api.internal.PsiRenderHelper;

public class RenderPsiArrow extends EntityRenderer<EntityPsiArrow> {
    public RenderPsiArrow(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityPsiArrow entity) {
        return Psipherals.location("textures/items/psi_arrow.png");
    }

    public void render(EntityPsiArrow p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.push();
        p_225623_4_.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(p_225623_3_, p_225623_1_.prevRotationYaw, p_225623_1_.rotationYaw) - 90.0F));
        p_225623_4_.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(p_225623_3_, p_225623_1_.prevRotationPitch, p_225623_1_.rotationPitch)));
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = 0.15625F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = 0.15625F;
        float f7 = 0.3125F;
        float f8 = 0.05625F;
        int colorVal = p_225623_1_.getColor();
        int r = PsiRenderHelper.r(colorVal);
        int g = PsiRenderHelper.green(colorVal);
        int b = PsiRenderHelper.b(colorVal);
        float f9 = (float)p_225623_1_.arrowShake - p_225623_3_;
        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            p_225623_4_.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(f10));
        }

        p_225623_4_.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(45.0F));
        p_225623_4_.scale(0.05625F, 0.05625F, 0.05625F);
        p_225623_4_.translate(-4.0D, 0.0D, 0.0D);
        IVertexBuilder ivertexbuilder = p_225623_5_.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(p_225623_1_)));
        MatrixStack.Entry matrixstack$entry = p_225623_4_.peek();
        Matrix4f matrix4f = matrixstack$entry.getModel();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        /*this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, p_225623_6_);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, p_225623_6_);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, p_225623_6_);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, p_225623_6_);*/
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, p_225623_6_, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, p_225623_6_, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, p_225623_6_, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, p_225623_6_, r, g, b);

        for(int j = 0; j < 2; ++j) {
            p_225623_4_.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
            this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, p_225623_6_, r, g, b);
            this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, p_225623_6_, r, g, b);
            this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, p_225623_6_, r, g, b);
            this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, p_225623_6_, r, g, b);
        }

        p_225623_4_.pop();
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    public void func_229039_a_(Matrix4f p_229039_1_, Matrix3f p_229039_2_, IVertexBuilder p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_, int r, int g, int b) {
        p_229039_3_.vertex(p_229039_1_, (float)p_229039_4_, (float)p_229039_5_, (float)p_229039_6_).color(r, g, b, 255/3).texture(p_229039_7_, p_229039_8_).overlay(OverlayTexture.DEFAULT_UV).light(p_229039_12_).normal(p_229039_2_, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }
}
