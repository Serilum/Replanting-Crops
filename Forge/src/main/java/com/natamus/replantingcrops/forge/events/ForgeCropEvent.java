package com.natamus.replantingcrops.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.replantingcrops.events.CropEvent;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeCropEvent {
	@SubscribeEvent
	public void onHarvest(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (level == null) {
			return;
		}

		CropEvent.onHarvest(level, e.getPlayer(), e.getPos(), e.getState(), null);
	}
	
	@SubscribeEvent
	public void onCropEntity(EntityJoinWorldEvent e) {
		if (!CropEvent.onCropEntity(e.getWorld(), e.getEntity())) {
			e.setCanceled(true);
		}
	}
}
