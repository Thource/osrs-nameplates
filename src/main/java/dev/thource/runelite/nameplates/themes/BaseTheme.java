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

  protected abstract void drawOverlay(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected void drawDebugData(
      Graphics2D graphics,
      int width,
      int height,
      float scale,
      Nameplate nameplate,
      Point anchor,
      Actor actor) {
    int leftX = anchor.getX() - width / 2;
    int bottomY = anchor.getY() + height / 2;

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    graphics.setColor(Color.WHITE);
    graphics.drawString("hr: " + actor.getHealthRatio(), leftX + 2, bottomY + 10 * scale);
    graphics.drawString("hs: " + actor.getHealthScale(), leftX + 2, bottomY + 20 * scale);
    graphics.drawString("sc: " + scale, leftX + 2, bottomY + 30 * scale);
  }

  public void drawNameplate(
      Graphics2D graphics, Nameplate nameplate, Point anchor, float scale, Actor actor) {
    int width = getWidth(graphics, scale, nameplate);
    int height = getHeight(graphics, scale, nameplate);

    BufferedImage plate = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D plateGraphics = plate.createGraphics();
    drawBasePlate(plateGraphics, width, height, scale, nameplate);
    drawOverlay(plateGraphics, width, height, scale, nameplate);
    //        drawDebugData(graphics, width, height, scale, nameplate, anchor, actor);

    //        Composite oldComposite = graphics.getComposite();
    //        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
    graphics.drawImage(plate, anchor.getX() - width / 2, anchor.getY() - height / 2, null);
    //        graphics.setComposite(oldComposite);
  }

  protected int getWidth(Graphics2D graphics, float scale, Nameplate nameplate) {
    return (int) (120 * scale);
  }

  protected int getHeight(Graphics2D graphics, float scale, Nameplate nameplate) {
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
}
