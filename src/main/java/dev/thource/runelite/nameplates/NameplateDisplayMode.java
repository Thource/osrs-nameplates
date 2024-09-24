package dev.thource.runelite.nameplates;

import net.runelite.api.Client;

public enum NameplateDisplayMode {
  ALWAYS,
  IN_COMBAT,
  IN_COMBAT_PERSONAL,
  NEVER;

  public boolean shouldDraw(Client client, Nameplate nameplate) {
    if (this == NameplateDisplayMode.ALWAYS) {
      return true;
    }

    if (this == NameplateDisplayMode.NEVER || nameplate == null) {
      return false;
    }

    if (this == NameplateDisplayMode.IN_COMBAT) {
      return nameplate.isInCombat(client);
    } else if (this == NameplateDisplayMode.IN_COMBAT_PERSONAL) {
      return nameplate.isInLocalCombat(client);
    }

    return false;
  }
}
