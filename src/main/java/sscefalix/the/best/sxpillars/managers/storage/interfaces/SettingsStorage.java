package sscefalix.the.best.sxpillars.managers.storage.interfaces;

import org.bukkit.Location;

public interface SettingsStorage {
    void setLobby(Location lobby);

    Location getLobby();
}
