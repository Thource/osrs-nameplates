package dev.thource.runelite.nameplates.themes;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import lombok.Setter;
import net.runelite.api.Actor;
import net.runelite.api.Point;
import net.runelite.client.ui.FontManager;

public abstract class BaseTheme {
  @Setter protected NameplatesPlugin plugin;

  protected abstract void drawBasePlate(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected abstract void drawName(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected abstract void drawCombatLevel(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected abstract void drawHealthBar(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected abstract void drawPrayerBar(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected void drawOverlay(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    if (shouldDrawName(nameplate.getName())) {
      drawName(graphics, width, height, scale, nameplate);
    }

    if (nameplate.getMaxHealth() > 0) {
      if (nameplate.getCombatLevel() > 0) {
        drawCombatLevel(graphics, width, height, scale, nameplate);
      }

      drawHealthBar(graphics, width, height, scale, nameplate);

      if (shouldDrawPrayerBar(nameplate.getActor())) {
        drawPrayerBar(graphics, width, height, scale, nameplate);
      }
    }
  }

  protected abstract void drawOverheads(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate, Point anchor);

  protected void drawDebugData(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate, Point anchor) {
    int leftX = anchor.getX() - width / 2;
    int bottomY = anchor.getY();

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    graphics.setColor(Color.WHITE);
    graphics.drawString(
        "hr: " + nameplate.getActor().getHealthRatio(), leftX + 2, bottomY + 10 * scale);
    graphics.drawString(
        "hs: " + nameplate.getActor().getHealthScale(), leftX + 2, bottomY + 20 * scale);
    graphics.drawString("sc: " + scale, leftX + 2, bottomY + 30 * scale);
  }

  protected void drawExternal(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate, Point anchor) {
    drawOverheads(graphics, width, height, scale, nameplate, anchor);
    // drawDebugData(graphics, width, height, scale, nameplate, anchor);
  }

  public void drawNameplate(Graphics2D graphics, Nameplate nameplate, Point anchor, float scale) {
    int width = getWidth(graphics, scale, nameplate);
    int height = getHeight(graphics, scale, nameplate);

    BufferedImage plate = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D plateGraphics = plate.createGraphics();
    drawBasePlate(plateGraphics, width, height, scale, nameplate);
    drawOverlay(plateGraphics, width, height, scale, nameplate);
    drawExternal(graphics, width, height, scale, nameplate, anchor);

    //        Composite oldComposite = graphics.getComposite();
    //        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
    graphics.drawImage(plate, anchor.getX() - width / 2, anchor.getY() - height, null);
    //        graphics.setComposite(oldComposite);
  }

  protected int getWidth(Graphics2D graphics, float scale, Nameplate nameplate) {
    return (int) (120 * scale);
  }

  public int getHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
    return (int) (20 * scale);
  }

  protected Color getLevelColor(int playerCombatLevel, int combatLevel) {
    int delta = combatLevel - playerCombatLevel;

    if (delta >= 10) {
      return new Color(0xff0000);
    } else if (delta >= 7) {
      return new Color(0xff3000);
    } else if (delta >= 4) {
      return new Color(0xff7000);
    } else if (delta >= 1) {
      return new Color(0xffb000);
    } else if (delta == 0) {
      return new Color(0xffff00);
    } else if (delta >= -3) {
      return new Color(0xc0ff00);
    } else if (delta >= -6) {
      return new Color(0x80ff00);
    } else if (delta >= -9) {
      return new Color(0x40ff00);
    } else {
      return new Color(0x00ff00);
    }
  }

  protected boolean shouldDrawName(String name) {
    return name != null && !name.isEmpty() && !name.equals("null");
  }

  protected boolean shouldDrawPrayerBar(Actor actor) {
    return plugin.getClient().getLocalPlayer() == actor && plugin.getConfig().drawPrayerBar();
  }
}
