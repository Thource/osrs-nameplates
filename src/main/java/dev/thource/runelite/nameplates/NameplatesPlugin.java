package dev.thource.runelite.nameplates;

import com.google.inject.Provides;
import dev.thource.runelite.nameplates.themes.Themes;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.api.SpritePixels;
import net.runelite.api.VarPlayer;
import net.runelite.api.Varbits;
import net.runelite.api.WorldView;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.NPCManager;
import net.runelite.client.hiscore.HiscoreClient;
import net.runelite.client.hiscore.HiscoreEndpoint;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

/** NameplatesPlugin is a RuneLite plugin designed to add WoW style nameplates to NPCs. */
@Slf4j
@PluginDescriptor(
    name = "Nameplates",
    description = "Adds nameplates to NPCs.",
    tags = {"nameplates", "health", "npcs"})
public class NameplatesPlugin extends Plugin {

  @Getter @Inject private Client client;
  @Getter @Inject private ClientThread clientThread;
  @Getter @Inject private NameplatesConfig config;
  @Inject private OverlayManager overlayManager;
  @Inject private NameplatesOverlay nameplatesOverlay;
  @Getter @Inject private NPCManager npcManager;
  @Getter @Inject private HiscoreClient hiscoreClient;

  @Getter private final HashMap<Integer, HpCacheEntry> hpCache = new HashMap<>();
  @Getter private final HashMap<Integer, Nameplate> nameplates = new HashMap<>();
  @Getter private final HashMap<Integer, SpritePixels> overriddenSprites = new HashMap<>();

  private static final int[] spritesToHide = {
    SpriteID.HEALTHBAR_DEFAULT_FRONT_30PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_40PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_50PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_60PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_70PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_80PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_100PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_120PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_140PX,
    SpriteID.HEALTHBAR_DEFAULT_FRONT_160PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_30PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_40PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_50PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_60PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_70PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_80PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_100PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_120PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_140PX,
    SpriteID.HEALTHBAR_DEFAULT_BACK_160PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_30PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_40PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_50PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_60PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_70PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_80PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_100PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_120PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_140PX,
    SpriteID.HEALTHBAR_CYAN_FRONT_160PX,
    SpriteID.HEALTHBAR_CYAN_BACK_30PX,
    SpriteID.HEALTHBAR_CYAN_BACK_40PX,
    SpriteID.HEALTHBAR_CYAN_BACK_50PX,
    SpriteID.HEALTHBAR_CYAN_BACK_60PX,
    SpriteID.HEALTHBAR_CYAN_BACK_70PX,
    SpriteID.HEALTHBAR_CYAN_BACK_80PX,
    SpriteID.HEALTHBAR_CYAN_BACK_100PX,
    SpriteID.HEALTHBAR_CYAN_BACK_120PX,
    SpriteID.HEALTHBAR_CYAN_BACK_140PX,
    SpriteID.HEALTHBAR_CYAN_BACK_160PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_30PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_40PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_50PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_60PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_70PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_80PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_100PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_120PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_140PX,
    SpriteID.HEALTHBAR_ORANGE_FRONT_160PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_30PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_40PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_50PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_60PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_70PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_80PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_100PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_120PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_140PX,
    SpriteID.HEALTHBAR_ORANGE_BACK_160PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_30PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_40PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_50PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_60PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_70PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_80PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_100PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_120PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_140PX,
    SpriteID.HEALTHBAR_YELLOW_FRONT_160PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_30PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_40PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_50PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_60PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_70PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_80PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_100PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_120PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_140PX,
    SpriteID.HEALTHBAR_YELLOW_BACK_160PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_30PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_40PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_50PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_60PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_70PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_80PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_100PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_120PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_140PX,
    SpriteID.HEALTHBAR_PURPLE_FRONT_160PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_30PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_40PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_50PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_60PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_70PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_80PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_100PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_120PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_140PX,
    SpriteID.HEALTHBAR_PURPLE_BACK_160PX,
    SpriteID.HEALTHBAR_BLUE_FRONT_50PX,
    SpriteID.HEALTHBAR_BLUE_BACK_50PX,
    SpriteID.HEALTHBAR_COX_GREEN,
    SpriteID.HEALTHBAR_COX_BLUE,
    SpriteID.HEALTHBAR_COX_YELLOW,
    SpriteID.HEALTHBAR_COX_RED,
  };
  private int cacheCleaningTick;
  SpritePixels transparent;
  @Getter private HiscoreEndpoint hiscoreEndpoint = HiscoreEndpoint.NORMAL;

