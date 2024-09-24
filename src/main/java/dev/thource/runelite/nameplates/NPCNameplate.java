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
}
