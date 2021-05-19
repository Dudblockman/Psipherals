package com.dudblockman.psipherals.block.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

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
            matrixStackIn.translate(0.5,0.9375+.25, 0.5);
            long i = MathHelper.getCoordinateRandom(tileEntityIn.getPos().getX(), tileEntityIn.getPos().getY(), tileEntityIn.getPos().getZ());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(3 * ((mc.world.getGameTime()+i) % 360 + partialTicks)));
            ir.renderItem(tileEntityIn.stack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
    }
}
