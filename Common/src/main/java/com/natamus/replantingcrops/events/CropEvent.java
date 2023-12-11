package com.natamus.replantingcrops.events;

import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.services.Services;
import com.natamus.replantingcrops.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class CropEvent {
	private static final HashMap<BlockPos, Block> checkreplant = new HashMap<BlockPos, Block>();
	private static final HashMap<BlockPos, BlockState> cocoaStates = new HashMap<BlockPos, BlockState>();
	
	public static boolean onHarvest(Level world, Player player, BlockPos hpos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return true;
		}
		
		if (player == null) {
			return true;
		}
		
		InteractionHand hand = null;
		if (ConfigHandler.mustHoldHoeForReplanting) {
			hand = InteractionHand.MAIN_HAND;
			if (!Services.TOOLFUNCTIONS.isHoe(player.getMainHandItem())) {
				hand = InteractionHand.OFF_HAND;
				if (!Services.TOOLFUNCTIONS.isHoe(player.getOffhandItem())) {
					return true;
				}
			}
		}
		
		if (player.isShiftKeyDown()) {
			return true;
		}
		
		Block block = state.getBlock();

		if (block instanceof CropBlock) {
			checkreplant.put(hpos, block);
		}
		else if (block.equals(Blocks.NETHER_WART)) {
			checkreplant.put(hpos, block);
		}
		else if (block.equals(Blocks.COCOA)) {
			cocoaStates.put(hpos, state);
			checkreplant.put(hpos, block);
		}
		else {
			return true;
		}
		
		if (hand != null && !player.isCreative()) {
			ItemFunctions.itemHurtBreakAndEvent(player.getItemInHand(hand), (ServerPlayer)player, hand, 1);
		}
		
		return true;
	}
	
	public static boolean onCropEntity(Level world, Entity entity) {
		if (world.isClientSide) {
			return true;
		}
		
		if (!(entity instanceof ItemEntity)) {
			return true;
		}
		
		BlockPos ipos = entity.blockPosition();
		if (!checkreplant.containsKey(ipos)) {
			return true;
		}

		Block preblock = checkreplant.get(ipos);

		Item compareitem = null;
		if (preblock instanceof CropBlock) {
			compareitem = preblock.getCloneItemStack(world, ipos, null).getItem();
		}
		
		ItemEntity itementity = (ItemEntity)entity;
		ItemStack itemstack = itementity.getItem();
		Item item = itemstack.getItem();

		if (item.equals(compareitem)) {
			world.setBlockAndUpdate(ipos, preblock.defaultBlockState());
		}
		else if (item.equals(Items.NETHER_WART)) {
			world.setBlockAndUpdate(ipos, Blocks.NETHER_WART.defaultBlockState());
		}
		else if (item.equals(Items.COCOA_BEANS)) {
			if (!cocoaStates.containsKey(ipos)) {
				checkreplant.remove(ipos);
				return true;
			}
			world.setBlockAndUpdate(ipos, cocoaStates.get(ipos).setValue(CocoaBlock.AGE, 0));
			cocoaStates.remove(ipos);
		}
		else {
			return true;
		}

		checkreplant.remove(ipos);
		
		if (itemstack.getCount() > 1) {
			itemstack.shrink(1);
		}
		else {
			entity.remove(RemovalReason.DISCARDED);
			return false;
		}
		return true;
	}
}
