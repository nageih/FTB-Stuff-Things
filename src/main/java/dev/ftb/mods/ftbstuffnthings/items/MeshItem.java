package dev.ftb.mods.ftbstuffnthings.items;

import net.minecraft.world.item.Item;

public class MeshItem extends Item {
    public final MeshType mesh;

    public MeshItem(MeshType m) {
        super(new Properties().stacksTo(16));
        this.mesh = m;
    }
}
