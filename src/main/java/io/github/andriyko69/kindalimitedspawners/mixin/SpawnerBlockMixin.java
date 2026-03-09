package io.github.andriyko69.kindalimitedspawners.mixin;

import io.github.andriyko69.kindalimitedspawners.block.ModBlockStateProperties;
import io.github.andriyko69.kindalimitedspawners.client.SpawnerVisuals;
import io.github.andriyko69.kindalimitedspawners.logic.SpawnerLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnerBlock.class)
public abstract class SpawnerBlockMixin extends BaseEntityBlock {
    protected SpawnerBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack stack,
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hitResult
    ) {
        if (!SpawnerLogic.isNetherStar(stack)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        SpawnerBlockEntity spawner = kls$getSpawner(level, pos);
        if (spawner == null) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if (!SpawnerLogic.canInsertNetherStar(spawner)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        SpawnerLogic.setUnlimited(spawner, true);

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        SpawnerVisuals.syncVisualState(level, pos, true);
        level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return ItemInteractionResult.CONSUME;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull BlockHitResult hitResult
    ) {
        SpawnerBlockEntity spawner = kls$getSpawner(level, pos);
        if (spawner == null || !SpawnerLogic.canRemoveNetherStar(spawner)) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        SpawnerLogic.setUnlimited(spawner, false);

        ItemStack refundedStar = new ItemStack(Items.NETHER_STAR);
        if (!player.addItem(refundedStar)) {
            player.drop(refundedStar, false);
        }

        SpawnerVisuals.syncVisualState(level, pos, false);
        level.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ModBlockStateProperties.UNLIMITED);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void kls$initDefaultState(Properties properties, CallbackInfo ci) {
        this.registerDefaultState(this.defaultBlockState().setValue(ModBlockStateProperties.UNLIMITED, false));
    }

    @Unique
    private static SpawnerBlockEntity kls$getSpawner(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof SpawnerBlockEntity spawner ? spawner : null;
    }
}