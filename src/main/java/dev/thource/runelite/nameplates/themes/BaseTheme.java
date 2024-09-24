package dev.thource.runelite.nameplates.themes;

import dev.thource.runelite.nameplates.NPCNameplate;
import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesConfig;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.PoisonStatus;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.opponentinfo.HitpointsDisplayStyle;
import net.runelite.client.ui.FontManager;

public abstract class BaseTheme {
  protected NameplatesPlugin plugin;
  protected NameplatesConfig config;

  public void setPlugin(NameplatesPlugin plugin) {
    this.plugin = plugin;
    config = plugin.getConfig();
  }

  protected abstract void drawBasePlate(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected abstract void drawName(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected abstract void drawCombatLevel(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate);

  protected String getHealthString(Nameplate nameplate) {
    String healthString = nameplate.getCurrentHealth() + " / " + nameplate.getMaxHealth();
    if (nameplate instanceof NPCNameplate
        && ((NPCNameplate) nameplate).getPercentageHealthOverride() > 0) {
      healthString += "~";
    }

    boolean forcePercentage =
        (nameplate.getActor() instanceof Player
                && !config.lookupPlayerHp()
                && nameplate.getActor() != plugin.getClient().getLocalPlayer())
            || (nameplate instanceof NPCNameplate
                && ((NPCNameplate) nameplate).isPercentageHealth());

    HitpointsDisplayStyle displayStyle = config.hitpointsDisplayStyle();
    if (forcePercentage || displayStyle != HitpointsDisplayStyle.HITPOINTS) {
      double percentage =
          Math.ceil((float) nameplate.getCurrentHealth() / nameplate.getMaxHealth() * 1000f) / 10f;

      if (forcePercentage || displayStyle == HitpointsDisplayStyle.PERCENTAGE) {
        healthString = percentage + "%";
      } else {
        healthString += " (" + percentage + "%)";
      }
    }

    return healthString;
  }

  protected void drawHealthBar(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    int borderSize = getBorderSize(scale);
    int titleHeight = getTitleHeight(scale);
    int plateHeight = getPlateHeight(graphics, scale, nameplate);
    int barTopY = titleHeight + borderSize;
    int fullBarWidth = (width - borderSize * 2);
    int barWidth = (int) (fullBarWidth * Math.min(1, nameplate.getHealthPercentage()));
    int barHeight = plateHeight - borderSize * 2;
    boolean isLocalPlayer = nameplate.getActor() == plugin.getClient().getLocalPlayer();
    PoisonStatus poisonStatus = null;
    if (isLocalPlayer) {
      poisonStatus = plugin.getPoisonStatus();
    }

    drawBarBackground(graphics, borderSize, barTopY, fullBarWidth, barHeight);
    drawHealthBarBar(
        graphics, isLocalPlayer, poisonStatus, borderSize, barTopY, barWidth, barHeight, nameplate);

    if (isLocalPlayer && poisonStatus != null) {
      drawHealthBarPoisonChange(
          graphics,
          nameplate,
          poisonStatus,
          fullBarWidth,
          borderSize,
          barWidth,
          barTopY,
          barHeight);
    }

    StatChange hpChange = null;
    if (isLocalPlayer && config.drawConsumableHealAmount()) {
      hpChange = plugin.getHoveredItemHpChange();
    }
    if (hpChange != null && hpChange.getRelative() != 0) {
      drawHealthBarConsumableChange(
          graphics, nameplate, hpChange, fullBarWidth, borderSize, barWidth, barTopY, barHeight);
    }

    if (config.drawHpRegenIndicator() && isLocalPlayer) {
      drawHealthBarRegenIndicator(
          graphics, nameplate, borderSize, fullBarWidth, barTopY, barHeight);
    }

    if (config.drawPoisonDamageIndicator() && isLocalPlayer && poisonStatus != null) {
      drawHealthBarPoisonIndicator(graphics, borderSize, fullBarWidth, barTopY, barHeight);
    }

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();

    if (hpChange != null && hpChange.getRelative() != 0) {
      drawConsumableChangeText(
          graphics, width, hpChange, fontMetrics, borderSize, barTopY, barHeight);
    }

    drawHealthBarText(
        graphics, width, scale, nameplate, fontMetrics, borderSize, barTopY, barHeight);
  }

  protected abstract void drawBarBackground(
      Graphics2D graphics, int barLeftX, int barTopY, int fullBarWidth, int barHeight);

  protected int getBorderSize(double scale) {
    return 0;
  }

  protected abstract void drawPrayerBarText(
      Graphics2D graphics,
      int width,
      int currentPrayer,
      int maxPrayer,
      FontMetrics fontMetrics,
      int barTopY,
      int barHeight);

  protected abstract void drawPrayerBarFlickIndicator(
      Graphics2D graphics, int borderSize, int fullBarWidth, int barTopY, int barHeight);

  protected abstract void drawPrayerBarBar(
      Graphics2D graphics, int borderSize, int barTopY, int barWidth, int barHeight);

  protected abstract int getTitleHeight(float scale);

  protected abstract int getPlateHeight(Graphics2D graphics, float scale, Nameplate nameplate);

  protected abstract void drawHealthBarBar(
      Graphics2D graphics,
      boolean isLocalPlayer,
      PoisonStatus poisonStatus,
      int borderSize,
      int barTopY,
      int barWidth,
      int barHeight,
      Nameplate nameplate);

  protected abstract void drawHealthBarText(
      Graphics2D graphics,
      int width,
      float scale,
      Nameplate nameplate,
      FontMetrics fontMetrics,
      int borderSize,
      int barTopY,
      int barHeight);

  protected abstract void drawConsumableChangeText(
      Graphics2D graphics,
      int width,
      StatChange hpChange,
      FontMetrics fontMetrics,
      int borderSize,
      int barTopY,
      int barHeight);

  protected abstract void drawHealthBarPoisonIndicator(
      Graphics2D graphics, int borderSize, int fullBarWidth, int barTopY, int barHeight);

  protected abstract void drawHealthBarRegenIndicator(
      Graphics2D graphics,
      Nameplate nameplate,
      int borderSize,
      int fullBarWidth,
      int barTopY,
      int barHeight);

  protected abstract void drawHealthBarConsumableChange(
      Graphics2D graphics,
      Nameplate nameplate,
      StatChange change,
      int fullBarWidth,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight);

  protected abstract void drawHealthBarPoisonChange(
      Graphics2D graphics,
      Nameplate nameplate,
      PoisonStatus poisonStatus,
      int fullBarWidth,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight);

  protected void drawPrayerBar(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    int borderSize = getBorderSize(scale);
    int titleHeight = getTitleHeight(scale);
    int plateHeight = getPlateHeight(graphics, scale, nameplate);
    int currentPrayer = plugin.getClient().getBoostedSkillLevel(Skill.PRAYER);
    int maxPrayer = plugin.getClient().getRealSkillLevel(Skill.PRAYER);
    float prayerPercentage = (float) currentPrayer / maxPrayer;
    int barTopY = titleHeight + plateHeight + borderSize;
    int fullBarWidth = width - borderSize * 2;
    int barWidth = (int) (fullBarWidth * Math.min(1, prayerPercentage));
    int barHeight = plateHeight - borderSize * 2;

    drawBarBackground(graphics, borderSize, barTopY, fullBarWidth, barHeight);
    drawPrayerBarBar(graphics, borderSize, barTopY, barWidth, barHeight);

    StatChange prayerChange = null;
    if (config.drawConsumableHealAmount()) {
      prayerChange = plugin.getHoveredItemPrayerChange();
    }

    if (prayerChange != null && prayerChange.getRelative() != 0) {
      drawPrayerBarConsumableChange(
          graphics,
          fullBarWidth,
          prayerChange,
          maxPrayer,
          borderSize,
          barWidth,
          barTopY,
          barHeight);
    }

    if (config.drawPrayerFlickIndicator() && plugin.isAnyPrayerActive()) {
      drawPrayerBarFlickIndicator(graphics, borderSize, fullBarWidth, barTopY, barHeight);
    }

    graphics.setFont(FontManager.getRunescapeSmallFont().deriveFont((float) Math.ceil(16 * scale)));
    FontMetrics fontMetrics = graphics.getFontMetrics();

    if (prayerChange != null && prayerChange.getRelative() != 0) {
      drawConsumableChangeText(
          graphics, width, prayerChange, fontMetrics, borderSize, barTopY, barHeight);
    }

    drawPrayerBarText(graphics, width, currentPrayer, maxPrayer, fontMetrics, barTopY, barHeight);
  }

  protected abstract void drawPrayerBarConsumableChange(
      Graphics2D graphics,
      int fullBarWidth,
      StatChange prayerChange,
      int maxPrayer,
      int borderSize,
      int barWidth,
      int barTopY,
      int barHeight);

  protected void drawOverlay(
      Graphics2D graphics, int width, int height, float scale, Nameplate nameplate) {
    if (shouldDrawName(nameplate.getName())) {
      drawName(graphics, width, height, scale, nameplate);

      if (nameplate.getCombatLevel() > 0) {
        drawCombatLevel(graphics, width, height, scale, nameplate);
      }
    }

    if (shouldDrawBars(nameplate)) {
      drawHealthBar(graphics, width, height, scale, nameplate);

      if (shouldDrawPrayerBar(nameplate.getActor())) {
        drawPrayerBar(graphics, width, height, scale, nameplate);
      }
    }
  }

  protected abstract void drawOverheads(
      Graphics2D graphics,
      int width,
      int height,
      float scale,
      Nameplate nameplate,
      Point anchor,
      ExternalDrawData externalDrawData);

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
      Graphics2D graphics,
      int width,
      int height,
      float scale,
      Nameplate nameplate,
      Point anchor,
      boolean isHovered) {
    ExternalDrawData externalDrawData = new ExternalDrawData();

    // TODO: Remove this conditional when the RL team add NPC.getOverheadIcon()
    if (nameplate.getActor() instanceof Player) {
      drawOverheads(graphics, width, height, scale, nameplate, anchor, externalDrawData);
    }

    if (nameplate.getActor() instanceof Player) {
      drawSkullIcon(graphics, width, height, scale, nameplate, anchor, externalDrawData);
    }

    if (nameplate.getActor() instanceof NPC && ((NPCNameplate) nameplate).isNoLoot()) {
      drawNoLootIcon(graphics, width, height, scale, nameplate, anchor, externalDrawData);
    }

    if (isHovered) {
      drawHoverIndicator(graphics, width, height, scale, nameplate, anchor, externalDrawData);
    }

    // drawDebugData(graphics, width, height, scale, nameplate, anchor);
  }

  protected abstract void drawNoLootIcon(
      Graphics2D graphics,
      int width,
      int height,
      float scale,
      Nameplate nameplate,
      Point anchor,
      ExternalDrawData externalDrawData);

  protected abstract void drawSkullIcon(
      Graphics2D graphics,
      int width,
      int height,
      float scale,
      Nameplate nameplate,
      Point anchor,
      ExternalDrawData externalDrawData);

  protected abstract void drawHoverIndicator(
      Graphics2D graphics,
      int width,
      int height,
      float scale,
      Nameplate nameplate,
      Point anchor,
      ExternalDrawData externalDrawData);

  public void drawNameplate(
      Graphics2D graphics, Nameplate nameplate, Point anchor, float scale, boolean isHovered) {
    int width = getWidth(graphics, scale, nameplate);
    int height = getHeight(graphics, scale, nameplate);
    if (width <= 0 || height <= 0) {
      return;
    }

    BufferedImage plate = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D plateGraphics = plate.createGraphics();
    drawBasePlate(plateGraphics, width, height, scale, nameplate);
    drawOverlay(plateGraphics, width, height, scale, nameplate);
    plateGraphics.dispose();
    drawExternal(graphics, width, height, scale, nameplate, anchor, isHovered);

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

  protected boolean shouldDrawBars(Nameplate nameplate) {
    if (nameplate.getMaxHealth() <= 0) {
      return false;
    }

    return plugin.shouldDrawFor(nameplate.getActor());
  }

  protected boolean shouldDrawPrayerBar(Actor actor) {
    return plugin.getClient().getLocalPlayer() == actor && config.drawPrayerBar();
  }
}
