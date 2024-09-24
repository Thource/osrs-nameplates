package dev.thource.runelite.nameplates;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.ParamID;

@Getter
public class NPCNameplate extends Nameplate {
  @Setter private boolean noLoot;
  private boolean percentageHealth;
  private int percentageHealthOverride;
  private float firstPercentageHealth = -1f;
  @Setter private int damageTaken;

  public NPCNameplate(NameplatesPlugin plugin, NPC actor) {
    super(plugin, actor);
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    super.updateFromActor(plugin);

    int maxHealth = 0;

    NPC npc = (NPC) actor;
    Integer health = plugin.getNpcManager().getHealth(npc.getId());
    if (health != null) {
      maxHealth = health;
    }

    NPCComposition composition = npc.getTransformedComposition();
    if (composition != null) {
      String longName = composition.getStringValue(ParamID.NPC_HP_NAME);
      if (!Strings.isNullOrEmpty(longName)) {
        this.name = longName;
      }
    }

    this.percentageHealth = maxHealth <= 0 && percentageHealthOverride <= 0;
    this.maxHealth = this.percentageHealth ? 100 : maxHealth;
  }

  public void recalculatePercentageHealth(NameplatesPlugin plugin) {
    if (firstPercentageHealth == -1f) {
      firstPercentageHealth = (float) currentHealth / maxHealth;
    }

    float currentPercentage = plugin.getCurrentHealth(actor, 1000) / 1000f;
    float percentageDifference = firstPercentageHealth - currentPercentage;
    int estimatedMaxHealth = Math.round(damageTaken / percentageDifference);

    if (this.percentageHealth) {
      this.currentHealth = Math.round(estimatedMaxHealth * firstPercentageHealth);
    }
    this.percentageHealth = false;
    this.percentageHealthOverride = estimatedMaxHealth;
  }

  public int getMaxHealth() {
    if (percentageHealthOverride > 0) {
      return percentageHealthOverride;
    }

    return maxHealth;
  }
}
