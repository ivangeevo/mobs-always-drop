package ivangeevo.mobsalwaysdrop.util;

import net.fabricmc.loader.api.FabricLoader;

public interface SideModUtils {

    boolean isBTWRLoaded = FabricLoader.getInstance().isModLoaded("btwr");
}
