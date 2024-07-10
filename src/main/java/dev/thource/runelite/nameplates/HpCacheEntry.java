package dev.thource.runelite.nameplates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class HpCacheEntry {
  private final int npcIndex;
  @Setter private int healthScale;
  @Setter private int hp;
  @Setter private int lastUpdate;
}
