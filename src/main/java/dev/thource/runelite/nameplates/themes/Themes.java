package dev.thource.runelite.nameplates.themes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Themes {
  OSRS(new OsrsTheme()),
  OTHER(new OtherTheme());

  @Getter private final BaseTheme theme;
}