  private void overrideSprites() {
    Map<Integer, SpritePixels> overrides = client.getSpriteOverrides();
    boolean anySpriteOverridden = false;

    for (int spriteId : spritesToHide) {
      SpritePixels overridenSprite = overrides.get(spriteId);
      if (overridenSprite == transparent) {
        continue;
      }

      overriddenSprites.put(spriteId, overrides.get(spriteId));
      overrides.put(spriteId, transparent);
      anySpriteOverridden = true;
    }

    if (anySpriteOverridden) {
      clientThread.invokeLater(client::resetHealthBarCaches);
    }
  }

  private void restoreSprites() {
    Map<Integer, SpritePixels> overrides = client.getSpriteOverrides();

    for (int spriteId : spritesToHide) {
      SpritePixels overriddenSprite = overriddenSprites.get(spriteId);

      if (overriddenSprite != null) {
        overrides.put(spriteId, overriddenSprite);
      } else {
        overrides.remove(spriteId);
      }
    }

    clientThread.invokeLater(client::resetHealthBarCaches);
  }

  @Override
  protected void startUp() {
    for (Themes theme : Themes.values()) {
      theme.getTheme().setPlugin(this);
    }

    if (transparent == null) {
      transparent =
          ImageUtil.getImageSpritePixels(
              new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), client);
    }

