package dev.thource.runelite.nameplates;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.util.Text;

@Getter
public class Nameplate {
  private String name;
  private int maxHealth = 0;
  private int combatLevel;
  @Setter private int healthScale = 30;
  @Setter private int currentHealth;
  @Setter private int lastUpdate;
  private final AnimationData hpAnimationData = new AnimationData();
  private boolean hiscoreLookupStarted;

  public Nameplate(NameplatesPlugin plugin, Actor actor) {
    updateFromActor(plugin, actor);
    currentHealth = maxHealth;
    hpAnimationData.startAnimation(maxHealth, maxHealth, 0);
  }

  public void updateFromActor(NameplatesPlugin plugin, Actor actor) {
    String name = Text.removeTags(actor.getName());
    int maxHealth = 0;

    if (actor instanceof NPC) {
      NPC npc = (NPC) actor;
      Integer health = plugin.getNpcManager().getHealth(npc.getId());
      if (health != null) {
        maxHealth = health;
      }

      NPCComposition composition = npc.getTransformedComposition();

      if (composition != null) {
        String longName = composition.getStringValue(ParamID.NPC_HP_NAME);
        if (!Strings.isNullOrEmpty(longName)) {
          name = longName;
        }
      }
    } else if (actor == plugin.getClient().getLocalPlayer()) {
      maxHealth = plugin.getClient().getRealSkillLevel(Skill.HITPOINTS);
    } else if (!hiscoreLookupStarted) {
      hiscoreLookupStarted = true;
      final Nameplate nameplate = this;
      plugin
          .getHiscoreClient()
          .lookupAsync(name, plugin.getHiscoreEndpoint())
          .whenCompleteAsync(
              (result, ex) -> {
                if (nameplate == null || result == null) {
                  return;
                }

                int hp = Math.max(10, result.getSkill(HiscoreSkill.HITPOINTS).getLevel());
                nameplate.maxHealth = hp;

                if (nameplate.currentHealth > hp) {
                  nameplate.currentHealth = hp;
                  hpAnimationData.startAnimation(hp, hp, 0);

                  NpcHpCacheEntry cacheEntry = plugin.getHpCacheEntryForActor(actor);
                  if (cacheEntry != null) {
                    cacheEntry.setHp(hp);
                  }
                }
              });

      maxHealth = 99;
    }

    this.name = name;
    if (actor instanceof NPC || maxHealth != 0) {
      this.maxHealth = maxHealth;
    }
    combatLevel = actor.getCombatLevel();
  }

  public float getHealthPercentage() {
    return hpAnimationData.getCurrentValue() / maxHealth;
  }
}
