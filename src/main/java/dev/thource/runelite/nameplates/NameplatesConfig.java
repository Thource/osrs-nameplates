package dev.thource.runelite.nameplates;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.opponentinfo.HitpointsDisplayStyle;

/** ImprovedContextMenuConfig manages the config for the plugin. */
@SuppressWarnings("SameReturnValue")
@ConfigGroup("nameplates")
public interface NameplatesConfig extends Config {

  String CONFIG_GROUP = "nameplates";

  @ConfigItem(
      keyName = "hitpointsDisplayStyle",
      name = "Hitpoints display style",
      description = "Show hitpoints as a value, percentage, or both")
  default HitpointsDisplayStyle hitpointsDisplayStyle() {
    return HitpointsDisplayStyle.HITPOINTS;
  }

  @ConfigItem(
      keyName = "drawPrayerBar",
      name = "Prayer bar",
      description = "If enabled, draws a prayer bar on the local player's nameplate")
  default boolean drawPrayerBar() {
    return true;
  }

  @ConfigItem(
      keyName = "drawNameInHealthBar",
      name = "Draw name in health bar",
      description =
          "If enabled, draws the name in the health bar (left aligned, hp will be right aligned)")
  default boolean drawNameInHealthBar() {
    return false;
  }

  // consumable healing indicator (display that hp will go up by 20 if you eat a shark, etc)
  // health regen indicator
  // poison damage indicator
  // prayer 1-tick flick indicator

  // draw nameplates for players
  // draw nameplates for npcs
  // draw nameplates on hover
  // draw nameplates for npcs in combat
  // draw nameplates for npcs targetting you
  // draw nameplates for npcs that recently targetted you
  // nameplate draw range (excluding hover)
}
