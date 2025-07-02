package com.natamus.replantingcrops.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.replantingcrops.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean mustHoldHoeForReplanting = true;
	@Entry public static boolean onlyReplantFullyGrown = true;

	public static void initConfig() {
		configMetaData.put("mustHoldHoeForReplanting", Arrays.asList(
				"If enabled, players must hold a hoe in their hand to automatically replant the crop."
		));
		configMetaData.put("onlyReplantFullyGrown", Arrays.asList(
				"If enabled, wheat will only be replanted if it is fully grown."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}