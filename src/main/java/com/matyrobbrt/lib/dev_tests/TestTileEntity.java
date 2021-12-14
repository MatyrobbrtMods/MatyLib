/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Matyrobbrt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.matyrobbrt.lib.dev_tests;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.matyrobbrt.lib.annotation.SyncValue;
import com.matyrobbrt.lib.multiblock.IMultiblock;
import com.matyrobbrt.lib.multiblock.IMultiblockConnector;
import com.matyrobbrt.lib.multiblock.IMultiblockComponent;
import com.matyrobbrt.lib.multiblock.MultiblockDriver;
import com.matyrobbrt.lib.multiblock.wsd.MultiblockDriverWSD;
import com.matyrobbrt.lib.registry.annotation.AutoBlockItem;
import com.matyrobbrt.lib.registry.annotation.RegisterBlock;
import com.matyrobbrt.lib.registry.annotation.RegisterTileEntityType;
import com.matyrobbrt.lib.tile_entity.BaseTileEntity;
import com.matyrobbrt.lib.wrench.IWrenchBehaviour;
import com.matyrobbrt.lib.wrench.IWrenchUsable;
import com.matyrobbrt.lib.wrench.WrenchResult;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

//@RegistryHolder(modid = MatyLib.MOD_ID)
class TestTileEntity extends BaseTileEntity implements ITickableTileEntity, IMultiblockComponent {

	@AutoBlockItem
	@RegisterBlock("test")
	public static final TestBlock TEST_BLOCK = new TestBlock(AbstractBlock.Properties.copy(Blocks.IRON_BARS));

	public static final class TestBlock extends Block implements IWrenchUsable {

		public TestBlock(Properties properties) {
			super(properties);
		}

		@Override
		public boolean hasTileEntity(net.minecraft.block.BlockState state) {
			return true;
		}

		@Override
		public TileEntity createTileEntity(net.minecraft.block.BlockState state,
				net.minecraft.world.IBlockReader world) {
			return TE_TYPE.create();
		}

		@Override
		public IWrenchBehaviour getBehaviour() {
			return (stack, mode, player, state, pos, level) -> {
				System.out.println("yes");
				return WrenchResult.CONSUME;
			};
		}
	}

	@RegisterTileEntityType("test")
	public static final TileEntityType<TestTileEntity> TE_TYPE = TileEntityType.Builder
			.of(TestTileEntity::new, TEST_BLOCK).build(null);

	public TestTileEntity() {
		super(TE_TYPE);
	}

	@SyncValue(name = "whateverSync", onPacket = true)
	private int whatever;

	@Override
	public void tick() {
		if (level.isClientSide()) {
			whatever = 13;
			sync(com.matyrobbrt.lib.network.matylib.SyncValuesMessage.Direction.CLIENT_TO_SERVER);
		} else {
			TestWSD.getInstance((ServerWorld) level).getDriver().getHolder(0).addBlockPos(worldPosition);
			TestWSD.getInstance((ServerWorld) level).setDirty(true);
		}
	}

	public static final class TestWSD extends MultiblockDriverWSD<TestMultiblock> {

		public static final String ID = "test_multiblock";

		private TestWSD() {
			super(ID);
		}

		public static TestWSD getInstance(World level) {
			return getInstance((ServerWorld) level);
		}

		public static TestWSD getInstance(ServerWorld level) {
			return level.getDataStorage().computeIfAbsent(TestWSD::new, ID);
		}

		private final MultiblockDriver<TestMultiblock> driver = new MultiblockDriver.Builder<TestMultiblock>()
				.deserializer((tag, multiblock) -> {})
				.componentGetter((level, pos) -> level.getBlockEntity(pos) instanceof IMultiblockComponent
						? (IMultiblockComponent) level.getBlockEntity(pos)
						: null)
				.connector(new Merger()).build();

		@Override
		public MultiblockDriver<TestMultiblock> getDriver() { return driver; }

	}

	static final class Merger implements IMultiblockConnector<TestMultiblock> {

		@Override
		public void initialize(MultiblockDriver<TestMultiblock> driver, World level, TestMultiblock newMultiblock,
				int id) {
			driver.createOrUpdate(id, newMultiblock);
		}

		@Override
		public void merge(MultiblockDriver<TestMultiblock> driver, World level, TestMultiblock newMultiblock,
				TestMultiblock otherMultiblock) {
		}

		@Override
		public void distribute(MultiblockDriver<TestMultiblock> driver, World level, TestMultiblock original,
				List<Pair<Integer, Set<BlockPos>>> todo) {
		}

	}

	@Override
	public ResourceLocation getId() { return new ResourceLocation("name"); }

	@Override
	public int getMultiblockId() { return TestWSD.getInstance(level).getDriver().getIDForPos(worldPosition); }

	@Override
	public void setMultiblockId(int id) {
	}

	static final class TestMultiblock implements IMultiblock {
	}
}
