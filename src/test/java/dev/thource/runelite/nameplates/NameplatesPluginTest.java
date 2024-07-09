package dev.thource.runelite.nameplates;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class NameplatesPluginTest {

  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws Exception {
    ExternalPluginManager.loadBuiltin(NameplatesPlugin.class);
    RuneLite.main(args);
  }
}
