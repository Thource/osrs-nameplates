package dev.thource.runelite.nameplates;

import lombok.Getter;

@Getter
public class AnimationData {
  private float startValue;
  private float endValue;
  private long durationMs;
  private long progressMs;

  public void startAnimation(int startValue, int endValue, int durationMs) {
    if ((this.startValue > 0 || this.endValue > 0) && this.progressMs < this.durationMs) {
      float progress = (float) this.progressMs / this.durationMs;
      this.startValue = this.startValue + ((this.endValue - this.startValue) * progress);
    } else {
      this.startValue = startValue;
    }

    this.endValue = endValue;
    this.durationMs = durationMs;
    this.progressMs = 0;
  }

  public float getCurrentValue() {
    if (this.progressMs >= this.durationMs) {
      return this.endValue;
    }

    float progress = (float) this.progressMs / this.durationMs;
    return this.startValue + ((this.endValue - this.startValue) * progress);
  }

  public void progressBy(long deltaMs) {
    this.progressMs = Math.min(durationMs, this.progressMs + deltaMs);
  }
}
