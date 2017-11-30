package com.dragovorn.mda.manager;

import com.dragovorn.mda.Main;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.Reader;
import java.util.UUID;

import static com.dragovorn.mda.ReflectionHelper.injectStaticFinal;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileLockManager.class, Gson.class, Main.class, Bukkit.class})
public class TestFileLockManager {

    private FileLockManager manager;

    @Test
    public void testLockNoFile() {
        initManager(false);

        UUID uuid = UUID.randomUUID();

        OfflinePlayer offlineOwner = mock(OfflinePlayer.class);
        when(offlineOwner.getName()).thenReturn("owner");

        mockStatic(Bukkit.class);
        when(Bukkit.getOfflinePlayer(uuid)).thenReturn(offlineOwner);

        Player owner = mock(Player.class);
        when(owner.getUniqueId()).thenReturn(uuid);

        Block block = mock(Block.class);
        when(block.getX()).thenReturn(0);
        when(block.getY()).thenReturn(0);
        when(block.getZ()).thenReturn(0);

        this.manager.lock(owner, block);

        Player random = mock(Player.class);
        when(random.getUniqueId()).thenReturn(UUID.randomUUID());

        assertEquals(true, this.manager.isLocked(block));
        assertEquals(false, this.manager.lock(random, block));
        assertEquals("owner", this.manager.getWhoLocked(block).getName());
    }

    private void initManager(boolean exists) {
        suppress(method(FileLockManager.class, "save"));
        suppress(constructor(File.class, File.class, String.class));

        Main main = mock(Main.class);
        when(main.getDataFolder()).thenReturn(mock(File.class));

        mockStatic(Main.class);
        when(Main.getInstance()).thenReturn(main);

        File file = mock(File.class);
        when(file.exists()).thenReturn(exists);

        JsonObject object = new JsonObject();
        object.addProperty("key", "000");
        object.addProperty("owner", "fa2daf04-02e9-4fe2-a70c-b38db29afc47");

        JsonArray array = new JsonArray();

        Gson gson = mock(Gson.class);
        when(gson.fromJson(any(Reader.class), any())).thenReturn(array);

        injectStaticFinal(FileLockManager.class, "DATA", File.class, file);
        injectStaticFinal(FileLockManager.class, "GSON", Gson.class, gson);

        this.manager = new FileLockManager();
    }
}