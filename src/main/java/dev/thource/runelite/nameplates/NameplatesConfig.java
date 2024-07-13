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

  @ConfigItem(
      keyName = "drawPrayerFlickIndicator",
      name = "Draw prayer flick indicator",
      description = "If enabled, draws the prayer flick indicator")
  default boolean drawPrayerFlickIndicator() {
    return false;
  }

  @ConfigItem(
      keyName = "drawHpRegenIndicator",
      name = "Draw hp regen indicator",
      description = "If enabled, draws the hp regen indicator")
  default boolean drawHpRegenIndicator() {
    return false;
  }

  @ConfigItem(
      keyName = "drawConsumableHealAmount",
      name = "Draw consumable heal amount",
      description = "If enabled, draws how much the hovered consumable will heal/damage you for")
  default boolean drawConsumableHealAmount() {
    return true;
  }

  @ConfigItem(
      keyName = "drawPoisonDamageIndicator",
      name = "Draw poison damage indicator",
      description = "If enabled, draws the poison damage indicator")
  default boolean drawPoisonDamageIndicator() {
    return true;
  }

  // poison damage indicator

  // draw nameplates for players
  // draw nameplates for npcs
  // draw nameplates on hover
  // draw nameplates for npcs in combat
  // draw nameplates for npcs targetting you
  // draw nameplates for npcs that recently targetted you
  // nameplate draw range (excluding hover)
}
