package com.dudblockman.psipherals.block;

import com.dudblockman.psipherals.block.tile.TilePsilon;
import com.dudblockman.psipherals.util.InfusionCrafting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockPsilon extends Block {

    private static final VoxelShape SHAPE = VoxelShapes.or(VoxelShapes.or(
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.makeCuboidShape(2.0D, 12.0D, 2.0D, 14.0D, 16.0D, 14.0D)),
            Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D));


    public BlockPsilon(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TilePsilon();
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        breakBlock(state, world, pos);
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        breakBlock(state, world, pos);
        super.onBlockExploded(state, world, pos, explosion);
    }

    public void breakBlock(BlockState state, IBlockReader world, BlockPos pos) {
        if (hasTileEntity(state)) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TilePsilon) {
                ((TilePsilon) world.getTileEntity(pos)).onDestroyed(state, pos);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile != null) {
            return ((TilePsilon) tile).getComparatorValue();
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        worldIn.updateComparatorOutputLevel(pos, this);
        if (hasTileEntity(state)) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TilePsilon) {
                if(((TilePsilon) te).getComparatorValue() > 0) {
                  ((TilePsilon) te).scheduleOffTick();
                }
                if (((TilePsilon) te).isMaster()) {
                    if(((TilePsilon) te).mode == TilePsilon.InfusionState.READY) {
                        InfusionCrafting.ActivateInfusion((TilePsilon) te);
                    } else if(((TilePsilon) te).mode == TilePsilon.InfusionState.LIT) {
                        InfusionCrafting.infusionCraft((TilePsilon) te);
                    } else if(((TilePsilon) te).mode == TilePsilon.InfusionState.CONSUMING) {
                        ((TilePsilon) te).disconnect(true);
                    }
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        if (hasTileEntity(state)) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TilePsilon) {
                return ((TilePsilon) world.getTileEntity(pos)).onActivated(state, pos, player, hand);
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, ray);
    }
}
