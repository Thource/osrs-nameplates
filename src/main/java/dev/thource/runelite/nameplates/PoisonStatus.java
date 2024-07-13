package dev.thource.runelite.nameplates;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PoisonStatus {
  public static final int POISON_TICK_MILLIS = 18_200;
  private static final int VENOM_MAXIUMUM_DAMAGE = 20;
  private static final int VENOM_THRESHOLD = 1_000_000;
  private final int value;

  public boolean isVenom() {
    return value >= VENOM_THRESHOLD;
  }

  public int getDamage() {
    if (isVenom()) {
      // Venom damage starts at 6, and increments in twos;
      // The VarPlayer increments in values of 1, however.
      return Math.min(VENOM_MAXIUMUM_DAMAGE, (value - VENOM_THRESHOLD + 3) * 2);
    }

    return (int) Math.ceil(value / 5.0f);
  }
}
