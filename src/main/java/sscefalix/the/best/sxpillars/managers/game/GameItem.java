package sscefalix.the.best.sxpillars.managers.game;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameItem {
    public static final List<Material> exceptions = List.of(
            Material.BARRIER,
            Material.COMMAND_BLOCK,
            Material.COMMAND_BLOCK_MINECART,
            Material.CHAIN_COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            Material.STRUCTURE_VOID,
            Material.STRUCTURE_BLOCK,
            Material.DEBUG_STICK,
            Material.FIREWORK_ROCKET,
            Material.ENDER_PEARL,
            Material.BEDROCK,
            Material.RESPAWN_ANCHOR,
            Material.END_CRYSTAL,
            Material.ENCHANTED_BOOK,
            Material.ENDER_EYE,
            Material.END_PORTAL_FRAME,
            Material.END_PORTAL,
            Material.NETHER_PORTAL
    );

    public static ItemStack getRandomItem() {
        List<Material> materials = Arrays.stream(Material.values()).filter(material -> !exceptions.contains(material) && !material.isAir()).toList();

        Random random = new Random();

        Material randomMaterial = materials.get(random.nextInt(materials.size()));

        return new ItemStack(randomMaterial);
    }
}
