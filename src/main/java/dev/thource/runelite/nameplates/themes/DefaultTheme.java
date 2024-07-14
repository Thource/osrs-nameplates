package dev.thource.runelite.nameplates.themes;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplateHeadIcon;
import dev.thource.runelite.nameplates.PoisonStatus;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stats;
import net.runelite.client.ui.FontManager;

public class DefaultTheme extends BaseTheme {
  private static final int TITLE_HEIGHT = 10;
  private static final int PLATE_HEIGHT = 14;

  @Override
  protected void drawBasePlate(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    if (nameplate.getMaxHealth() <= 0) {
      return;
    }

    //        graphics.setColor(Color.RED);
    //        graphics.fillRect(0, 0, width, height);

    int plateHeight = getPlateHeight(graphics, scale, nameplate);
    if (shouldDrawPrayerBar(nameplate.getActor())) {
      plateHeight *= 2;
    }

    graphics.setColor(config.themeDefaultColorBorder());
    graphics.fillRect(0, getTitleHeight(scale), width, plateHeight);
  }

  @Override
  protected void drawName(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    if (config.themeDefaultDrawNameInHealthBar() && nameplate.getMaxHealth() > 0) {
      return;
    }

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();

    int textLineY =
        (int) (fontMetrics.getStringBounds(nameplate.getName(), graphics).getHeight() * 0.9f);
    graphics.setColor(Color.WHITE);
    graphics.drawString(nameplate.getName(), nameplate.getMaxHealth() <= 0 ? 0 : 2, textLineY);
  }

  @Override
  protected void drawCombatLevel(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    if (config.themeDefaultDrawNameInHealthBar()) {
      // TODO: figure out where to draw combat level
      return;
    }

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();
    String combatLevelString = String.valueOf(nameplate.getCombatLevel());
    Rectangle2D combatLevelBounds = fontMetrics.getStringBounds(combatLevelString, graphics);

    int textLineY =
        (int)
            (fontMetrics
                    .getStringBounds(String.valueOf(nameplate.getCombatLevel()), graphics)
                    .getHeight()
                * 0.9f);
    graphics.setColor(
        getLevelColor(
            plugin.getClient().getLocalPlayer().getCombatLevel(), nameplate.getCombatLevel()));
    graphics.drawString(combatLevelString, (int) (width - combatLevelBounds.getWidth()), textLineY);
  }

  @Override
  protected void drawBarBackground(
      Graphics2D graphics, int barLeftX, int barTopY, int fullBarWidth, int barHeight) {
    graphics.setColor(config.themeDefaultColorBarBackground());
    graphics.fillRect(barLeftX, barTopY, fullBarWidth, barHeight);
  }

  @Override
  protected int getBorderSize(double scale) {
    return (int) Math.ceil(scale);
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
    Color barColor = config.themeDefaultColorHealthBarFill();
    if (isLocalPlayer && poisonStatus != null) {
      if (poisonStatus.isVenom()) {
        barColor = config.themeDefaultColorHealthBarFillVenom();
      } else {
        barColor = config.themeDefaultColorHealthBarFillPoison();
      }
    }
    graphics.setColor(barColor);
    graphics.fillRect(borderSize, barTopY, barWidth, barHeight);
  }

  @Override
  protected void drawHealthBarText(
      Graphics2D graphics,
      int width,
      float scale,
      Nameplate nameplate,
      FontMetrics fontMetrics,
      int borderSize,
      int barTopY,
      int barHeight) {
    String healthString = getHealthString(nameplate);

    Rectangle2D healthBounds = fontMetrics.getStringBounds(healthString, graphics);

    graphics.setColor(Color.WHITE);
    if (config.themeDefaultDrawNameInHealthBar()) {
      Rectangle2D nameBounds = fontMetrics.getStringBounds(nameplate.getName(), graphics);
      graphics.drawString(
          nameplate.getName(),
          borderSize * 3,
          (int) (barTopY + barHeight / 2f + nameBounds.getHeight() / 2));
      graphics.drawString(
          healthString,
          width - borderSize * 3 - (int) healthBounds.getWidth(),
          (int) (barTopY + barHeight / 2f + healthBounds.getHeight() / 2));
    } else {
      graphics.drawString(
          healthString,
          width / 2 - (int) healthBounds.getWidth() / 2,
          (int) (barTopY + barHeight / 2f + healthBounds.getHeight() / 2));
    }
  }

