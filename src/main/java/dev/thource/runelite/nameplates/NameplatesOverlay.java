package dev.thource.runelite.nameplates;

import dev.thource.runelite.nameplates.themes.BaseTheme;
import dev.thource.runelite.nameplates.themes.Themes;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.stream.Stream;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class NameplatesOverlay extends Overlay {
  @Inject private Client client;
  @Inject private NameplatesPlugin plugin;
  private long lastRender;

  @Inject
  NameplatesOverlay() {
    setPosition(OverlayPosition.DYNAMIC);
    setLayer(OverlayLayer.ABOVE_SCENE);
    setPriority(PRIORITY_MED);

    lastRender = System.currentTimeMillis();
  }

  @Override
  public Dimension render(Graphics2D graphics) {
    long deltaMs = System.currentTimeMillis() - lastRender;

    LocalPoint cameraPoint = new LocalPoint(client.getCameraX(), client.getCameraY());

    WorldView topLevelWorldView = client.getTopLevelWorldView();
    Stream.of(topLevelWorldView.npcs(), topLevelWorldView.players())
        .flatMap(IndexedObjectSet::stream)
        .filter(this::shouldDrawFor)
        .sorted(
            Comparator.comparingInt(
                    (Actor actor) -> actor.getLocalLocation().distanceTo(cameraPoint))
                .reversed())
        .forEach(
            actor -> {
              Point point =
                  actor.getCanvasTextLocation(
                      graphics, " ", (int) (actor.getLogicalHeight() * 1.4f));
              if (point == null) {
                return;
              }

              Nameplate nameplate = plugin.getNameplateForActor(actor);
              if (nameplate == null) {
                return;
              }

              renderNameplate(
                  graphics,
                  nameplate,
                  point,
                  actor.getLocalLocation().distanceTo(cameraPoint),
                  actor);
              nameplate.getHpAnimationData().progressBy(deltaMs);
            });

    //    theme.drawNameplate(graphics, new NameplateInfo("Test 0.6x", 50, 100, 23, new
    // Point(400, 100), 0.6f));
    //    theme.drawNameplate(graphics, new NameplateInfo("Test 1x", 50, 100, 23, new Point(400,
    // 150), 1f));
    //    theme.drawNameplate(graphics, new NameplateInfo("Test 2x", 50, 100, 23, new Point(400,
    // 250), 2f));
    //    theme.drawNameplate(graphics, new NameplateInfo("Test 3.3x", 50, 100, 23, new
    // Point(400, 350), 3.3f));

    lastRender = System.currentTimeMillis();

    return null;
  }

  private void renderNameplate(
      Graphics2D graphics, Nameplate nameplate, Point point, int distance, Actor actor) {
    //        float scale = Math.min(Math.max(8f / (distance / 300f), 0.5f), 1);
    //        scale = Math.max(scale * ((float) Math.pow(client.get3dZoom(), 0.6f) / 50f), 1f);
    float scale = 1;

    getTheme(actor).drawNameplate(graphics, nameplate, point, scale, actor);
  }

  private BaseTheme getTheme(Actor actor) {
    if (actor.getCombatLevel() != 9) {
      return Themes.OTHER.getTheme();
    }

    return Themes.OTHER.getTheme();
  }

  public boolean shouldDrawFor(Actor actor) {
    //    if (npc.isDead()) {
    //      return false;
    //    }

    // Hovered NPC

    // NPCs targetted in the last x seconds

    //    // Targetted NPC
    //    if (client.getLocalPlayer().getInteracting() == npc) {
    //      return true;
    //    }

    //    // NPCs targetting you
    //    if (npc.getInteracting() == client.getLocalPlayer()) {
    //      return true;
    //    }

    return true;
  }
}
