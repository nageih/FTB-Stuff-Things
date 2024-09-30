package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.AutoProcessingBlock;
import dev.ftb.mods.ftbobb.blocks.PumpBlock;
import dev.ftb.mods.ftbobb.blocks.SluiceBlock;
import dev.ftb.mods.ftbobb.blocks.TemperedJarBlock;
import dev.ftb.mods.ftbobb.client.model.TubeModel;
import dev.ftb.mods.ftbobb.items.MeshType;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.temperature.Temperature;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.EnumMap;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class BlockStatesGenerators extends BlockStateProvider {
    public BlockStatesGenerators(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FTBOBB.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
        int[] dirsRot = {0, 180, 270, 90};

        for (DeferredBlock<SluiceBlock> block : BlocksRegistry.ALL_SLUICES) {
            String key = block.getKey().location().getPath();
            MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block.get());

            for (int d = 0; d < 4; d++) {
                builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/" + key + "_body"))).rotationY(dirsRot[d]).addModel().condition(HORIZONTAL_FACING, dirs[d]).condition(SluiceBlock.PART, SluiceBlock.Part.MAIN);
                builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/" + key + "_front"))).rotationY(dirsRot[d]).addModel().condition(HORIZONTAL_FACING, dirs[d]).condition(SluiceBlock.PART, SluiceBlock.Part.FUNNEL);

                for (MeshType type : MeshType.NON_EMPTY_VALUES) {
                    builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/" + type.getSerializedName() + "_mesh"))).rotationY(dirsRot[d]).addModel().condition(SluiceBlock.MESH, type).condition(HORIZONTAL_FACING, dirs[d]).condition(SluiceBlock.PART, SluiceBlock.Part.MAIN);
                }
            }
        }

        int[] dirsRot2 = new int[]{90, 270, 0, 180};
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(BlocksRegistry.PUMP.get());
        for (int d = 0; d < 4; d++) {
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_off"))).rotationY(dirsRot2[d]).addModel().condition(PumpBlock.ON_OFF, false).condition(HORIZONTAL_FACING, dirs[d]);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_on"))).rotationY(dirsRot2[d]).addModel().condition(PumpBlock.ON_OFF, true).condition(HORIZONTAL_FACING, dirs[d]);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_20"))).rotationY(dirsRot2[d]).addModel().condition(PumpBlock.ON_OFF, true).condition(HORIZONTAL_FACING, dirs[d]).condition(PumpBlock.PROGRESS, PumpBlock.Progress.TWENTY);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_40"))).rotationY(dirsRot2[d]).addModel().condition(PumpBlock.ON_OFF, true).condition(HORIZONTAL_FACING, dirs[d]).condition(PumpBlock.PROGRESS, PumpBlock.Progress.FORTY);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_60"))).rotationY(dirsRot2[d]).addModel().condition(PumpBlock.ON_OFF, true).condition(HORIZONTAL_FACING, dirs[d]).condition(PumpBlock.PROGRESS, PumpBlock.Progress.SIXTY);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_80"))).rotationY(dirsRot2[d]).addModel().condition(PumpBlock.ON_OFF, true).condition(HORIZONTAL_FACING, dirs[d]).condition(PumpBlock.PROGRESS, PumpBlock.Progress.EIGHTY);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_100"))).rotationY(dirsRot2[d]).addModel().condition(PumpBlock.ON_OFF, true).condition(HORIZONTAL_FACING, dirs[d]).condition(PumpBlock.PROGRESS, PumpBlock.Progress.HUNDRED);
        }

        TubeLoaderBuilder tlb = models().getBuilder("block/tube").customLoader(TubeLoaderBuilder::new);
        simpleBlock(BlocksRegistry.TUBE.get(), tlb.end());

        simpleBlock(BlocksRegistry.BLUE_MAGMA_BLOCK.get());
        simpleBlock(BlocksRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE.get());
        simpleBlock(BlocksRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE.get());
        simpleBlock(BlocksRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE.get());

        simpleBlock(BlocksRegistry.CAST_IRON_BLOCK.get());

        models().withExistingParent("block/tube_inv", modLoc("block/tube_base"));

        ModelFile jarModel = models().withExistingParent("block/jar", modLoc("block/jar_base"));
        simpleBlock(BlocksRegistry.JAR.get(), jarModel);

        getVariantBuilder(BlocksRegistry.TEMPERED_JAR.get()).forAllStates(state -> {
            Temperature temp = state.getValue(TemperedJarBlock.TEMPERATURE);
            return ConfiguredModel.builder().modelFile(
                    models().withExistingParent("tempered_jar_" + temp.getSerializedName(), modLoc("block/jar_base"))
                            .texture("cover", modLoc("block/cast_iron_jar_cover"))
                            .texture("glass_side", modLoc("block/jar_glass_side_" + temp.getSerializedName()))
                            .texture("glass_top", modLoc("block/jar_glass_tempered_top"))
                            .texture("glass_bottom", modLoc("block/jar_glass_bottom_" + temp.getSerializedName()))
            ).build();
        });

        EnumMap<Direction, IntIntPair> rots = Util.make(new EnumMap<>(Direction.class), map -> {
            map.put(Direction.UP, IntIntPair.of(180, 0));
            map.put(Direction.NORTH, IntIntPair.of(90, 180));
            map.put(Direction.SOUTH, IntIntPair.of(90, 0));
            map.put(Direction.WEST, IntIntPair.of(90, 90));
            map.put(Direction.EAST, IntIntPair.of(90, 270));
        });

        MultiPartBlockStateBuilder apBuilder = getMultipartBuilder(BlocksRegistry.AUTO_PROCESSING_BLOCK.get());
        apBuilder.part().modelFile(models().getExistingFile(modLoc("block/auto_processing_block"))).addModel();
        AutoProcessingBlock.CONN_PROPS.forEach((dir, prop) -> {
            apBuilder.part().modelFile(models().getExistingFile(modLoc("block/tube_base")))
                    .rotationX(rots.get(dir).firstInt())
                    .rotationY(rots.get(dir).secondInt())
                    .addModel()
                    .condition(prop, true);
        });
    }

    private static class TubeLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {
        public TubeLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(TubeModel.Loader.ID, parent, existingFileHelper, false);
        }
    }
}
