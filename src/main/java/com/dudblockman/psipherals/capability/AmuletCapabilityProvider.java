package com.dudblockman.psipherals.capability;

import com.dudblockman.psipherals.spell.trick.internal.TrickExecuteAmulet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.tool.ToolSocketable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class AmuletCapabilityProvider extends ToolSocketable implements ICurio {

    private static final String TAG_TIMES_CAST = "timesCast";

    private final LazyOptional<?> capOptional;

    public AmuletCapabilityProvider(ItemStack tool, int slots) {
        super(tool, slots);
        this.capOptional = LazyOptional.of(() -> this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PsiAPI.SOCKETABLE_CAPABILITY ||
                cap == PsiAPI.SPELL_ACCEPTOR_CAPABILITY ||
                cap == CuriosCapability.ITEM) {
            return capOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void setSpell(PlayerEntity player, Spell spell) {
        int slot = this.getSelectedSlot();
        ItemStack bullet = this.getBulletInSocket(slot);
        if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
            ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
            this.setBulletInSocket(slot, bullet);
        } else if (bullet.isEmpty()) {
            ItemSpellDrive.setSpell(tool, spell);
        }

    }

    @Override
    public Spell getSpell() {
        return ItemSpellDrive.getSpell(tool);
    }

    @Override
    public boolean containsSpell() {
        return tool.getOrCreateTag().getBoolean(ItemSpellDrive.HAS_SPELL);
    }

    @Override
    public ArrayList<Entity> castSpell(SpellContext context) {
        if (!(context.cspell.actions.firstElement().piece instanceof TrickExecuteAmulet)) {
            CompiledSpell.Action newAction = context.cspell.new Action(new TrickExecuteAmulet(null));
            context.cspell.actions.add(0, newAction);
        }
        context.cspell.safeExecute(context);
        return null;
    }

    @Override
    public double getCostModifier() {
        return 1.0;
    }

    @Override
    public boolean castableFromSocket() {
        return true;
    }

    @Override
    public boolean isCADOnlyContainer() {
        return true;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) livingEntity;
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack playerCad = PsiAPI.getPlayerCAD(player);

        if (!playerCad.isEmpty()) {
            int timesCast = tool.getOrCreateTag().getInt(TAG_TIMES_CAST);
            ItemCAD.cast(player.getEntityWorld(), player, data, tool, playerCad, 2, 0, 0, (SpellContext context) -> {
                context.tool = tool;
                context.loopcastIndex = timesCast;
            });
            tool.getOrCreateTag().putInt(TAG_TIMES_CAST, timesCast + 1);
        }

    }
}