  @Override
  protected void drawConsumableChangeText(
      Graphics2D graphics,
      int width,
      StatChange change,
      FontMetrics fontMetrics,
      int borderSize,
      int barTopY,
      int barHeight) {
    if (config.themeDefaultDrawNameInHealthBar() && change.getStat() == Stats.HITPOINTS) {
      return;
    }

    Color changeColor = config.themeDefaultColorConsumableTextPositive();
    if (change.getRelative() > 0) {
      if (change.getRelative() != change.getTheoretical()) {
        changeColor = config.themeDefaultColorConsumableTextCapped();
      }
    } else {
      changeColor = config.themeDefaultColorConsumableTextNegative();
    }
    graphics.setColor(changeColor);

    Rectangle2D changeBounds = fontMetrics.getStringBounds(change.getFormattedRelative(), graphics);
    graphics.drawString(
        change.getFormattedRelative(),
        width - borderSize * 3 - (int) changeBounds.getWidth(),
        (int) (barTopY + barHeight * 0.5f + changeBounds.getHeight() / 2));
  }

  @Override
  protected void drawHealthBarPoisonIndicator(
      Graphics2D graphics, int borderSize, int fullBarWidth, int barTopY, int barHeight) {
    double indicatorProgress =
        (double) Duration.between(Instant.now(), plugin.getNextPoisonTick()).toMillis()
            / PoisonStatus.POISON_TICK_MILLIS;
    int indicatorX = (int) (borderSize + fullBarWidth * indicatorProgress);

    graphics.setColor(config.themeDefaultColorPoisonDamageIndicator());
    graphics.fillRect(indicatorX, barTopY, 1, barHeight);
  }

  @Override
  protected void drawHealthBarRegenIndicator(
      Graphics2D graphics,
      Nameplate nameplate,
      int borderSize,
      int fullBarWidth,
      int barTopY,
      int barHeight) {
    double indicatorProgress = plugin.getHpRegenProgress();
    if (indicatorProgress > 0) {
      if (nameplate.getHealthPercentage() > 1) {
        indicatorProgress = Math.max(0, 1 - indicatorProgress);
      }
      int indicatorX = (int) (borderSize + fullBarWidth * indicatorProgress);

      graphics.setColor(config.themeDefaultColorHpRegenIndicator());
      graphics.fillRect(indicatorX, barTopY, 1, barHeight);
    }
  }

  @Override
  protected void drawHealthBarConsumableChange(
      Graphics2D graphics,
      Nameplate nameplate,
      StatChange change,
      int fullBarWidth,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight) {
    Color changeColor = config.themeDefaultColorHealthBarPositiveChange();
    if (change.getRelative() < 0) {
      changeColor = config.themeDefaultColorHealthBarNegativeChange();
    } else if (change.getRelative() != change.getTheoretical()) {
      changeColor = config.themeDefaultColorHealthBarCappedChange();
    }
    graphics.setColor(changeColor);

    int changeWidth =
        (int) (fullBarWidth * ((float) change.getRelative() / nameplate.getMaxHealth()));
    int changeOffset = 0;
    if (changeWidth < 0) {
      changeOffset = changeWidth;
      changeWidth = -changeWidth;
    }
    graphics.fillRect(borderSize + barWidth + changeOffset, barTopY, changeWidth, barHeight);
  }

  @Override
  protected void drawHealthBarPoisonChange(
      Graphics2D graphics,
      Nameplate nameplate,
      PoisonStatus poisonStatus,
      int fullBarWidth,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight) {
    int nextPoisonDamage = Math.min(nameplate.getCurrentHealth(), poisonStatus.getDamage());
    int changeWidth = (int) (fullBarWidth * ((float) nextPoisonDamage / nameplate.getMaxHealth()));
    Color changeColor = config.themeDefaultColorHealthBarDamagePoison();
    if (poisonStatus.isVenom()) {
      changeColor = config.themeDefaultColorHealthBarDamageVenom();
    }
    graphics.setColor(changeColor);
    graphics.fillRect(borderSize + barWidth - changeWidth, barTopY, changeWidth, barHeight);
  }

  @Override
  protected void drawPrayerBarConsumableChange(
      Graphics2D graphics,
      int fullBarWidth,
      StatChange prayerChange,
      int maxPrayer,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight) {
    Color changeColor = config.themeDefaultColorPrayerBarPositiveChange();
    if (prayerChange.getRelative() < 0) {
      changeColor = config.themeDefaultColorPrayerBarNegativeChange();
    } else if (prayerChange.getRelative() != prayerChange.getTheoretical()) {
      changeColor = config.themeDefaultColorPrayerBarCappedChange();
    }

    int changeWidth = (int) (fullBarWidth * ((float) prayerChange.getRelative() / maxPrayer));
    int changeOffset = 0;
    if (changeWidth < 0) {
      changeOffset = changeWidth;
      changeWidth = -changeWidth;
    }
    graphics.setColor(changeColor);
    graphics.fillRect(borderSize + barWidth + changeOffset, barTopY, changeWidth, barHeight);
  }

