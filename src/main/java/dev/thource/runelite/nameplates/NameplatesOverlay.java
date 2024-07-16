package dev.thource.runelite.nameplates;

import dev.thource.runelite.nameplates.themes.BaseTheme;
import dev.thource.runelite.nameplates.themes.Themes;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
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

    Player localPlayer = client.getLocalPlayer();
    map.computeIfAbsent(localPlayer.getLocalLocation(), (k) -> new ArrayList<>()).add(localPlayer);
    Stream.of(topLevelWorldView.players(), topLevelWorldView.npcs())
        .flatMap(IndexedObjectSet::stream)
        .filter(this::shouldDrawFor)
        .filter((actor) -> actor != localPlayer)
        .forEach(
            (actor) ->
                map.computeIfAbsent(actor.getLocalLocation(), (k) -> new ArrayList<>()).add(actor));

    return map;
  }

  @Override
  public Dimension render(Graphics2D graphics) {
    long deltaMs = System.currentTimeMillis() - lastRender;

    LocalPoint cameraPoint =
        new LocalPoint(client.getCameraX(), client.getCameraY(), client.getTopLevelWorldView());

    Actor finalHoveredActor = getHoveredActor();
    getLocalPointActorMap().entrySet().stream()
        .sorted(
            Comparator.comparingInt(
                    (Map.Entry<LocalPoint, List<Actor>> entry) ->
                        entry.getKey().distanceTo(cameraPoint))
                .reversed())
        .forEach(
            (entry) -> {
              List<Actor> actors = entry.getValue();

              // TODO: set stackHeight initial value back to 0 and remove the addition below when RL
              // addsupport for overriding overhead and skull sprites
              // This is just to make the nameplates show up higher than any potential skull or
              // overhead icons
              int stackHeight = actors.stream().anyMatch(a -> a instanceof Player) ? 28 : 0;
              if (actors.stream()
                  .filter(a -> a instanceof Player)
                  .anyMatch(a -> ((Player) a).getOverheadIcon() != null)) {
                stackHeight += 30;
              }

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
                            actor,
                            finalHoveredActor == actor)
                        + 4;
                nameplate.getHpAnimationData().progressBy(deltaMs);
              }
            });

    lastRender = System.currentTimeMillis();

    return null;
  }

  private Actor getHoveredActor() {
    MenuEntry[] menuEntries = client.getMenuEntries();
    if (menuEntries.length == 0) {
      return null;
    }

    HoverIndicatorMode hoverIndicatorMode = plugin.getConfig().hoverIndicatorMode();
    if ((hoverIndicatorMode == HoverIndicatorMode.RIGHT_CLICK
            || hoverIndicatorMode == HoverIndicatorMode.BUSY_RIGHT_CLICK)
        && !client.isMenuOpen()) {
      return null;
    }

    if (hoverIndicatorMode == HoverIndicatorMode.BUSY
        || hoverIndicatorMode == HoverIndicatorMode.BUSY_RIGHT_CLICK) {
      long uniqueActors =
          Arrays.stream(menuEntries)
              .map(MenuEntry::getActor)
              .filter(Objects::nonNull)
              .distinct()
              .count();

      if (uniqueActors <= 1) {
        return null;
      }
    }

    MenuEntry entry =
        client.isMenuOpen()
            ? getHoveredMenuEntry(menuEntries)
            : menuEntries[menuEntries.length - 1];
    MenuAction menuAction = entry.getType();
    switch (menuAction) {
      case WIDGET_TARGET_ON_NPC:
      case NPC_FIRST_OPTION:
      case NPC_SECOND_OPTION:
      case NPC_THIRD_OPTION:
      case NPC_FOURTH_OPTION:
      case NPC_FIFTH_OPTION:
      case EXAMINE_NPC:
      case WIDGET_TARGET_ON_PLAYER:
      case PLAYER_FIRST_OPTION:
      case PLAYER_SECOND_OPTION:
      case PLAYER_THIRD_OPTION:
      case PLAYER_FOURTH_OPTION:
      case PLAYER_FIFTH_OPTION:
      case PLAYER_SIXTH_OPTION:
      case PLAYER_SEVENTH_OPTION:
      case PLAYER_EIGHTH_OPTION:
      case RUNELITE_PLAYER:
        return entry.getActor();
    }

    return null;
  }

  private int renderNameplate(
      Graphics2D graphics,
      Nameplate nameplate,
      Point point,
      int distance,
      Actor actor,
      boolean isHovered) {
    //        float scale = Math.min(Math.max(8f / (distance / 300f), 0.5f), 1);
    //        scale = Math.max(scale * ((float) Math.pow(client.get3dZoom(), 0.6f) / 50f), 1f);
    float scale = 1;

    BaseTheme theme = getTheme(actor);
    theme.drawNameplate(graphics, nameplate, point, scale, isHovered);
    return theme.getHeight(graphics, scale, nameplate);
  }

  private BaseTheme getTheme(Actor actor) {
    return Themes.DEFAULT.getTheme();
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

  private MenuEntry getHoveredMenuEntry(final MenuEntry[] menuEntries) {
    final int menuX = client.getMenuX();
    final int menuY = client.getMenuY();
    final int menuWidth = client.getMenuWidth();
    final Point mousePosition = client.getMouseCanvasPosition();

    int dy = mousePosition.getY() - menuY;
    dy -= 19; // Height of Choose Option
    if (dy < 0) {
      return menuEntries[menuEntries.length - 1];
    }

    int idx = dy / 15; // Height of each menu option
    idx = menuEntries.length - 1 - idx;

    if (mousePosition.getX() > menuX
        && mousePosition.getX() < menuX + menuWidth
        && idx >= 0
        && idx < menuEntries.length) {
      return menuEntries[idx];
    }
    return menuEntries[menuEntries.length - 1];
  }
}
