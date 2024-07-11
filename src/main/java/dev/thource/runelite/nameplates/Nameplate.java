package dev.thource.runelite.nameplates;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Actor;
import net.runelite.client.util.Text;

@Getter
public abstract class Nameplate {
  protected final Actor actor;
  protected String name;
  protected int maxHealth = 0;
  protected int combatLevel;
  @Setter protected int healthScale = 30;
  @Setter protected int currentHealth;
  @Setter protected int lastUpdate;
  protected final AnimationData hpAnimationData = new AnimationData();

  public Nameplate(NameplatesPlugin plugin, Actor actor) {
    this.actor = actor;
    updateFromActor(plugin);
    currentHealth = maxHealth;
    hpAnimationData.startAnimation(maxHealth, maxHealth, 0);
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    this.name = Text.removeTags(actor.getName());
    combatLevel = actor.getCombatLevel();
  }

  public float getHealthPercentage() {
    return hpAnimationData.getCurrentValue() / maxHealth;
  }
}
