package dev.ftb.mods.ftbstuffnthings.data;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.blocks.*;
import dev.ftb.mods.ftbstuffnthings.blocks.cobblegen.CobblegenBlock;
import dev.ftb.mods.ftbstuffnthings.blocks.jar.JarAutomaterBlock;
import dev.ftb.mods.ftbstuffnthings.blocks.jar.TemperedJarBlock;
import dev.ftb.mods.ftbstuffnthings.blocks.pump.PumpBlock;
import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceBlock;
import dev.ftb.mods.ftbstuffnthings.client.model.TubeModel;
import dev.ftb.mods.ftbstuffnthings.items.MeshType;
import dev.ftb.mods.ftbstuffnthings.registry.BlocksRegistry;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class BlockStatesGenerators extends BlockStateProvider {
    public BlockStatesGenerators(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FTBStuffNThings.MODID, exFileHelper);
    }

    private static final List<DirRotation> HORIZONTALS = Util.make(new ArrayList<>(), l -> {
        l.add(new DirRotation(Direction.NORTH, 0));
        l.add(new DirRotation(Direction.EAST, 90));
        l.add(new DirRotation(Direction.SOUTH, 180));
        l.add(new DirRotation(Direction.WEST, 270));
    });

    @Override
    protected void registerStatesAndModels() {
        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
        int[] dirsRot = {0, 180, 270, 90};

        // Sluices
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

        // Pump
        int[] dirsRot2 = new int[]{90, 270, 0, 180};
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(BlocksRegistry.PUMP.get());
        for (int d = 0; d < 4; d++) {
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_off")))
                    .rotationY(dirsRot2[d]).addModel()
                    .condition(PumpBlock.ON_OFF, false)
                    .condition(HORIZONTAL_FACING, dirs[d]);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_on")))
                    .rotationY(dirsRot2[d]).addModel()
                    .condition(PumpBlock.ON_OFF, true)
                    .condition(HORIZONTAL_FACING, dirs[d]);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_20")))
                    .rotationY(dirsRot2[d]).addModel()
                    .condition(PumpBlock.ON_OFF, true)
                    .condition(HORIZONTAL_FACING, dirs[d])
                    .condition(PumpBlock.PROGRESS, PumpBlock.Progress.TWENTY);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_40")))
                    .rotationY(dirsRot2[d]).addModel()
                    .condition(PumpBlock.ON_OFF, true)
                    .condition(HORIZONTAL_FACING, dirs[d])
                    .condition(PumpBlock.PROGRESS, PumpBlock.Progress.FORTY);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_60")))
                    .rotationY(dirsRot2[d]).addModel()
                    .condition(PumpBlock.ON_OFF, true)
                    .condition(HORIZONTAL_FACING, dirs[d])
                    .condition(PumpBlock.PROGRESS, PumpBlock.Progress.SIXTY);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_80")))
                    .rotationY(dirsRot2[d]).addModel()
                    .condition(PumpBlock.ON_OFF, true)
                    .condition(HORIZONTAL_FACING, dirs[d])
                    .condition(PumpBlock.PROGRESS, PumpBlock.Progress.EIGHTY);
            builder.part().modelFile(this.models().getExistingFile(this.modLoc("block/pump_100")))
                    .rotationY(dirsRot2[d]).addModel()
                    .condition(PumpBlock.ON_OFF, true)
                    .condition(HORIZONTAL_FACING, dirs[d])
                    .condition(PumpBlock.PROGRESS, PumpBlock.Progress.HUNDRED);
        }

        // Auto Hammers
        for (var block : List.of(BlocksRegistry.IRON_AUTO_HAMMER, BlocksRegistry.GOLD_AUTO_HAMMER, BlocksRegistry.DIAMOND_AUTO_HAMMER, BlocksRegistry.NETHERITE_AUTO_HAMMER)) {
            MultiPartBlockStateBuilder b = getMultipartBuilder(block.get());
            String path = block.getId().getPath();
            for (DirRotation d : HORIZONTALS) {
                b.part().modelFile(models().getExistingFile(modLoc("block/" + path)))
                        .rotationY(d.rotation).addModel()
                        .condition(AbstractMachineBlock.ACTIVE, false)
                        .condition(HORIZONTAL_FACING, d.direction);
                b.part().modelFile(models().getExistingFile(modLoc("block/" + path + "_active")))
                        .rotationY(d.rotation).addModel()
                        .condition(AbstractMachineBlock.ACTIVE, true)
                        .condition(HORIZONTAL_FACING, d.direction);
            }
        }

        // Cobble generators
        Map<DeferredBlock<CobblegenBlock>, String> cobblegenModels = Map.of(
                BlocksRegistry.STONE_COBBLEGEN, "block/stone_cobblestone_generator",
                BlocksRegistry.IRON_COBBLEGEN, "block/iron_cobblestone_generator",
                BlocksRegistry.GOLD_COBBLEGEN, "block/gold_cobblestone_generator",
                BlocksRegistry.DIAMOND_COBBLEGEN, "block/diamond_cobblestone_generator",
                BlocksRegistry.NETHERITE_COBBLEGEN, "block/netherite_cobblestone_generator"
        );

        for (DeferredBlock<CobblegenBlock> block : cobblegenModels.keySet()) {
            String modelPath = cobblegenModels.get(block);
            ModelFile cobblegenModel = models().withExistingParent("block/cobblegen", modLoc(modelPath));
            simpleBlock(block.get(), cobblegenModel);
        }


        // Fusing Machine & Super Cooler
        for (var block: List.of(BlocksRegistry.FUSING_MACHINE, BlocksRegistry.SUPER_COOLER)) {
            var model = machineModel(block, false);
            var activeModel = machineModel(block, true);
            VariantBlockStateBuilder.PartialBlockstate machineBuilder = getVariantBuilder(block.get()).partialState();
            for (DirRotation d : HORIZONTALS) {
                machineBuilder.with(HORIZONTAL_FACING, d.direction).with(AbstractMachineBlock.ACTIVE, false)
                        .setModels(new ConfiguredModel(model, 0, d.rotation, false));
                machineBuilder.with(HORIZONTAL_FACING, d.direction).with(AbstractMachineBlock.ACTIVE, true)
                        .setModels(new ConfiguredModel(activeModel, 0, d.rotation, false));
            }
            simpleBlockItem(block.get(), model);
        }

        // Tubes
        TubeLoaderBuilder tlb = models().getBuilder("block/tube").customLoader(TubeLoaderBuilder::new);
        simpleBlock(BlocksRegistry.TUBE.get(), tlb.end());
        models().withExistingParent("block/tube_inv", modLoc("block/tube_base"));

        // Dripper
        ModelFile dripperModel = models().withExistingParent("block/dripper", modLoc("block/dripper_base"));
        simpleBlock(BlocksRegistry.DRIPPER.get(), dripperModel);

        // Jar
        ModelFile jarModel = models().withExistingParent("block/jar", modLoc("block/jar_base"));
        simpleBlock(BlocksRegistry.JAR.get(), jarModel);

        // Tempered Jar
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

        // Jar auto processing block
        EnumMap<Direction, IntIntPair> rots = Util.make(new EnumMap<>(Direction.class), map -> {
            map.put(Direction.UP, IntIntPair.of(180, 0));
            map.put(Direction.NORTH, IntIntPair.of(90, 180));
            map.put(Direction.SOUTH, IntIntPair.of(90, 0));
            map.put(Direction.WEST, IntIntPair.of(90, 90));
            map.put(Direction.EAST, IntIntPair.of(90, 270));
        });
        MultiPartBlockStateBuilder apBuilder = getMultipartBuilder(BlocksRegistry.JAR_AUTOMATER.get());
        apBuilder.part().modelFile(models().getExistingFile(modLoc("block/auto_processing_block"))).addModel();
        JarAutomaterBlock.CONN_PROPS.forEach((dir, prop) -> {
            apBuilder.part().modelFile(models().getExistingFile(modLoc("block/tube_base")))
                    .rotationX(rots.get(dir).firstInt())
                    .rotationY(rots.get(dir).secondInt())
                    .addModel()
                    .condition(prop, true);
        });


        // Misc simple blocks

        simpleBlock(BlocksRegistry.BLUE_MAGMA_BLOCK.get());
        simpleBlock(BlocksRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE.get());
        simpleBlock(BlocksRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE.get());
        simpleBlock(BlocksRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE.get());

        simpleBlock(BlocksRegistry.CAST_IRON_BLOCK.get());
    }

    private ModelFile machineModel(DeferredBlock<? extends Block> block, boolean active) {
        String name = block.getId().getPath();
        String suffix = active ? "_active" : "";
        return models().withExistingParent(name + suffix, "block/orientable")
                .texture("top", modLoc("block/" + name + "_top" + suffix))
                .texture("side", modLoc("block/generic_machine_side"))
                .texture("front", modLoc("block/" + name + "_front" + suffix));
    }

    private static class TubeLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {
        public TubeLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(TubeModel.Loader.ID, parent, existingFileHelper, false);
        }
    }

    private record DirRotation(Direction direction, int rotation) {
    }

}
