package com.epherical.croptopia.listeners;

import com.epherical.croptopia.blocks.LeafCropBlock;
import com.epherical.croptopia.events.HarvestEvent;
import com.epherical.croptopia.mixin.AgeInvokerMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.epherical.croptopia.CroptopiaForge.mod;


public class Harvest {


    @SubscribeEvent
    public void onHarvest(PlayerInteractEvent.RightClickBlock event) {
        if (mod.config().rightClickHarvest && !event.getEntity().isSpectator()) {
            if (!(event.getEntity().getMainHandItem().getItem() instanceof BoneMealItem)) {
                if (!event.getLevel().isClientSide) {
                    Level world = event.getLevel();
                    BlockPos pos = event.getPos();
                    BlockState blockClicked = event.getLevel().getBlockState(pos);
                    if (blockClicked.getBlock() instanceof CropBlock block && block instanceof AgeInvokerMixin mixin) {
                        IntegerProperty property = mixin.doGetAgeProperty();
                        int age = blockClicked.getValue(property);
                        if (age == block.getMaxAge()) {
                            HarvestEvent harvestedCropEvent = new HarvestEvent(event.getEntity(), blockClicked, withAge(blockClicked, property, 0));
                            NeoForge.EVENT_BUS.post(harvestedCropEvent);
                            world.setBlock(pos, harvestedCropEvent.getTurnedState(), 2);
                            if (blockClicked.getBlock() instanceof LeafCropBlock) {
                                for (ItemStack drop : Block.getDrops(blockClicked, (ServerLevel) world, pos, null)) {
                                    Block.popResourceFromFace(world, pos, event.getFace(), drop);
                                }
                            } else {
                                Block.dropResources(blockClicked, world, event.getPos());
                            }
                            event.setCancellationResult(InteractionResult.SUCCESS);
                        }
                    }
                }
            }
        }
    }

    public BlockState withAge(BlockState state, IntegerProperty property, int age) {
        return state.setValue(property, Integer.valueOf(age));
    }
}
