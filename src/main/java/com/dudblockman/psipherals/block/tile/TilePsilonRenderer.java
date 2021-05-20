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
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.common.Psi;

public class TilePsilonRenderer extends TileEntityRenderer<TilePsilon> {
    public TilePsilonRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }
    float lastTime = 0;
    @Override
    public void render(TilePsilon tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        float delta = partialTicks - lastTime;
        if (delta <= 0) delta++;

        if (!tileEntityIn.stack.isEmpty()) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5,1.1875, 0.5);
            long i = MathHelper.getPositionRandom(tileEntityIn.getPos());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(3 * ((mc.world.getGameTime()+i) % 360 + partialTicks)));
            ir.renderItem(tileEntityIn.stack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (tileEntityIn.connectedPsilons != null) {
            if (tileEntityIn.isSlave()) {
                Vector3 origin = Vector3.fromBlockPos(tileEntityIn.getPos());
                Vector3 voffset = Vector3.fromBlockPos(tileEntityIn.connectedPsilons.get(0)).subtract(origin);
                double distance = voffset.mag();
                voffset.normalize();
                origin = origin.add(0.5, 1.1875, 0.5);
                switch (tileEntityIn.mode) {
                    case OFF:
                        break;
                    case READY:
                        for (int i = 0; i < (int) (3 * delta); i++) {
                            Vector3 dir = new Vector3();
                            double dist = 0.05;
                            dir.x += (Math.random() - 0.5);
                            dir.y += (Math.random() - 0.5);
                            dir.z += (Math.random() - 0.5);
                            dir.normalize().multiply(dist);
                            Psi.proxy.sparkleFX(origin.x, origin.y, origin.z, 1, 1, 1, (float) dir.x, (float) dir.y, (float) dir.z, 1, 10);
                        }
                        break;
                    case LIT:
                        for (int i = 0; i < (int) (5 * delta); i++) {
                            float s = 0.2F + (float) Math.random() * 0.1F;
                            float m = 0.01F + (float) Math.random() * 0.015F;
                            Psi.proxy.wispFX(origin.x, origin.y, origin.z, 1, 1, 1, s, 0, m, 0,1f);
                        }
                        break;
                    case CONSUMING:
                        double spread = 0.3;
                        double dist = 0.1 * distance;
                        for (int i = 0; i < (int) (5 * delta); i++) {
                            Vector3 dir = new Vector3(voffset);
                            dir.x += (Math.random() - 0.5) * spread;
                            dir.y += (Math.random() - 0.5) * spread;
                            dir.z += (Math.random() - 0.5) * spread;
                            dir.normalize().multiply(dist);
                            Psi.proxy.sparkleFX(origin.x, origin.y, origin.z, 1, 1, 1, (float) dir.x, (float) dir.y, (float) dir.z, 1, 20);
                            float s = 0.2F + (float) Math.random() * 0.1F;
                            float m = 0.01F + (float) Math.random() * 0.015F;
                            Psi.proxy.wispFX(origin.x, origin.y, origin.z, 1, 1, 1, s, (float) (voffset.x * m), (float) (voffset.y * m), (float) (voffset.z * m),1f);
                        }
                        break;
                }
            }
            if (tileEntityIn.isMaster()) {
                BlockPos offset = tileEntityIn.connectedPsilons.get(0).subtract(tileEntityIn.getPos());
                float distance = (float) Math.sqrt(offset.getX() * offset.getX() + offset.getZ() * offset.getZ());
                matrixStackIn.push();
                matrixStackIn.translate(0.5, 0.1, 0.5);
                int color = ICADColorizer.DEFAULT_SPELL_COLOR;
                RenderSpellCircle.renderSpellCircle(mc.world.getGameTime() + partialTicks, tileEntityIn.getCircleActivation(partialTicks), -0.4f + distance * 0.675f, 0, 1, 0, color, matrixStackIn, bufferIn);
                matrixStackIn.pop();
            }

        }
    }
    public boolean isGlobalRenderer(TilePsilon te) {
        return true;
    }
}
