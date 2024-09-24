package dev.thource.runelite.nameplates;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.opponentinfo.HitpointsDisplayStyle;

/** ImprovedContextMenuConfig manages the config for the plugin. */
@SuppressWarnings("SameReturnValue")
@ConfigGroup("nameplates")
public interface NameplatesConfig extends Config {

  // TODO list
  // =========
  // add options for when nameplates should show up
  // add another theme (possibly a minimal one)
  // add combat level to name only draws

  // RL dependency list
  // NPC.getOverheadIcon() - required so that overheads can be drawn on NPC nameplates
  // Player.getSkullIcon() for non-local players (including blue Forinthry Surge buff skull)
  // Overriding of overhead icons and skull icons

  // draw nameplates for players
  // draw nameplates for npcs
  // draw nameplates on hover
  // draw nameplates for npcs in combat
  // draw nameplates for npcs targetting you
  // draw nameplates for npcs that recently targetted you
  // nameplate draw range (excluding hover)

  String CONFIG_GROUP = "nameplates";

  @ConfigItem(
      keyName = "npcNameplateDisplayMode",
      name = "NPC nameplate display behaviour",
      description = "Defines when NPC nameplates will be visible.")
  default NameplateDisplayMode npcNameplateDisplayMode() {
    return NameplateDisplayMode.IN_COMBAT;
  }

  @ConfigItem(
      keyName = "playerNameplateDisplayMode",
      name = "Player nameplate display behaviour",
      description = "Defines when player nameplates will be visible.")
  default NameplateDisplayMode playerNameplateDisplayMode() {
    return NameplateDisplayMode.IN_COMBAT;
  }

  @ConfigItem(
      keyName = "ownNameplateDisplayMode",
      name = "Own nameplate display behaviour",
      description = "Defines when local player nameplate will be visible.")
  default NameplateDisplayMode ownNameplateDisplayMode() {
    return NameplateDisplayMode.ALWAYS;
  }

  @ConfigItem(
      keyName = "alwaysDrawNPCNames",
      name = "Always draw NPC names",
      description =
          "Still draw NPC names when nameplate display behaviour is set to hide the nameplates.")
  default boolean alwaysDrawNPCNames() {
    return true;
  }

  @ConfigItem(
      keyName = "alwaysDrawPlayerNames",
      name = "Always draw player names",
      description =
          "Still draw player names when nameplate display behaviour is set to hide the nameplates.")
  default boolean alwaysDrawPlayerNames() {
    return true;
  }

  @ConfigItem(
      keyName = "alwaysDrawOwnName",
      name = "Always draw own name",
      description =
          "Still draw own name when nameplate display behaviour is set to hide the nameplate.")
  default boolean alwaysDrawOwnName() {
    return false;
  }

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

  @ConfigItem(
      keyName = "lookupPlayerHp",
      name = "Fetch other players HP",
      description = "If enabled, fetch other players HP from the hiscores")
  default boolean lookupPlayerHp() {
    return true;
  }

  @ConfigItem(
      keyName = "enableNoLootOtherGIMSplats",
      name = "[GIM] Enable no-loot indicator for other hitsplats",
      description =
          "If enabled, NPCs will be tagged as no-loot if a tinted damage hitsplat appears<br>on"
              + " them.<br><br>Disable this if you frequently PVM with your group members"
              + " and the false<br>positive no-loot indicators are bothering you.")
  default boolean enableNoLootOtherGIMSplats() {
    return true;
  }

  @ConfigItem(
      keyName = "hoverIndicatorMode",
      name = "Hover indicator mode",
      description =
          "Sets when the hover indicator should show up.<br><br>Always: Always show while hovering"
              + " any NPC/player<br>Right-click: Only show when hovering entries in the right"
              + " click menu<br>Busy: Only show when multiple NPC/players are present in the"
              + " right-click menu<br>Busy right-click: Combines Busy and Right-click mode")
  default HoverIndicatorMode hoverIndicatorMode() {
    return HoverIndicatorMode.BUSY_RIGHT_CLICK;
  }

