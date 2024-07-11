package dev.thource.runelite.nameplates;

import dev.thource.runelite.nameplates.themes.BaseTheme;
import dev.thource.runelite.nameplates.themes.Themes;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  private Map<LocalPoint, List<Actor>> getLocalPointActorMap() {
    HashMap<LocalPoint, List<Actor>> map = new HashMap<>();

    WorldView topLevelWorldView = client.getTopLevelWorldView();
    Stream.of(topLevelWorldView.players(), topLevelWorldView.npcs())
        .flatMap(IndexedObjectSet::stream)
        .filter(this::shouldDrawFor)
        .forEach(
            (actor) ->
                map.computeIfAbsent(actor.getLocalLocation(), (k) -> new ArrayList<>()).add(actor));

    return map;
  }

  @Override
  public Dimension render(Graphics2D graphics) {
    long deltaMs = System.currentTimeMillis() - lastRender;

    LocalPoint cameraPoint = new LocalPoint(client.getCameraX(), client.getCameraY());

    getLocalPointActorMap().entrySet().stream()
        .sorted(
            Comparator.comparingInt(
                    (Map.Entry<LocalPoint, List<Actor>> entry) ->
                        entry.getKey().distanceTo(cameraPoint))
                .reversed())
        .forEach(
            (entry) -> {
              int stackHeight = 0;
              List<Actor> actors = entry.getValue();
              int firstActorHeight = actors.get(0).getLogicalHeight();
              for (Actor actor : actors) {
                Point point = actor.getCanvasTextLocation(graphics, " ", firstActorHeight);
                if (point == null) {
                  return;
                }

                Nameplate nameplate = plugin.getNameplateForActor(actor);
                if (nameplate == null) {
                  return;
                }

                stackHeight +=
                    renderNameplate(
                            graphics,
                            nameplate,
                            new Point(point.getX(), point.getY() - 16 - stackHeight),
                            actor.getLocalLocation().distanceTo(cameraPoint),
                            actor)
                        + 4;
                nameplate.getHpAnimationData().progressBy(deltaMs);
              }
            });

    lastRender = System.currentTimeMillis();

    return null;
  }

  private int renderNameplate(
      Graphics2D graphics, Nameplate nameplate, Point point, int distance, Actor actor) {
    //        float scale = Math.min(Math.max(8f / (distance / 300f), 0.5f), 1);
    //        scale = Math.max(scale * ((float) Math.pow(client.get3dZoom(), 0.6f) / 50f), 1f);
    float scale = 1;

    BaseTheme theme = getTheme(actor);
    theme.drawNameplate(graphics, nameplate, point, scale);
    return theme.getHeight(graphics, scale, nameplate);
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
