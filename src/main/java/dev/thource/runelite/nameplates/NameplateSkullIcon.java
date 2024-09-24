package dev.thource.runelite.nameplates;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.SkullIcon;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;

@RequiredArgsConstructor
public enum NameplateSkullIcon {
  SKULL(SkullIcon.SKULL),
  SKULL_FIGHT_PIT(SkullIcon.SKULL_FIGHT_PIT),
  SKULL_HIGH_RISK(SkullIcon.SKULL_HIGH_RISK),
  FORINTHRY_SURGE(SkullIcon.FORINTHRY_SURGE),
  SKULL_DEADMAN(SkullIcon.SKULL_DEADMAN),
  LOOT_KEYS_ONE(SkullIcon.LOOT_KEYS_ONE),
  LOOT_KEYS_TWO(SkullIcon.LOOT_KEYS_TWO),
  LOOT_KEYS_THREE(SkullIcon.LOOT_KEYS_THREE),
  LOOT_KEYS_FOUR(SkullIcon.LOOT_KEYS_FOUR),
  LOOT_KEYS_FIVE(SkullIcon.LOOT_KEYS_FIVE),
  FORINTHRY_SURGE_DEADMAN(SkullIcon.FORINTHRY_SURGE_DEADMAN),
  FORINTHRY_SURGE_KEYS_ONE(SkullIcon.FORINTHRY_SURGE_KEYS_ONE),
  FORINTHRY_SURGE_KEYS_TWO(SkullIcon.FORINTHRY_SURGE_KEYS_TWO),
  FORINTHRY_SURGE_KEYS_THREE(SkullIcon.FORINTHRY_SURGE_KEYS_THREE),
  FORINTHRY_SURGE_KEYS_FOUR(SkullIcon.FORINTHRY_SURGE_KEYS_FOUR),
  FORINTHRY_SURGE_KEYS_FIVE(SkullIcon.FORINTHRY_SURGE_KEYS_FIVE);

  private final int skullIcon;
  @Getter private BufferedImage image;

  private static final Map<Integer, NameplateSkullIcon> map;

  static {
    map = new HashMap<>();
    for (NameplateSkullIcon v : NameplateSkullIcon.values()) {
      map.put(v.skullIcon, v);
    }
  }

  public static NameplateSkullIcon get(int skullIcon) {
    return map.get(skullIcon);
  }

  public void loadImage(SpriteManager spriteManager) {
    if (image != null) {
      return;
    }

    image = spriteManager.getSprite(SpriteID.PLAYER_KILLER_SKULL, skullIcon);
  }
}
