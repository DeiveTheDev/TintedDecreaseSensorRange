package deivethedev.dsrmod.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntitySensor;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileEntitySensor.class)
public abstract class EntitySensorRangeMixin extends TileEntity {

	private int getSightRange(World world, double x, double y, double z, Direction facing) {
		if (facing == Direction.NONE) {
			return 0;
		} else {
			int range = 4;
			int blockInFront = world.getBlockId(MathHelper.round(x + (double)facing.getOffsetX()), MathHelper.round(y + (double)facing.getOffsetY()), MathHelper.round(z + (double)facing.getOffsetZ()));
			if (Blocks.hasTag(blockInFront, BlockTags.EXTENDS_MOTION_SENSOR_RANGE)) {
				range = 8;
			} else if (blockInFront == Blocks.GLASS_TINTED.id()) {
				range = 2;
			}

			for(int i = 1; i <= range; ++i) {
				int x1 = MathHelper.round(x + (double)(facing.getOffsetX() * i));
				int y1 = MathHelper.round(y + (double)(facing.getOffsetY() * i));
				int z1 = MathHelper.round(z + (double)(facing.getOffsetZ() * i));
				int id = world.getBlockId(x1, y1, z1);
				if (Blocks.solid[id] && !Blocks.hasTag(id, BlockTags.EXTENDS_MOTION_SENSOR_RANGE)) {
					return i - 1;
				}
			}

			return range;
		}
	}

}