    overlayManager.add(nameplatesOverlay);
  }

  @Override
  protected void shutDown() {
    overlayManager.remove(nameplatesOverlay);

    restoreSprites();
  }

  private int getCurrentHealth(Actor actor, int maxHealth) {
    if (actor instanceof Player && actor == client.getLocalPlayer()) {
      return client.getBoostedSkillLevel(Skill.HITPOINTS);
    }

    if (actor instanceof NPC
        && ((NPC) actor).getId() == client.getVarpValue(VarPlayer.HP_HUD_NPC_ID)) {
      return client.getVarbitValue(Varbits.BOSS_HEALTH_CURRENT);
    }

    if (actor.getHealthScale() == -1) {
      HpCacheEntry cacheEntry = getHpCacheEntryForActor(actor);
      if (cacheEntry != null) {
        return cacheEntry.getHp();
      }

      return maxHealth;
    }

    if (actor.getHealthRatio() == 0) {
      return 0;
    }

    int min = 1;
    int max;
    if (actor.getHealthScale() > 1) {
      if (actor.getHealthRatio() > 1) {
        min =
            (maxHealth * (actor.getHealthRatio() - 1) + actor.getHealthScale() - 2)
                / (actor.getHealthScale() - 1);
      }
      max =
          Math.min(
              (maxHealth * actor.getHealthRatio() - 1) / (actor.getHealthScale() - 1), maxHealth);
    } else {
      max = maxHealth;
    }

    return (min + max + 1) / 2;
  }

  static int getActorId(Actor actor) {
    if (actor instanceof NPC) {
      // Offset this by 2048 to avoid collisions with player ids
      return ((NPC) actor).getIndex() + 2048;
    }

    return ((Player) actor).getId();
  }

  HpCacheEntry getHpCacheEntryForActor(Actor actor) {
    return hpCache.get(getActorId(actor));
  }

  Nameplate getNameplateForActor(Actor actor) {
    return nameplates.get(getActorId(actor));
  }

  private void updateHpCache(Actor actor) {
    if (actor.isDead() || client.getLocalPlayer() == actor) {
      return;
    }

    Integer maxHealth = 10;
    Nameplate nameplate = getNameplateForActor(actor);
    if (nameplate != null) {
      maxHealth = nameplate.getMaxHealth();
    } else {
      if (actor instanceof NPC) {
        maxHealth = npcManager.getHealth(((NPC) actor).getId());
      }
      if (maxHealth == null) {
        return;
      }
    }

    int actorId = getActorId(actor);
    int currentHealth = getCurrentHealth(actor, maxHealth);
    if (currentHealth > 0) {
      HpCacheEntry cacheEntry = hpCache.computeIfAbsent(actorId, (k) -> new HpCacheEntry(actorId));
      if (actor.getHealthScale() != -1) {
        cacheEntry.setHealthScale(actor.getHealthScale());
      }
      cacheEntry.setHp(currentHealth);
      cacheEntry.setLastUpdate(client.getTickCount());
    }
  }

  private void updateNameplate(Actor actor) {
    Nameplate nameplate = getNameplateForActor(actor);
    if (nameplate == null) {
      return;
    }

    int hp;
    HpCacheEntry cacheEntry = getHpCacheEntryForActor(actor);
    if (cacheEntry == null) {
      if (client.getLocalPlayer() != actor) {
        nameplate.getHpAnimationData().startAnimation(nameplate.getCurrentHealth(), 0, 200);
        nameplate.setCurrentHealth(0);

        return;
      }

      hp = getCurrentHealth(actor, nameplate.getMaxHealth());
    } else {
      hp = cacheEntry.getHp();
      nameplate.setHealthScale(cacheEntry.getHealthScale());
    }

    if (hp != nameplate.getCurrentHealth()) {
      nameplate.getHpAnimationData().startAnimation(nameplate.getCurrentHealth(), hp, 200);
      nameplate.setCurrentHealth(hp);
    }
    nameplate.updateFromActor(this, actor);
  }

  @Subscribe
  public void onGameStateChanged(GameStateChanged gameStateChanged) {
    if (gameStateChanged.getGameState() != GameState.LOGGED_IN) {
      return;
    }

    hiscoreEndpoint = HiscoreEndpoint.fromWorldTypes(client.getWorldType());
  }

  @Subscribe
  public void onGameTick(GameTick tick) {
    overrideSprites();

    WorldView topLevelWorldView = client.getTopLevelWorldView();
    Stream.of(topLevelWorldView.npcs(), topLevelWorldView.players())
        .flatMap(IndexedObjectSet::stream)
        .forEach(
            (actor) -> {
              updateHpCache(actor);
              updateNameplate(actor);
            });

    cacheCleaningTick++;

    if (cacheCleaningTick >= 10) {
      hpCache.values().removeIf((entry) -> client.getTickCount() >= entry.getLastUpdate() + 500);

      cacheCleaningTick = 0;
    }
  }

  @Subscribe
  public void onNpcSpawned(NpcSpawned npcSpawned) {
    NPC npc = npcSpawned.getNpc();

    nameplates.put(getActorId(npc), new Nameplate(this, npc));
  }

  @Subscribe
  public void onNpcDespawned(NpcDespawned npcDespawned) {
    nameplates.remove(getActorId(npcDespawned.getNpc()));
  }

  @Subscribe
  public void onPlayerSpawned(PlayerSpawned playerSpawned) {
    Player player = playerSpawned.getPlayer();

    nameplates.put(getActorId(player), new Nameplate(this, player));
  }

  @Subscribe
  public void onPlayerDespawned(PlayerDespawned playerDespawned) {
    nameplates.remove(getActorId(playerDespawned.getPlayer()));
  }

  @Subscribe
  public void onActorDeath(ActorDeath actorDeath) {
    Actor actor = actorDeath.getActor();

    hpCache.remove(getActorId(actor));
  }

  @Subscribe
  public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
    System.out.println(
        "Hitsplat - "
            + hitsplatApplied.getActor().getName()
            + ": "
            + hitsplatApplied.getHitsplat().getAmount());
  }

  @Provides
  NameplatesConfig provideConfig(ConfigManager configManager) {
    return configManager.getConfig(NameplatesConfig.class);
  }
}
