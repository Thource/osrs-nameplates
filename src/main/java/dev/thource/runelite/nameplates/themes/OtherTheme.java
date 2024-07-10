package dev.thource.runelite.nameplates.themes;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplateHeadIcon;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import net.runelite.api.Actor;
import net.runelite.api.Player;
import net.runelite.api.Point;
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

    graphics.setColor(new Color(0.1f, 0.1f, 0.1f));
    graphics.fillRect(0, getTitleHeight(scale), width, getPlateHeight(graphics, scale, nameplate));
  }

  private static int getTitleHeight(float scale) {
    return (int) Math.floor(TITLE_HEIGHT * scale);
  }

  private int getPlateHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
    return getHeight(graphics, scale, nameplate) - getTitleHeight(scale);
  }

  @Override
  protected void drawOverlay(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    if (nameplate.getMaxHealth() <= 0) {
      if (nameplate.getName() == null
          || nameplate.getName().isEmpty()
          || nameplate.getName().equals("null")) {
        return;
      }

      graphics.setFont(
          FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
      FontMetrics fontMetrics = graphics.getFontMetrics();

      int textLineY =
          (int) (fontMetrics.getStringBounds(nameplate.getName(), graphics).getHeight() * 0.9f);
      graphics.setColor(Color.WHITE);
      graphics.drawString(nameplate.getName(), 0, textLineY);

      return;
    }

    int borderSize = (int) Math.ceil(scale);
    int titleHeight = getTitleHeight(scale);
    int plateHeight = getPlateHeight(graphics, scale, nameplate);

    graphics.setColor(new Color(120, 50, 40));
    graphics.fillRect(
        borderSize,
        titleHeight + borderSize,
        (int) ((width - borderSize * 2) * nameplate.getHealthPercentage()),
        plateHeight - borderSize * 2);

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();

    int textLineY =
        (int) (fontMetrics.getStringBounds(nameplate.getName(), graphics).getHeight() * 0.9f);
    if (nameplate.getName() != null
        && !nameplate.getName().isEmpty()
        && !nameplate.getName().equals("null")) {
      graphics.setColor(Color.WHITE);
      graphics.drawString(nameplate.getName(), 2, textLineY);
    }

    if (nameplate.getCombatLevel() > 0) {
      String combatLevelString = String.valueOf(nameplate.getCombatLevel());
      Rectangle2D combatLevelBounds = fontMetrics.getStringBounds(combatLevelString, graphics);

      graphics.setColor(
          getLevelColor(
              plugin.getClient().getLocalPlayer().getCombatLevel(), nameplate.getCombatLevel()));
      graphics.drawString(
          combatLevelString, (int) (width - combatLevelBounds.getWidth()), textLineY);
    }

    if (nameplate.getMaxHealth() > 0) {
      String currentHealthString = String.valueOf(nameplate.getCurrentHealth());
      String maxHealthString = String.valueOf(nameplate.getMaxHealth());
      String healthString = currentHealthString + " / " + maxHealthString;
      Rectangle2D bounds = fontMetrics.getStringBounds(healthString, graphics);

      graphics.setColor(Color.WHITE);
      graphics.drawString(
          healthString,
          width / 2 - (int) bounds.getWidth() / 2,
          (int) (titleHeight + plateHeight / 2f + bounds.getHeight() / 2));
    }
  }

  @Override
  protected void drawExternal(
      Graphics2D graphics,
      int width,
      int height,
      float scale,
      Nameplate nameplate,
      Point anchor,
      Actor actor) {
    if (!(actor instanceof Player)) {
      return;
    }

    NameplateHeadIcon overheadIcon = NameplateHeadIcon.get(((Player) actor).getOverheadIcon());
    if (overheadIcon == null) {
      return;
    }

    int rightX = anchor.getX() + width / 2;
    int topY = anchor.getY() - height;
    int plateHeight = getPlateHeight(graphics, scale, nameplate);

    BufferedImage overheadImage = overheadIcon.getImage();
    graphics.drawImage(
        overheadImage.getScaledInstance(
            plateHeight, plateHeight, Image.SCALE_SMOOTH), // TODO: optimise this
        rightX + (int) (6 * scale),
        topY + getTitleHeight(scale),
        plateHeight,
        plateHeight,
        null);
  }

  @Override
  protected int getHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
    if (nameplate.getMaxHealth() <= 0) {
      graphics.setFont(
          FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
      FontMetrics fontMetrics = graphics.getFontMetrics();

      return (int) fontMetrics.getStringBounds(nameplate.getName(), graphics).getHeight();
    }

    return (int) ((TITLE_HEIGHT + PLATE_HEIGHT) * scale);
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
