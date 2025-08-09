package us.drullk.createenderstorage.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonHeadBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.phys.Vec3;

public class EnderStorageScenes {

    public static void enderChest(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("mounted_ender_chest", "Ender Storage Compatibility");
        scene.configureBasePlate(1, 1, 7);
        scene.showBasePlate();

        Selection baseplate = util.select().fromTo(1, 0, 1, 7, 0, 7);

        BlockPos contraptionChestStart = new BlockPos(4, 1, 5);
        BlockPos receiverChestStart = new BlockPos(4, 1, 3);
        BlockPos contraptionChestDest = new BlockPos(6, 3, 5);
        BlockPos receiverChestDest = new BlockPos(6, 2, 2);
        Vec3 contraptionChestDelta = contraptionChestStart.subtract(contraptionChestDest).getCenter().subtract(0.5, 0.5, 0.5);
        Vec3 receiverChestDelta = receiverChestStart.subtract(receiverChestDest).getCenter().subtract(0.5, 0.5, 0.5);

        Selection contraptionChestBox = util.select().position(contraptionChestDest);
        Selection receiverChestBox = util.select().position(receiverChestDest);
        BlockPos piston = new BlockPos(7, 1, 5);
        BlockPos pistonHead = new BlockPos(7, 1, 6);

        ElementLink<WorldSectionElement> contraption = scene.world().showIndependentSection(contraptionChestBox, Direction.DOWN);
        scene.world().moveSection(contraption, contraptionChestDelta, 0);

        scene.idle(10);

        ElementLink<WorldSectionElement> receiverChest = scene.world().showIndependentSection(receiverChestBox, Direction.DOWN);
        scene.world().moveSection(receiverChest, receiverChestDelta, 0);

        scene.idle(10);

        detailChestFrequencies(scene, contraptionChestStart, receiverChestStart);

        scene.idle(60);

        scene.world().moveSection(contraption, contraptionChestDelta.scale(-1), 20);
        scene.world().moveSection(receiverChest, receiverChestDelta.scale(-1), 20);

        scene.idle(20);

        scene.scaleSceneView(0.6f);

        Selection contraptionFrame = util.select().fromTo(6, 1, 5, 6, 2, 6);
        Selection contraptionDrills = util.select().fromTo(5, 1, 5, 5, 2, 6);
        Selection contraptionBox = contraptionFrame.add(contraptionDrills);

        scene.world().showSectionAndMerge(util.select().fromTo(7, 1, 6, 9, 1, 6), Direction.DOWN, contraption);

        Selection anchor = util.select().fromTo(6, 0, 7, 7, 1, 8);
        scene.world().showSection(anchor.substract(baseplate).substract(contraptionBox), Direction.NORTH);

        scene.world().setBlock(piston, AllBlocks.MECHANICAL_PISTON.getDefaultState().setValue(MechanicalPistonBlock.STATE, MechanicalPistonBlock.PistonState.MOVING).setValue(MechanicalPistonBlock.FACING, Direction.WEST).setValue(MechanicalPistonBlock.AXIS_ALONG_FIRST_COORDINATE, false), false);
        scene.world().showSectionAndMerge(util.select().position(pistonHead), Direction.DOWN, contraption);
        scene.world().setBlock(pistonHead, AllBlocks.MECHANICAL_PISTON_HEAD.getDefaultState().setValue(MechanicalPistonHeadBlock.TYPE, PistonType.STICKY).setValue(MechanicalPistonHeadBlock.FACING, Direction.WEST), false);
        ElementLink<WorldSectionElement> pistonAnchor = scene.world().showIndependentSection(util.select().position(piston), Direction.DOWN);
        scene.world().moveSection(pistonAnchor, new Vec3(0, 0, 1), 0);

        scene.idle(10);

        Selection receiverLine = util.select().fromTo(1, 1, 2, 6, 2, 2).substract(receiverChestBox);

        scene.world().showSectionAndMerge(contraptionFrame, Direction.EAST, contraption);
        scene.world().showSection(receiverLine, Direction.EAST);

        scene.idle(10);

        scene.world().showSectionAndMerge(contraptionDrills, Direction.EAST, contraption);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(contraptionChestBox.getCenter())
                .text("This includes while on contraptions. The Ender Storage are accessible by contraption actors for both usage and collection.");

        scene.idle(40);

        Selection cobblestone = util.select().fromTo(2, 1, 5, 3, 2, 6);
        scene.world().showSection(cobblestone, Direction.UP);

        scene.idle(40);

        scene.world().setKineticSpeed(util.select().position(6, 0, 8), -32);
        scene.world().setKineticSpeed(util.select().fromTo(7, 1, 5, 7, 1, 8), 64);
        scene.world().setKineticSpeed(contraptionDrills, 32);
        scene.world().moveSection(contraption, new Vec3(-1, 0, 0), 10);

        scene.idle(10);

        scene.world().setKineticSpeed(contraptionDrills, -64);

        BlockPos c5 = util.grid().at(3, 1, 5);
        BlockPos c6 = util.grid().at(3, 1, 6);
        BlockPos c7 = util.grid().at(3, 2, 5);
        BlockPos c8 = util.grid().at(3, 2, 6);
        for(int i1 = 0; i1 < 10; ++i1) {
            scene.idle(2);
            scene.world().incrementBlockBreakingProgress(c5);
            scene.world().incrementBlockBreakingProgress(c6);
            scene.world().incrementBlockBreakingProgress(c7);
            scene.world().incrementBlockBreakingProgress(c8);
        }

        BlockPos entryBeltPos = util.grid().at(5, 1, 2);
        BlockPos exitBeltPos = util.grid().at(1, 1, 2);

        ItemStack cobbleStack = Blocks.COBBLESTONE.asItem().getDefaultInstance();

        scene.world().flapFunnel(exitBeltPos.above(), false);
        scene.world().createItemOnBelt(entryBeltPos, Direction.EAST, cobbleStack);

        scene.world().setKineticSpeed(contraptionDrills, 32);

        scene.world().moveSection(contraption, new Vec3(-1, 0, 0), 10);

        scene.idle(10);

        scene.world().setKineticSpeed(contraptionDrills, -64);

        BlockPos c1 = util.grid().at(2, 1, 5);
        BlockPos c2 = util.grid().at(2, 1, 6);
        BlockPos c3 = util.grid().at(2, 2, 5);
        BlockPos c4 = util.grid().at(2, 2, 6);
        for(int iteration = 0; iteration < 10; ++iteration) {
            scene.idle(2);
            scene.world().incrementBlockBreakingProgress(c1);
            scene.world().incrementBlockBreakingProgress(c2);
            scene.world().incrementBlockBreakingProgress(c3);
            scene.world().incrementBlockBreakingProgress(c4);

            if (iteration == 2) {
                scene.world().flapFunnel(exitBeltPos.above(), false);
                scene.world().createItemOnBelt(entryBeltPos, Direction.EAST, cobbleStack);
            }
        }

        scene.world().setKineticSpeed(contraptionDrills, 0);

        scene.world().setKineticSpeed(util.select().position(6, 0, 8), 16);
        scene.world().setKineticSpeed(util.select().fromTo(7, 1, 5, 7, 1, 8), -32);
        scene.world().moveSection(contraption, new Vec3(2, 0, 0), 40);

        scene.idle(2);

        scene.world().flapFunnel(exitBeltPos.above(), false);
        scene.world().createItemOnBelt(entryBeltPos, Direction.EAST, cobbleStack);

        for (int i = 0; i < 6; i++) {
            scene.idle(16);
            scene.world().flapFunnel(exitBeltPos.above(), false);
            scene.world().createItemOnBelt(entryBeltPos, Direction.EAST, cobbleStack);
        }

        scene.idle(65);
    }

