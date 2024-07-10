package dev.thource.runelite.nameplates.themes;

import dev.thource.runelite.nameplates.Nameplate;
import java.awt.Color;
import java.awt.Graphics2D;
import net.runelite.api.SpriteID;
import net.runelite.api.SpritePixels;

public class OsrsTheme extends BaseTheme {
  @Override
  protected void drawBasePlate(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    int frontWidth = (int) (width * nameplate.getHealthPercentage());

    SpritePixels healthbarBackSprite = getHealthbarBackSprite(nameplate.getHealthScale());
    if (healthbarBackSprite != null) {
      graphics.drawImage(healthbarBackSprite.toBufferedImage(), null, 0, 0);
    } else {
      graphics.setColor(Color.RED);
      graphics.fillRect(0, 0, width, height);
    }

    SpritePixels healthbarFrontSprite = getHealthbarFrontSprite(nameplate.getHealthScale());
    if (healthbarFrontSprite != null) {
      graphics.drawImage(
          healthbarFrontSprite.toBufferedImage(),
          0,
          0,
          frontWidth,
          height,
          0,
          0,
          frontWidth,
          height,
          null);
    } else {
      graphics.setColor(Color.GREEN);
      graphics.fillRect(0, 0, frontWidth, height);
    }
  }

  private SpritePixels getHealthbarBackSprite(int healthScale) {
    int spriteId;
    switch (healthScale) {
      case 40:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_40PX;
        break;
      case 50:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_50PX;
        break;
      case 60:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_60PX;
        break;
      case 70:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_70PX;
        break;
      case 80:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_80PX;
        break;
      case 100:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_100PX;
        break;
      case 120:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_120PX;
        break;
      case 140:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_140PX;
        break;
      case 160:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_160PX;
        break;
      default:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_BACK_30PX;
    }

    return plugin.getOverriddenSprites().get(spriteId);
  }

  private SpritePixels getHealthbarFrontSprite(int healthScale) {
    int spriteId;
    switch (healthScale) {
      case 40:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_40PX;
        break;
      case 50:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_50PX;
        break;
      case 60:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_60PX;
        break;
      case 70:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_70PX;
        break;
      case 80:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_80PX;
        break;
      case 100:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_100PX;
        break;
      case 120:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_120PX;
        break;
      case 140:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_140PX;
        break;
      case 160:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_160PX;
        break;
      default:
        spriteId = SpriteID.HEALTHBAR_DEFAULT_FRONT_30PX;
    }

    return plugin.getOverriddenSprites().get(spriteId);
  }

  @Override
  protected void drawOverlay(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {}

  @Override
  protected int getWidth(Graphics2D graphics, float scale, Nameplate nameplate) {
    SpritePixels backSprite = getHealthbarBackSprite(nameplate.getHealthScale());
    if (backSprite != null) {
      return backSprite.getWidth();
    }

    return nameplate.getHealthScale();
  }

  @Override
  public int getHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
    return 7;
  }
}
