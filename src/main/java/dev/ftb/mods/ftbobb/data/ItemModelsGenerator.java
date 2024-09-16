package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModelsGenerator extends ItemModelProvider {
    public ItemModelsGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FTBOBB.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        String path = BlocksRegistry.PUMP.getKey().location().getPath();
        this.getBuilder(path).parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + path + "_on")));

        withExistingParent("tube", FTBOBB.id("block/tube_inv"));
        withExistingParent("jar", FTBOBB.id("block/jar"));
        withExistingParent("tempered_jar", FTBOBB.id("block/tempered_jar_none"));
    }
}
