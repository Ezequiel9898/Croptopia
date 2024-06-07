package com.epherical.croptopia.items;

import com.epherical.croptopia.Croptopia;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class GuideBookItem extends Item {


    public GuideBookItem(Properties settings) {
        super(settings);
    }


    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        if (!Croptopia.patchouli.isLoaded()) {
            list.add(Component.nullToEmpty("Patchouli is not installed."));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {

        ItemStack stack = user.getItemInHand(hand);

        if (user instanceof ServerPlayer && Croptopia.patchouli.isLoaded()) {
            ServerPlayer player = (ServerPlayer) user;
            PatchouliAPI.get().openBookGUI(player, BuiltInRegistries.ITEM.getKey(this));
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
