package com.dudblockman.psipherals.block.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import vazkii.psi.client.render.entity.RenderSpellCircle;

public class TilePsilonRenderer extends TileEntityRenderer<TilePsilon> {

    public TilePsilonRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TilePsilon tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();

        if (!tileEntityIn.stack.isEmpty()) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5, 1.1875, 0.5);
            long i = MathHelper.getPositionRandom(tileEntityIn.getPos());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(3 * ((mc.world.getGameTime() + i) % 360 + partialTicks)));
            ir.renderItem(tileEntityIn.stack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }

        if (tileEntityIn.isMaster()) {
            BlockPos offset = tileEntityIn.connectedPsilons.get(0).subtract(tileEntityIn.getPos());
            float distance = (float) Math.sqrt(offset.getX() * offset.getX() + offset.getZ() * offset.getZ());
            matrixStackIn.push();
            matrixStackIn.translate(0.5, 0.1, 0.5);
            int color = tileEntityIn.getColorizerColor();
            float size = -0.4f + distance * 0.675f;
            RenderSpellCircle.renderSpellCircle(mc.world.getGameTime() + partialTicks, tileEntityIn.getCircleActivation(partialTicks), size, 0, 1, 0, color, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
    }

    public boolean isGlobalRenderer(TilePsilon te) {
        return true;
    }
}