  @ConfigSection(
      name = "Theme settings: Default",
      description = "Settings for the Default theme",
      position = 100,
      closedByDefault = true)
  String SECTION_THEME_DEFAULT = "Theme settings: Default";

  @ConfigItem(
      keyName = "themeDefaultDrawNameInHealthBar",
      name = "Draw name in health bar",
      description =
          "If enabled, draws the name in the health bar (left aligned, hp will be right"
              + " aligned)<br>This will hide combat level and hp boost/drain text.",
      section = SECTION_THEME_DEFAULT,
      position = 1)
  default boolean themeDefaultDrawNameInHealthBar() {
    return false;
  }

  @ConfigItem(
      keyName = "themeDefaultColorBorder",
      name = "Border colour",
      description = "The colour of the nameplate border",
      section = SECTION_THEME_DEFAULT,
      position = 10)
  default Color themeDefaultColorBorder() {
    return new Color(0.1f, 0.1f, 0.1f);
  }

  @ConfigItem(
      keyName = "themeDefaultColorBarBackground",
      name = "Bar background colour",
      description = "The colour of the bar background",
      section = SECTION_THEME_DEFAULT,
      position = 11)
  default Color themeDefaultColorBarBackground() {
    return new Color(0.3f, 0.3f, 0.3f);
  }

  @ConfigItem(
      keyName = "themeDefaultColorHealthBarFill",
      name = "Health bar fill colour",
      description = "The colour of the health bar foreground",
      section = SECTION_THEME_DEFAULT,
      position = 100)
  default Color themeDefaultColorHealthBarFill() {
    return new Color(120, 50, 40);
  }

  @ConfigItem(
      keyName = "themeDefaultColorHealthBarFillPoison",
      name = "Health bar poison fill colour",
      description = "The colour of the health bar foreground while poisoned",
      section = SECTION_THEME_DEFAULT,
      position = 101)
  default Color themeDefaultColorHealthBarFillPoison() {
    return new Color(50, 120, 40);
  }

  @ConfigItem(
      keyName = "themeDefaultColorHealthBarDamagePoison",
      name = "Health bar poison damage colour",
      description = "The colour of the health bar poison damage amount indicator",
      section = SECTION_THEME_DEFAULT,
      position = 102)
  default Color themeDefaultColorHealthBarDamagePoison() {
    return new Color(30, 80, 20);
  }

  @ConfigItem(
      keyName = "themeDefaultColorHealthBarFillVenom",
      name = "Health bar venom fill colour",
      description = "The colour of the health bar foreground while envenomed",
      section = SECTION_THEME_DEFAULT,
      position = 103)
  default Color themeDefaultColorHealthBarFillVenom() {
    return new Color(50, 100, 80);
  }

  @ConfigItem(
      keyName = "themeDefaultColorHealthBarDamageVenom",
      name = "Health bar venom damage colour",
      description = "The colour of the health bar venom damage amount indicator",
      section = SECTION_THEME_DEFAULT,
      position = 104)
  default Color themeDefaultColorHealthBarDamageVenom() {
    return new Color(30, 60, 50);
  }

  @ConfigItem(
      keyName = "themeDefaultColorHealthBarPositiveChange",
      name = "Health bar consumable heal colour",
      description = "The colour of the health bar consumable heal indicator",
      section = SECTION_THEME_DEFAULT,
      position = 105)
  default Color themeDefaultColorHealthBarPositiveChange() {
    return new Color(80, 30, 20);
  }

  @ConfigItem(
      keyName = "themeDefaultColorHealthBarCappedChange",
      name = "Health bar consumable capped heal colour",
      description =
          "The colour of the health bar consumable heal indicator where the boost would be"
              + " partially wasted by hitting max hp",
      section = SECTION_THEME_DEFAULT,
      position = 106)
  default Color themeDefaultColorHealthBarCappedChange() {
    return new Color(100, 80, 0);
  }

  @ConfigItem(
      keyName = "themeDefaultColorHealthBarNegativeChange",
      name = "Health bar consumable damage colour",
      description = "The colour of the health bar consumable damage indicator",
      section = SECTION_THEME_DEFAULT,
      position = 107)
  default Color themeDefaultColorHealthBarNegativeChange() {
    return new Color(80, 30, 20);
  }

