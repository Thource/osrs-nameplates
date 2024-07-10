package dev.thource.runelite.nameplates;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import net.runelite.api.HeadIcon;
import net.runelite.client.util.ImageUtil;

public enum NameplateHeadIcon {
  MELEE(HeadIcon.MELEE, "protect_from_melee"),
  RANGED(HeadIcon.RANGED, "protect_from_ranged"),
  MAGIC(HeadIcon.MAGIC, "protect_from_magic"),
  RETRIBUTION(HeadIcon.RETRIBUTION, "retribution"),
  SMITE(HeadIcon.SMITE, "smite"),
  REDEMPTION(HeadIcon.REDEMPTION, "redemption"),
  RANGE_MAGE(HeadIcon.RANGE_MAGE, "smite"),
  RANGE_MELEE(HeadIcon.RANGE_MELEE, "smite"),
  MAGE_MELEE(HeadIcon.MAGE_MELEE, "smite"),
  RANGE_MAGE_MELEE(HeadIcon.RANGE_MAGE_MELEE, "smite"),
  WRATH(HeadIcon.WRATH, "smite"),
  SOUL_SPLIT(HeadIcon.SOUL_SPLIT, "smite"),
  DEFLECT_MELEE(HeadIcon.DEFLECT_MELEE, "smite"),
  DEFLECT_RANGE(HeadIcon.DEFLECT_RANGE, "smite"),
  DEFLECT_MAGE(HeadIcon.DEFLECT_MAGE, "smite");

  private final HeadIcon headIcon;
  @Getter private final BufferedImage image;

  private static final Map<HeadIcon, NameplateHeadIcon> map;

  static {
    map = new HashMap<>();
    for (NameplateHeadIcon v : NameplateHeadIcon.values()) {
      map.put(v.headIcon, v);
    }
  }

  NameplateHeadIcon(HeadIcon headIcon, String imageName) {
    this.headIcon = headIcon;
    image = ImageUtil.loadImageResource(NameplatesPlugin.class, "overheads/" + imageName + ".png");
  }

  public static NameplateHeadIcon get(HeadIcon headIcon) {
    return map.get(headIcon);
  }
}
