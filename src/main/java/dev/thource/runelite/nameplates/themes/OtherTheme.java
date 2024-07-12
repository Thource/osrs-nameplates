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

    graphics.setColor(new Color(120, 50, 40));
    graphics.fillRect(
        borderSize,
        titleHeight + borderSize,
        (int) ((width - borderSize * 2) * nameplate.getHealthPercentage()),
        plateHeight - borderSize * 2);

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

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();
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

    graphics.setColor(new Color(20, 120, 110));
    graphics.fillRect(
        borderSize,
        titleHeight + plateHeight + borderSize,
        (int) ((width - borderSize * 2) * prayerPercentage),
        plateHeight - borderSize * 2);

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();
    String prayerString = currentPrayer + " / " + maxPrayer;
    Rectangle2D bounds = fontMetrics.getStringBounds(prayerString, graphics);

    graphics.setColor(Color.WHITE);
    graphics.drawString(
        prayerString,
        width / 2 - (int) bounds.getWidth() / 2,
        (int) (titleHeight + plateHeight * 1.5f + bounds.getHeight() / 2));
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
