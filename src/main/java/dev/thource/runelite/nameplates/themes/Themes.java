package dev.thource.runelite.nameplates.themes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Themes {
  OSRS(new OsrsTheme()),
  DEFAULT(new DefaultTheme());

  private final BaseTheme theme;
}