  @ConfigItem(
      keyName = "themeDefaultColorPrayerBarFill",
      name = "Prayer bar fill colour",
      description = "The colour of the prayer bar foreground",
      section = SECTION_THEME_DEFAULT,
      position = 200)
  default Color themeDefaultColorPrayerBarFill() {
    return new Color(20, 120, 110);
  }

  @ConfigItem(
      keyName = "themeDefaultColorPrayerBarFillActive",
      name = "Prayer bar active fill colour",
      description = "The colour of the prayer bar foreground while any prayer is active",
      section = SECTION_THEME_DEFAULT,
      position = 201)
  default Color themeDefaultColorPrayerBarFillActive() {
    return new Color(30, 180, 160);
  }

  @ConfigItem(
      keyName = "themeDefaultColorPrayerBarPositiveChange",
      name = "Prayer bar consumable boost colour",
      description = "The colour of the prayer bar consumable boost indicator",
      section = SECTION_THEME_DEFAULT,
      position = 202)
  default Color themeDefaultColorPrayerBarPositiveChange() {
    return new Color(20, 90, 80);
  }

  @ConfigItem(
      keyName = "themeDefaultColorPrayerBarCappedChange",
      name = "Prayer bar consumable capped boost colour",
      description =
          "The colour of the prayer bar consumable boost indicator where the boost would be"
              + " partially wasted by hitting max prayer points",
      section = SECTION_THEME_DEFAULT,
      position = 203)
  default Color themeDefaultColorPrayerBarCappedChange() {
    return new Color(100, 80, 0);
  }

  @ConfigItem(
      keyName = "themeDefaultColorPrayerBarNegativeChange",
      name = "Prayer bar consumable drain colour",
      description = "The colour of the prayer bar consumable drain indicator",
      section = SECTION_THEME_DEFAULT,
      position = 204)
  default Color themeDefaultColorPrayerBarNegativeChange() {
    return new Color(20, 90, 80);
  }

  @ConfigItem(
      keyName = "themeDefaultColorConsumableTextPositive",
      name = "Consumable boost text colour",
      description = "The colour of the consumable text change colour where the boost is positive",
      section = SECTION_THEME_DEFAULT,
      position = 300)
  default Color themeDefaultColorConsumableTextPositive() {
    return new Color(60, 200, 40);
  }

  @ConfigItem(
      keyName = "themeDefaultColorConsumableTextCapped",
      name = "Consumable capped text colour",
      description =
          "The colour of the consumable text change colour where the boost is positive but capped",
      section = SECTION_THEME_DEFAULT,
      position = 301)
  default Color themeDefaultColorConsumableTextCapped() {
    return new Color(200, 200, 60);
  }

  @ConfigItem(
      keyName = "themeDefaultColorConsumableTextNegative",
      name = "Consumable drain text colour",
      description = "The colour of the consumable text change colour where the boost is negative",
      section = SECTION_THEME_DEFAULT,
      position = 302)
  default Color themeDefaultColorConsumableTextNegative() {
    return new Color(200, 50, 50);
  }

  @ConfigItem(
      keyName = "themeDefaultColorPoisonDamageIndicator",
      name = "Poison damage indicator colour",
      description = "The colour of the poison damage indicator",
      section = SECTION_THEME_DEFAULT,
      position = 400)
  default Color themeDefaultColorPoisonDamageIndicator() {
    return Color.RED;
  }

  @ConfigItem(
      keyName = "themeDefaultColorHpRegenIndicator",
      name = "Hp regen indicator colour",
      description = "The colour of the hp regen indicator",
      section = SECTION_THEME_DEFAULT,
      position = 401)
  default Color themeDefaultColorHpRegenIndicator() {
    return Color.LIGHT_GRAY;
  }

  @ConfigItem(
      keyName = "themeDefaultColorPrayerFlickIndicator",
      name = "Prayer flick indicator colour",
      description = "The colour of the prayer flick indicator",
      section = SECTION_THEME_DEFAULT,
      position = 402)
  default Color themeDefaultColorPrayerFlickIndicator() {
    return Color.BLUE;
  }
}
