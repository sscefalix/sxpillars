package sscefalix.the.best.sxpillars.managers;

import org.bukkit.permissions.Permissible;

import java.util.List;

public class PermissionManager {
    public static boolean hasPermission(Permissible target, List<String> permissions) {
        boolean hasPermission = false;

        for (String perm : permissions) {
            if (target.hasPermission(perm)) {
                hasPermission = true;
                break;
            }
        }

        return hasPermission;
    }
}
