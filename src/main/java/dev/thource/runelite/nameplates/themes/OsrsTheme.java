package dev.thource.runelite.nameplates.themes;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.PoisonStatus;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import net.runelite.api.Point;
import net.runelite.api.SpriteID;
import net.runelite.api.SpritePixels;
import net.runelite.client.plugins.itemstats.StatChange;

public class OsrsTheme extends BaseTheme {
  @Override
  protected void drawBasePlate(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    SpritePixels healthbarBackSprite = getHealthbarBackSprite(nameplate.getHealthScale());
    if (healthbarBackSprite != null) {
      graphics.drawImage(healthbarBackSprite.toBufferedImage(), null, 0, 0);
    } else {
      graphics.setColor(Color.RED);
      graphics.fillRect(0, 0, width, height);
    }
  }

  @Override
  protected void drawName(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {}

  @Override
  protected void drawCombatLevel(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {}

  @Override
  protected void drawBarBackground(
      Graphics2D graphics, int barLeftX, int barTopY, int fullBarWidth, int barHeight) {}

  @Override
  protected void drawPrayerBarText(
      Graphics2D graphics,
      int width,
      int currentPrayer,
      int maxPrayer,
      FontMetrics fontMetrics,
      int barTopY,
      int barHeight) {}

  @Override
  protected void drawPrayerBarFlickIndicator(
      Graphics2D graphics, int borderSize, int fullBarWidth, int barTopY, int barHeight) {}

  @Override
  protected void drawPrayerBarBar(
      Graphics2D graphics, int borderSize, int barTopY, int barWidth, int barHeight) {}

  @Override
  protected int getTitleHeight(float scale) {
    return 0;
  }

  @Override
  protected int getPlateHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
    return 7;
  }

  @Override
  protected void drawHealthBarBar(
      Graphics2D graphics,
      boolean isLocalPlayer,
      PoisonStatus poisonStatus,
      int borderSize,
      int barTopY,
      int barWidth,
      int barHeight,
      Nameplate nameplate) {
    SpritePixels healthbarFrontSprite = getHealthbarFrontSprite(nameplate.getHealthScale());
    if (healthbarFrontSprite != null) {
      graphics.drawImage(
          healthbarFrontSprite.toBufferedImage(),
          0,
          0,
          barWidth,
          barHeight,
          0,
          0,
          barWidth,
          barHeight,
          null);
    } else {
      graphics.setColor(Color.GREEN);
      graphics.fillRect(0, 0, barWidth, barHeight);
    }
  }

  @Override
  protected void drawHealthBarText(
      Graphics2D graphics,
      int width,
      float scale,
      Nameplate nameplate,
      FontMetrics fontMetrics,
      int borderSize,
      int titleHeight,
      int plateHeight) {}

  @Override
  protected void drawConsumableChangeText(
      Graphics2D graphics,
      int width,
      StatChange hpChange,
      FontMetrics fontMetrics,
      int borderSize,
      int barTopY,
      int barHeight) {}

  @Override
  protected void drawHealthBarPoisonIndicator(
      Graphics2D graphics, int borderSize, int fullBarWidth, int barTopY, int barHeight) {}

  @Override
  protected void drawHealthBarRegenIndicator(
      Graphics2D graphics,
      Nameplate nameplate,
      int borderSize,
      int fullBarWidth,
      int barTopY,
      int barHeight) {}

  @Override
  protected void drawHealthBarConsumableChange(
      Graphics2D graphics,
      Nameplate nameplate,
      StatChange change,
      int fullBarWidth,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight) {}

  @Override
  protected void drawHealthBarPoisonChange(
      Graphics2D graphics,
      Nameplate nameplate,
      PoisonStatus poisonStatus,
      int fullBarWidth,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight) {}

  @Override
  protected void drawPrayerBar(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {}

  @Override
  protected void drawPrayerBarConsumableChange(
      Graphics2D graphics,
      int fullBarWidth,
      StatChange prayerChange,
      int maxPrayer,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight) {}

  @Override
  protected void drawOverheads(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate, Point anchor) {}

  @Override
  protected void drawHoverIndicator(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate, Point anchor) {}

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