  @Override
  protected void drawPrayerBarText(
      Graphics2D graphics,
      int width,
      int currentPrayer,
      int maxPrayer,
      FontMetrics fontMetrics,
      int barTopY,
      int barHeight) {
    String prayerString = currentPrayer + " / " + maxPrayer;
    Rectangle2D bounds = fontMetrics.getStringBounds(prayerString, graphics);

    graphics.setColor(Color.WHITE);
    graphics.drawString(
        prayerString,
        width / 2 - (int) bounds.getWidth() / 2,
        (int) (barTopY + barHeight * 0.5f + bounds.getHeight() / 2));
  }

  @Override
  protected void drawPrayerBarFlickIndicator(
      Graphics2D graphics, int borderSize, int fullBarWidth, int barTopY, int barHeight) {
    double indicatorProgress = Math.max(0, 1 - plugin.getTickProgress());
    int indicatorX = (int) (borderSize + fullBarWidth * indicatorProgress);

    graphics.setColor(config.themeDefaultColorPrayerFlickIndicator());
    graphics.fillRect(indicatorX, barTopY, 1, barHeight);
  }

  @Override
  protected void drawPrayerBarBar(
      Graphics2D graphics, int borderSize, int barTopY, int barWidth, int barHeight) {
    Color barColor = config.themeDefaultColorPrayerBarFill();
    if (plugin.isAnyPrayerActive()) {
      barColor = config.themeDefaultColorPrayerBarFillActive();
    }
    graphics.setColor(barColor);
    graphics.fillRect(borderSize, barTopY, barWidth, barHeight);
  }

  @Override
  protected int getTitleHeight(float scale) {
    if (config.themeDefaultDrawNameInHealthBar()) {
      return 0;
    }

    return (int) Math.floor(TITLE_HEIGHT * scale);
  }

  @Override
  protected int getPlateHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
    int plateHeight = getHeight(graphics, scale, nameplate) - getTitleHeight(scale);

    if (shouldDrawPrayerBar(nameplate.getActor())) {
      return plateHeight / 2;
    }

    return plateHeight;
  }

  @Override
  protected void drawOverheads(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate, Point anchor) {
    if (!(nameplate.getActor() instanceof Player)) {
      return;
    }

    NameplateHeadIcon overheadIcon =
        NameplateHeadIcon.get(((Player) nameplate.getActor()).getOverheadIcon());
    if (overheadIcon == null) {
      return;
    }

    int rightX = anchor.getX() + width / 2;
    int topY = anchor.getY() - height;
    int overheadSize = height;

    // reduce overhead size to ensure consistency
    if (shouldDrawPrayerBar(nameplate.getActor())) {
      overheadSize -= getPlateHeight(graphics, scale, nameplate);
    }

    BufferedImage overheadImage = overheadIcon.getImage();
    graphics.drawImage(
        overheadImage.getScaledInstance(
            overheadSize, overheadSize, Image.SCALE_SMOOTH), // TODO: optimise this
        rightX + (int) (6 * scale),
        topY,
        overheadSize,
        overheadSize,
        null);
  }

  @Override
  public int getHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
    if (nameplate.getMaxHealth() <= 0) {
      graphics.setFont(
          FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
      FontMetrics fontMetrics = graphics.getFontMetrics();

      return (int) fontMetrics.getStringBounds(nameplate.getName(), graphics).getHeight();
    }

    int height = PLATE_HEIGHT;
    if (!config.themeDefaultDrawNameInHealthBar()) {
      height += TITLE_HEIGHT;
    }
    if (shouldDrawPrayerBar(nameplate.getActor())) {
      height += PLATE_HEIGHT;
    }
    return (int) (height * scale);
  }

  @Override
  protected int getWidth(Graphics2D graphics, float scale, Nameplate nameplate) {
    if (nameplate.getMaxHealth() <= 0) {
      graphics.setFont(
          FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
      FontMetrics fontMetrics = graphics.getFontMetrics();

      return (int) fontMetrics.getStringBounds(nameplate.getName(), graphics).getWidth();
    }

    return (int) (120 * scale);
  }
}
