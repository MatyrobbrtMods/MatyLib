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

package com.matyrobbrt.lib.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.matyrobbrt.lib.event.AddPackFindersEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DownloadingPackFinder;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.ResourcePackList;

import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.client.ClientModLoader;

@Mixin(ClientModLoader.class)
public abstract class ClientModLoaderMixin {

	@Inject(at = @At("TAIL"), method = "begin(Lnet/minecraft/client/Minecraft;Lnet/minecraft/resources/ResourcePackList;Lnet/minecraft/resources/IReloadableResourceManager;Lnet/minecraft/client/resources/DownloadingPackFinder;)V", remap = false)
	private static void matylib$addPackFinders(final Minecraft minecraft, final ResourcePackList defaultResourcePacks,
			final IReloadableResourceManager mcResourceManager, DownloadingPackFinder metadataSerializer,
			CallbackInfo ci) {
		ModLoader.get().postEvent(new AddPackFindersEvent(defaultResourcePacks::addPackFinder));
	}

}
