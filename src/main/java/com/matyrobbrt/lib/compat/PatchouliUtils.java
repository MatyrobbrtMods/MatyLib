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

package com.matyrobbrt.lib.compat;

import com.matyrobbrt.lib.MatyLib;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliUtils {

	public static void openBookGUI(ServerPlayerEntity player, ResourceLocation id) {
		if (MatyLib.patchouliLoaded) {
			PatchouliAPI.get().openBookGUI(player, id);
		} else {
			player.sendMessage(new StringTextComponent(TextFormatting.RED + "Patchouli is missing! No manual present"),
					Util.NIL_UUID);
		}
	}

	public static void openBookEntry(ServerPlayerEntity player, ResourceLocation id, ResourceLocation entry, int page) {
		if (MatyLib.patchouliLoaded) {
			PatchouliAPI.get().openBookEntry(player, id, entry, page);
		} else {
			player.sendMessage(new StringTextComponent(TextFormatting.RED + "Patchouli is missing! No manual present"),
					Util.NIL_UUID);
		}
	}

}
