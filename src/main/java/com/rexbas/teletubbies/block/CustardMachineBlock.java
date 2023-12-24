package com.rexbas.teletubbies.block;

import com.rexbas.teletubbies.block.entity.CustardMachineBlockEntity;
import com.rexbas.teletubbies.block.entity.CustardMachineSlaveBlockEntity;
import com.rexbas.teletubbies.init.TeletubbiesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CustardMachineBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<CustardMachinePart> PART = EnumProperty.create("part", CustardMachinePart.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
		
	protected static final VoxelShape SMALLTOWER_AABB_NORTH = Shapes.or(
			box(7.0D, 0.0D, 5.0D, 13.0D, 3.0D, 11.0D), 
			box(8.0D, 3.0D, 6.0D, 12.0D, 10.0D, 10.0D), 
			box(7.0D, 10.0D, 5.0D, 13.0D, 11.0D, 11.0D), 
			box(8.0D, 11.0D, 6.0D, 12.0D, 13.0D, 10.0D),
			box(9.0D, 13.0D, 7.0D, 11.0D, 14.0D, 9.0D))
			.optimize();
	
	protected static final VoxelShape BIGTOWER_AABB_NORTH = Shapes.or(
			box(3.0D, 0.0D, 5.0D, 9.0D, 3.0D, 11.0D), 
			box(4.0D, 3.0D, 6.0D, 8.0D, 12.0D, 10.0D), 
			box(3.0D, 12.0D, 5.0D, 9.0D, 13.0D, 11.0D), 
			box(4.0D, 13.0D, 6.0D, 8.0D, 15.0D, 10.0D),
			box(5.0D, 15.0D, 7.0D, 7.0D, 16.0D, 9.0D),
			box(7.0D, 0.0D, 2.0D, 12.0D, 3.0D, 5.0D))
			.optimize();
	
	protected static final VoxelShape SMALLTOWER_AABB_EAST = TeletubbiesBlocks.voxelShapeRotateY(SMALLTOWER_AABB_NORTH, Math.toRadians(270));
	protected static final VoxelShape SMALLTOWER_AABB_SOUTH = TeletubbiesBlocks.voxelShapeRotateY(SMALLTOWER_AABB_NORTH, Math.toRadians(180));
	protected static final VoxelShape SMALLTOWER_AABB_WEST = TeletubbiesBlocks.voxelShapeRotateY(SMALLTOWER_AABB_NORTH, Math.toRadians(90));
	protected static final VoxelShape BIGTOWER_AABB_EAST = TeletubbiesBlocks.voxelShapeRotateY(BIGTOWER_AABB_NORTH, Math.toRadians(270));
	protected static final VoxelShape BIGTOWER_AABB_SOUTH = TeletubbiesBlocks.voxelShapeRotateY(BIGTOWER_AABB_NORTH, Math.toRadians(180));
	protected static final VoxelShape BIGTOWER_AABB_WEST = TeletubbiesBlocks.voxelShapeRotateY(BIGTOWER_AABB_NORTH, Math.toRadians(90));
		
	public CustardMachineBlock() {
		super(Properties.of()
				.mapColor(MapColor.METAL)
				.strength(3.0f, 5.0f));

		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, CustardMachinePart.BASE).setValue(WATERLOGGED, false).setValue(LIT, false));
	}
	
	@Override
    @Nullable
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return BlockPathTypes.BLOCKED;
    }
	
	@Override
	public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockPos tilePos = getBasePos(pos, state.getValue(PART), state.getValue(FACING));
		CustardMachineBlockEntity be = (CustardMachineBlockEntity) world.getBlockEntity(tilePos);

		if (!world.isClientSide && player instanceof ServerPlayer) {
			NetworkHooks.openScreen((ServerPlayer) player, be, tilePos);
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.getValue(PART) == CustardMachinePart.BASE || state.getValue(PART) == CustardMachinePart.BIGBASE || state.getValue(PART) == CustardMachinePart.SMALLBASE) {
			return Shapes.block();
		}
		if (state.getValue(PART) == CustardMachinePart.SMALL) {
			return switch (state.getValue(FACING)) {
				case EAST -> SMALLTOWER_AABB_EAST;
				case SOUTH -> SMALLTOWER_AABB_SOUTH;
				case WEST -> SMALLTOWER_AABB_WEST;
				default -> SMALLTOWER_AABB_NORTH;
			};
		}
		return switch (state.getValue(FACING)) {
			case EAST -> BIGTOWER_AABB_EAST;
			case SOUTH -> BIGTOWER_AABB_SOUTH;
			case WEST -> BIGTOWER_AABB_WEST;
			default -> BIGTOWER_AABB_NORTH;
		};
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (placer != null) {
			BlockPos smallbasePos = getSmallTowerBasePos(pos, placer.getDirection());
			BlockPos bigbasePos = getBigTowerBasePos(pos, placer.getDirection());
			BlockPos smallPos = getSmallTowerPos(pos, placer.getDirection());
			BlockPos bigPos = getBigTowerPos(pos, placer.getDirection());
		    FluidState smallFluid = world.getFluidState(smallPos);
		    FluidState bigFluid = world.getFluidState(bigPos);
			world.setBlockAndUpdate(smallbasePos, state.setValue(PART, CustardMachinePart.SMALLBASE).setValue(WATERLOGGED, false));
		    world.setBlockAndUpdate(bigbasePos, state.setValue(PART, CustardMachinePart.BIGBASE).setValue(WATERLOGGED, false));
		    world.setBlockAndUpdate(smallPos, state.setValue(PART, CustardMachinePart.SMALL).setValue(WATERLOGGED, smallFluid.getType() == Fluids.WATER));
		    world.setBlockAndUpdate(bigPos, state.setValue(PART, CustardMachinePart.BIG).setValue(WATERLOGGED, bigFluid.getType() == Fluids.WATER));
		}
	}
	
	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		Direction facing = state.getValue(FACING);
		
		BlockPos basePos = getBasePos(pos, state.getValue(PART), facing);
		BlockState subblockState = world.getBlockState(basePos);
		if (subblockState.getBlock() == this && !pos.equals(basePos)) {
			removePart(world, basePos);
		}
		
		BlockPos subblock = getSmallTowerBasePos(basePos, facing);
		subblockState = world.getBlockState(subblock);
		if (subblockState.getBlock() == this && !pos.equals(subblock)) {	
			removePart(world, subblock);
		}
		
		subblock = getBigTowerBasePos(basePos, facing);
		subblockState = world.getBlockState(subblock);
		if (subblockState.getBlock() == this && !pos.equals(subblock)) {
			removePart(world, subblock);
		}
		
		subblock = getSmallTowerPos(basePos, facing);
		subblockState = world.getBlockState(subblock);
		if (subblockState.getBlock() == this && !pos.equals(subblock)) {	
			removePart(world, subblock);
		}
		
		subblock = getBigTowerPos(basePos, facing);
		subblockState = world.getBlockState(subblock);
		if (subblockState.getBlock() == this && !pos.equals(subblock)) {		
			removePart(world, subblock);
		}		      
		return super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
		Direction facing = state.getValue(FACING);
		
		BlockPos basePos = getBasePos(pos, state.getValue(PART), facing);
		BlockState subblockState = world.getBlockState(basePos);
		if (subblockState.getBlock() == this && !pos.equals(basePos)) {		      
			removePart(world, basePos);
		}
		
		BlockPos subblock = getSmallTowerBasePos(basePos, facing);
		subblockState = world.getBlockState(subblock);
		if (subblockState.getBlock() == this && !pos.equals(subblock)) {		      
			removePart(world, subblock);
		}
		
		subblock = getBigTowerBasePos(basePos, facing);
		subblockState = world.getBlockState(subblock);
		if (subblockState.getBlock() == this && !pos.equals(subblock)) {		      
			removePart(world, subblock);
		}
		
		subblock = getSmallTowerPos(basePos, facing);
		subblockState = world.getBlockState(subblock);
		if (subblockState.getBlock() == this && !pos.equals(subblock)) {		      
			removePart(world, subblock);
		}
		
		subblock = getBigTowerPos(basePos, facing);
		subblockState = world.getBlockState(subblock);
		if (subblockState.getBlock() == this && !pos.equals(subblock)) {		      
			removePart(world, subblock);
		}
		super.onBlockExploded(state, world, pos, explosion);
    }

	private void removePart(Level world, BlockPos pos) {
		FluidState fluidState = world.getFluidState(pos);
	    if (fluidState.getType() == Fluids.WATER) {
			world.setBlock(pos, fluidState.createLegacyBlock(), 35); 
	    }
	    else {
	    	world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
	    }
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		BlockPos smallbasePos = getSmallTowerBasePos(pos, context.getHorizontalDirection());
		BlockPos bigbasePos = getBigTowerBasePos(pos, context.getHorizontalDirection());
		BlockPos smallPos = getSmallTowerPos(pos, context.getHorizontalDirection());
		BlockPos bigPos = getBigTowerPos(pos, context.getHorizontalDirection());
		if (pos.getY() < 255 &&
				smallbasePos.getY() < 255 && context.getLevel().getBlockState(smallbasePos).canBeReplaced(context) &&
				bigbasePos.getY() < 255 && context.getLevel().getBlockState(bigbasePos).canBeReplaced(context) &&
				smallPos.getY() < 255 && context.getLevel().getBlockState(smallPos).canBeReplaced(context) &&
				bigPos.getY() < 255 && context.getLevel().getBlockState(bigPos).canBeReplaced(context)) {
			
			return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(PART, CustardMachinePart.BASE).setValue(WATERLOGGED, false);
		}
		return null;
	}
	
	@Override
	public @NotNull FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, PART, WATERLOGGED, LIT);
	}
	
	public static boolean isUnderwater(Level world, BlockPos pos) {
		Direction facing = world.getBlockState(pos).getValue(FACING);
		BlockPos basePos = getBasePos(pos, world.getBlockState(pos).getValue(PART), facing);
		return TeletubbiesBlocks.isBlockSurrounded(world, basePos) &&
				TeletubbiesBlocks.isBlockSurrounded(world, getSmallTowerBasePos(basePos, facing)) &&
				TeletubbiesBlocks.isBlockSurrounded(world, getBigTowerBasePos(basePos, facing)) &&
				world.getBlockState(getSmallTowerPos(basePos, facing)).getValue(WATERLOGGED) &&
				world.getBlockState(getBigTowerPos(basePos, facing)).getValue(WATERLOGGED);
	}
	
	public boolean hasBlockEntity(BlockState state) {
		return (state.getValue(PART) == CustardMachinePart.BASE || state.getValue(PART) == CustardMachinePart.BIGBASE || state.getValue(PART) == CustardMachinePart.SMALLBASE);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		if (state.getValue(PART) == CustardMachinePart.BASE) {
			return new CustardMachineBlockEntity(pos, state);
		}
		else if (state.getValue(PART) == CustardMachinePart.BIGBASE) {
			return new CustardMachineSlaveBlockEntity(pos, state);
		}
		else if (state.getValue(PART) == CustardMachinePart.SMALLBASE) {
			return new CustardMachineSlaveBlockEntity(pos, state);
		}
		return null;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (((CustardMachineBlock) state.getBlock()).hasBlockEntity(state) && state.getBlock() != newState.getBlock() && state.getValue(PART) == CustardMachinePart.BASE) {
			IItemHandler inputHandler = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, world.getBlockEntity(pos), null);
			if (inputHandler != null) {
				for (int i = 0; i < inputHandler.getSlots(); i++) {
					popResource(world, pos, inputHandler.getStackInSlot(i));
				}
			}

			IItemHandler outputHandler = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, world.getBlockEntity(pos), Direction.DOWN);
			if (outputHandler != null) {
				for (int i = 0; i < outputHandler.getSlots(); i++) {
					popResource(world, pos, outputHandler.getStackInSlot(i));
				}
			}
			world.removeBlockEntity(pos);
		}
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
		return state.getValue(LIT) ? 6 : 0;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		if (state.getValue(PART) == CustardMachinePart.BASE) {
			return (w, blockPos, blockState, t) -> {
				if (t instanceof CustardMachineBlockEntity be) {
					be.commonTick();
				}
			};	
		}
		return null;
	}
	
	@Override
	public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	public static BlockPos getSmallTowerBasePos(BlockPos base, Direction facing) {
		return switch (facing) {
			case EAST -> base.south();
			case SOUTH -> base.west();
			case WEST -> base.north();
			default -> base.east();
		};
	}
	
	public static BlockPos getBigTowerBasePos(BlockPos base, Direction facing) {
		return switch (facing) {
			case EAST -> base.north();
			case SOUTH -> base.east();
			case WEST -> base.south();
			default -> base.west();
		};
	}
	
	public static BlockPos getSmallTowerPos(BlockPos base, Direction facing) {
		return switch (facing) {
			case EAST -> base.south().above();
			case SOUTH -> base.west().above();
			case WEST -> base.north().above();
			default -> base.east().above();
		};
	}
	
	public static BlockPos getBigTowerPos(BlockPos base, Direction facing) {
		return switch (facing) {
			case EAST -> base.north().above();
			case SOUTH -> base.east().above();
			case WEST -> base.south().above();
			default -> base.west().above();
		};
	}
	
	public static BlockPos getBasePos(BlockPos pos, CustardMachinePart part, Direction facing) {
		if (part == CustardMachinePart.BASE) return pos;
		return switch (facing) {
			case NORTH -> switch (part) {
				case BIG -> pos.east().below();
				case BIGBASE -> pos.east();
				case SMALL -> pos.west().below();
				case SMALLBASE -> pos.west();
				default -> null;
			};
			case EAST -> switch (part) {
				case BIG -> pos.south().below();
				case BIGBASE -> pos.south();
				case SMALL -> pos.north().below();
				case SMALLBASE -> pos.north();
				default -> null;
			};
			case SOUTH -> switch (part) {
				case BIG -> pos.west().below();
				case BIGBASE -> pos.west();
				case SMALL -> pos.east().below();
				case SMALLBASE -> pos.east();
				default -> null;
			};
			case WEST -> switch (part) {
				case BIG -> pos.north().below();
				case BIGBASE -> pos.north();
				case SMALL -> pos.south().below();
				case SMALLBASE -> pos.south();
				default -> null;
			};
			default -> null;
		};
	}
}