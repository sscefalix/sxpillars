package sscefalix.the.best.sxpillars.managers.arena;

import java.util.List;

public record Arena(String name, int minPlayers, int maxPlayers, String world, boolean enabled, List<List<Integer>> locations) {
}
