package dev.thource.runelite.nameplates.themes;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplateHeadIcon;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.opponentinfo.HitpointsDisplayStyle;
import net.runelite.client.ui.FontManager;

public class OtherTheme extends BaseTheme {
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

    graphics.setColor(new Color(0.1f, 0.1f, 0.1f));
    graphics.fillRect(0, getTitleHeight(scale), width, plateHeight);
  }

  @Override
  protected void drawName(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    if (plugin.getConfig().drawNameInHealthBar()) {
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
    if (plugin.getConfig().drawNameInHealthBar()) {
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
  protected void drawHealthBar(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    int borderSize = (int) Math.ceil(scale);
    int titleHeight = getTitleHeight(scale);
    int plateHeight = getPlateHeight(graphics, scale, nameplate);
    int barTopY = titleHeight + borderSize;
    int fullBarWidth = (width - borderSize * 2);
    int barWidth = (int) (fullBarWidth * Math.min(1, nameplate.getHealthPercentage()));
    int barHeight = plateHeight - borderSize * 2;

    graphics.setColor(new Color(120, 50, 40));
    graphics.fillRect(borderSize, titleHeight + borderSize, barWidth, barHeight);

    StatChange hpChange = null;
    if (plugin.getConfig().drawConsumableHealAmount()) {
      hpChange = plugin.getHoveredItemHpChange();
    }

    if (hpChange != null && hpChange.getRelative() != 0) {
      Color changeColor = new Color(80, 30, 20);
      if (hpChange.getRelative() > 0 && hpChange.getRelative() != hpChange.getTheoretical()) {
        changeColor = new Color(100, 80, 0);
      }
      graphics.setColor(changeColor);

      int changeWidth =
          (int) (fullBarWidth * ((float) hpChange.getRelative() / nameplate.getMaxHealth()));
      int changeOffset = 0;
      if (changeWidth < 0) {
        changeOffset = changeWidth;
        changeWidth = -changeWidth;
      }
      graphics.fillRect(
          borderSize + barWidth + changeOffset, titleHeight + borderSize, changeWidth, barHeight);
    }

    if (plugin.getConfig().drawHpRegenIndicator()
        && nameplate.getActor() == plugin.getClient().getLocalPlayer()) {
      double indicatorProgress = plugin.getHpRegenProgress();
      if (indicatorProgress > 0) {
        if (nameplate.getHealthPercentage() > 1) {
          indicatorProgress = Math.max(0, 1 - indicatorProgress);
        }
        int indicatorX = (int) (borderSize + fullBarWidth * indicatorProgress);

        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawLine(indicatorX, barTopY, indicatorX, barTopY + barHeight);
      }
    }

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();

    if (hpChange != null && hpChange.getRelative() != 0) {
      Color changeColor = new Color(60, 200, 40);
      if (hpChange.getRelative() > 0) {
        if (hpChange.getRelative() != hpChange.getTheoretical()) {
          changeColor = new Color(200, 200, 60);
        }
      } else {
        changeColor = new Color(200, 50, 50);
      }
      graphics.setColor(changeColor);

      Rectangle2D changeBounds =
          fontMetrics.getStringBounds(hpChange.getFormattedRelative(), graphics);
      graphics.drawString(
          hpChange.getFormattedRelative(),
          width - borderSize - (int) changeBounds.getWidth(),
          (int) (titleHeight + plateHeight / 2f + changeBounds.getHeight() / 2));
    }

    String healthString = nameplate.getCurrentHealth() + " / " + nameplate.getMaxHealth();
    HitpointsDisplayStyle displayStyle = plugin.getConfig().hitpointsDisplayStyle();
    if (displayStyle != HitpointsDisplayStyle.HITPOINTS) {
      double percentage =
          Math.ceil((float) nameplate.getCurrentHealth() / nameplate.getMaxHealth() * 1000f) / 10f;

      if (displayStyle == HitpointsDisplayStyle.PERCENTAGE) {
        healthString = percentage + "%";
      } else {
        healthString += " (" + percentage + "%)";
      }
    }

    Rectangle2D healthBounds = fontMetrics.getStringBounds(healthString, graphics);

    graphics.setColor(Color.WHITE);
    if (plugin.getConfig().drawNameInHealthBar()) {
      Rectangle2D nameBounds = fontMetrics.getStringBounds(nameplate.getName(), graphics);
      graphics.drawString(
          nameplate.getName(),
          borderSize + 2 * scale,
          (int) (titleHeight + plateHeight / 2f + nameBounds.getHeight() / 2));
      graphics.drawString(
          healthString,
          width - borderSize - 2 - (int) healthBounds.getWidth(),
          (int) (titleHeight + plateHeight / 2f + healthBounds.getHeight() / 2));
    } else {
      graphics.drawString(
          healthString,
          width / 2 - (int) healthBounds.getWidth() / 2,
          (int) (titleHeight + plateHeight / 2f + healthBounds.getHeight() / 2));
    }
  }

  @Override
  protected void drawPrayerBar(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    int borderSize = (int) Math.ceil(scale);
    int titleHeight = getTitleHeight(scale);
    int plateHeight = getPlateHeight(graphics, scale, nameplate);
    int currentPrayer = plugin.getClient().getBoostedSkillLevel(Skill.PRAYER);
    int maxPrayer = plugin.getClient().getRealSkillLevel(Skill.PRAYER);
    float prayerPercentage = (float) currentPrayer / maxPrayer;
    int barTopY = titleHeight + plateHeight + borderSize;
    int barHeight = plateHeight - borderSize * 2;

    graphics.setColor(new Color(20, 120, 110));
    graphics.fillRect(
        borderSize,
        barTopY,
        (int) ((width - borderSize * 2) * Math.min(1, prayerPercentage)),
        barHeight);

    if (plugin.getConfig().drawPrayerFlickIndicator() && plugin.isAnyPrayerActive()) {
      double indicatorProgress = Math.max(0, 1 - plugin.getTickProgress());
      int indicatorX = (int) (borderSize + (width - borderSize * 2) * indicatorProgress);

      graphics.setColor(Color.LIGHT_GRAY);
      graphics.drawLine(indicatorX, barTopY, indicatorX, barTopY + barHeight);
    }

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();
    String prayerString = currentPrayer + " / " + maxPrayer;
    Rectangle2D bounds = fontMetrics.getStringBounds(prayerString, graphics);

    graphics.setColor(Color.WHITE);
    graphics.drawString(
        prayerString,
        width / 2 - (int) bounds.getWidth() / 2,
        (int) (barTopY + barHeight * 0.5f + bounds.getHeight() / 2));
  }

  private int getTitleHeight(float scale) {
    if (plugin.getConfig().drawNameInHealthBar()) {
      return 0;
    }

    return (int) Math.floor(TITLE_HEIGHT * scale);
  }

  private int getPlateHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
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
    if (!plugin.getConfig().drawNameInHealthBar()) {
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
