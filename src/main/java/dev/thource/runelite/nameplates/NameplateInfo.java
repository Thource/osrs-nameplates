package dev.thource.runelite.nameplates;

import lombok.Getter;
import net.runelite.api.NPC;
import net.runelite.api.Point;

public class NameplateInfo {
  @Getter private final String name;
  @Getter private final int currentHealth;
  @Getter private final Integer maxHealth;
  @Getter private final int combatLevel;
  @Getter private final Point anchor;
  @Getter private final float scale;
  @Getter private final NPC npc;

  NameplateInfo(
      String name,
      int currentHealth,
      Integer maxHealth,
      int combatLevel,
      Point anchor,
      float scale,
      NPC npc) {

    this.name = name;
    this.currentHealth = currentHealth;
    this.maxHealth = maxHealth;
    this.combatLevel = combatLevel;
    this.anchor = anchor;
    this.scale = scale;
    this.npc = npc;
  }

  public float getHealthPercentage() {
    return (float) currentHealth / maxHealth;
  }
}
