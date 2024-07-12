package dev.thource.runelite.nameplates;

import lombok.Getter;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.client.hiscore.HiscoreSkill;

@Getter
public class PlayerNameplate extends Nameplate {
  private boolean hiscoreLookupStarted;

  public PlayerNameplate(NameplatesPlugin plugin, Player actor) {
    super(plugin, actor);
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    super.updateFromActor(plugin);

    if (actor == plugin.getClient().getLocalPlayer()) {
      maxHealth = plugin.getClient().getRealSkillLevel(Skill.HITPOINTS);
      return;
    }

    if (!hiscoreLookupStarted) {
      this.maxHealth = 99;

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

                  HpCacheEntry cacheEntry = plugin.getHpCacheEntryForActor(actor);
                  if (cacheEntry != null) {
                    cacheEntry.setHp(hp);
                  }
                }
              });
    }
  }
}