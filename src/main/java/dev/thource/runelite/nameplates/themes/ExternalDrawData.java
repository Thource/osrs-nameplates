package dev.thource.runelite.nameplates.themes;

import lombok.Getter;

@Getter
public class ExternalDrawData {
  private int leftOffset;
  private int rightOffset;

  public void addLeftOffset(int overheadSize) {
    leftOffset += overheadSize;
  }

  public void addRightOffset(int overheadSize) {
    rightOffset += overheadSize;
  }
}
