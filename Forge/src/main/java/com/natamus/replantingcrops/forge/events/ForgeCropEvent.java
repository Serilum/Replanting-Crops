package com.natamus.replantingcrops.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.replantingcrops.events.CropEvent;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.lang.invoke.MethodHandles;

public class ForgeCropEvent {
	public static void registerEventsInBus() {
		BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeCropEvent.class);
	}

	@SubscribeEvent
	public static void onHarvest(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		CropEvent.onHarvest(level, e.getPlayer(), e.getPos(), e.getState(), null);
	}
	
	@SubscribeEvent
	public static boolean onCropEntity(EntityJoinLevelEvent e) {
		if (!CropEvent.onCropEntity(e.getLevel(), e.getEntity())) {
			return true;
		}
		return false;
	}
}
