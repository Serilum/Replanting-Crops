package com.natamus.replantingcrops.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.replantingcrops.events.CropEvent;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class NeoForgeCropEvent {
	@SubscribeEvent
	public static void onHarvest(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		CropEvent.onHarvest(level, e.getPlayer(), e.getPos(), e.getState(), null);
	}
	
	@SubscribeEvent
	public static void onCropEntity(EntityJoinLevelEvent e) {
		if (!CropEvent.onCropEntity(e.getLevel(), e.getEntity())) {
			e.setCanceled(true);
		}
	}
}
