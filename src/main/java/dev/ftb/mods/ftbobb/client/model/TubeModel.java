package dev.ftb.mods.ftbobb.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.tube.TubeBlockEntity;
import dev.ftb.mods.ftbobb.util.DirectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TubeModel extends BakedModelWrapper<BakedModel> {
    private static final Map<Integer, List<BakedQuad>> MODEL_CACHE = new ConcurrentHashMap<>();

    private final BakedModel[] rotated;

    public TubeModel(BakedModel centre, BakedModel[] rotated) {
        super(centre);
        this.rotated = rotated;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>(super.getQuads(state, side, rand, extraData, renderType));

        Integer connected = extraData.get(TubeBlockEntity.CONNECTION_PROPERTY);

        if (side == null && connected != null) {
            List<BakedQuad> cachedQuads = MODEL_CACHE.get(connected);
            if (cachedQuads == null) {
                cachedQuads = new ArrayList<>();
                for (Direction dir : DirectionUtil.VALUES) {
                    if (DirectionUtil.getDirectionBit(connected, dir)) {
                        cachedQuads.addAll(rotated[dir.get3DDataValue()].getQuads(state, null, rand, extraData, renderType));
                    }
                }
                MODEL_CACHE.put(connected, cachedQuads);
            }
            quads.addAll(cachedQuads);
        }

        return quads;
    }

    public record Geometry(BlockModel centre, BlockModel tubePart) implements IUnbakedGeometry<Geometry> {
        private static final Vector3f BLOCK_CENTER = new Vector3f(0.5f, 0.5f, 0.5f);

        // JSON models for the tube arm is in the DOWN orientation
        // rotate as appropriate to get a rotated model for each direction (DUNSWE order)
        private static final BlockModelRotation[] ROTATIONS = new BlockModelRotation[] {
                BlockModelRotation.X0_Y0,
                BlockModelRotation.X180_Y0,
                BlockModelRotation.X270_Y0,
                BlockModelRotation.X270_Y180,
                BlockModelRotation.X270_Y270,
                BlockModelRotation.X270_Y90
        };

        @Override
        public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> function, ModelState modelState, ItemOverrides itemOverrides) {
            BakedModel[] rotated = new BakedModel[6];

            for (Direction dir : DirectionUtil.VALUES) {
                int d = dir.get3DDataValue();
                ModelState rotatedState = UnbakedGeometryHelper.composeRootTransformIntoModelState(
                        modelState,
                        ROTATIONS[d].getRotation().applyOrigin(BLOCK_CENTER)
                );
                rotated[d] = tubePart.bake(modelBaker, tubePart, function, rotatedState, true);
            }

            return new TubeModel(centre.bake(modelBaker, centre, function, modelState, true), rotated);
        }
    }

    public enum Loader implements IGeometryLoader<Geometry> {
        INSTANCE;

        public static final ResourceLocation ID = FTBOBB.id("tube");

        @Override
        public Geometry read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            BlockModel centre = loadModel(FTBOBB.id("block/tube_center"));
            BlockModel tubePart = loadModel(FTBOBB.id("block/tube_base"));

            return new Geometry(centre, tubePart);
        }

        private static BlockModel loadModel(ResourceLocation location) {
            ResourceManager manager = Minecraft.getInstance().getResourceManager();
            ResourceLocation file = ModelBakery.MODEL_LISTER.idToFile(location);
            try (InputStream stream = manager.getResourceOrThrow(file).open()) {
                return net.minecraft.client.renderer.block.model.BlockModel.fromStream(new InputStreamReader(stream));
            } catch (IOException e) {
                throw new JsonParseException("Failed to load part model '" + file + "'", e);
            }
        }
    }
}
