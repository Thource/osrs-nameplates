package dev.thource.runelite.nameplates;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;

/** ImprovedContextMenuConfig manages the config for the plugin. */
@SuppressWarnings("SameReturnValue")
@ConfigGroup("nameplates")
public interface NameplatesConfig extends Config {

  String CONFIG_GROUP = "nameplates";

  // draw nameplates for players
  // draw nameplates for npcs
  // draw nameplates on hover
  // draw nameplates for npcs in combat
  // draw nameplates for npcs targetting you
  // draw nameplates for npcs that recently targetted you
  // nameplate draw range (excluding hover)
}
