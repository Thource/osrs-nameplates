package dev.thource.runelite.nameplates;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.HeadIcon;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;

@RequiredArgsConstructor
public enum NameplateHeadIcon {
  MELEE(HeadIcon.MELEE, 0),
  RANGED(HeadIcon.RANGED, 1),
  MAGIC(HeadIcon.MAGIC, 2),
  RETRIBUTION(HeadIcon.RETRIBUTION, 3),
  SMITE(HeadIcon.SMITE, 4),
  REDEMPTION(HeadIcon.REDEMPTION, 5),
  RANGE_MAGE(HeadIcon.RANGE_MAGE, 6),
  RANGE_MELEE(HeadIcon.RANGE_MELEE, 7),
  MAGE_MELEE(HeadIcon.MAGE_MELEE, 8),
  RANGE_MAGE_MELEE(HeadIcon.RANGE_MAGE_MELEE, 9),
  WRATH(HeadIcon.WRATH, 10),
  SOUL_SPLIT(HeadIcon.SOUL_SPLIT, 11),
  DEFLECT_MELEE(HeadIcon.DEFLECT_MELEE, 12),
  DEFLECT_RANGE(HeadIcon.DEFLECT_RANGE, 13),
  DEFLECT_MAGE(HeadIcon.DEFLECT_MAGE, 14);

  private final HeadIcon headIcon;
  private final int overheadFileId;
  @Getter private BufferedImage image;

  private static final Map<HeadIcon, NameplateHeadIcon> map;

  static {
    map = new HashMap<>();
    for (NameplateHeadIcon v : NameplateHeadIcon.values()) {
      map.put(v.headIcon, v);
    }
  }

  public static NameplateHeadIcon get(HeadIcon headIcon) {
    return map.get(headIcon);
  }

  public void loadImage(SpriteManager spriteManager) {
    if (image != null) {
      return;
    }

    image = spriteManager.getSprite(SpriteID.OVERHEAD_PROTECT_FROM_MELEE, overheadFileId);
  }
}
