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
  SKULL(SkullIcon.SKULL, 0),
  SKULL_FIGHT_PIT(SkullIcon.SKULL_FIGHT_PIT, 1),
  DEAD_MAN_ONE(SkullIcon.DEAD_MAN_ONE, 8),
  DEAD_MAN_TWO(SkullIcon.DEAD_MAN_TWO, 9),
  DEAD_MAN_THREE(SkullIcon.DEAD_MAN_THREE, 10),
  DEAD_MAN_FOUR(SkullIcon.DEAD_MAN_FOUR, 11),
  DEAD_MAN_FIVE(SkullIcon.DEAD_MAN_FIVE, 12);

  private final SkullIcon skullIcon;
  private final int fileId;
  @Getter private BufferedImage image;

  private static final Map<SkullIcon, NameplateSkullIcon> map;

  static {
    map = new HashMap<>();
    for (NameplateSkullIcon v : NameplateSkullIcon.values()) {
      map.put(v.skullIcon, v);
    }
  }

  public static NameplateSkullIcon get(SkullIcon skullIcon) {
    return map.get(skullIcon);
  }

  public void loadImage(SpriteManager spriteManager) {
    if (image != null) {
      return;
    }

    image = spriteManager.getSprite(SpriteID.PLAYER_KILLER_SKULL, fileId);
  }
}