    private static void detailChestFrequencies(SceneBuilder scene, BlockPos contraptionChestStart, BlockPos receiverChestStart) {
        Vec3 contraptionChestCenter = contraptionChestStart.getCenter().add(0, 0, -3 / 16f);
        Vec3 receiverChestCenter = receiverChestStart.getCenter().add(0, 0, 2 / 16f);

        scene.overlay().showText(120)
                .text("Ender Storage Chests of same dye-coded frequencies share the same inventory.")
                .placeNearTarget()
                .pointAt(receiverChestStart.getCenter());

        scene.idle(80);

        Vec3 redOffset = new Vec3(3 / 16f, 7 / 16f, 0);
        Vec3 greenOffset = new Vec3(0, 7 / 16f, 0);
        Vec3 blueOffset = new Vec3(-3 / 16f, 7 / 16f, 0);
        scene.idle(10);
        scene.overlay().showLine(PonderPalette.RED, contraptionChestCenter.add(redOffset), receiverChestCenter.add(redOffset), 60);
        scene.idle(10);
        scene.overlay().showLine(PonderPalette.GREEN, contraptionChestCenter.add(greenOffset), receiverChestCenter.add(greenOffset), 60);
        scene.idle(10);
        scene.overlay().showLine(PonderPalette.BLUE, contraptionChestCenter.add(blueOffset), receiverChestCenter.add(blueOffset), 60);
    }

}
