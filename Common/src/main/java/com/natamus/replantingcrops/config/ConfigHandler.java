package com.natamus.replantingcrops.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.replantingcrops.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean mustHoldToolForReplanting = true;

	public static void initConfig() {
		configMetaData.put("mustHoldToolForReplanting", Arrays.asList(
			"If enabled, players must hold a hoe/axe in their hand to automatically replant the crop."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}