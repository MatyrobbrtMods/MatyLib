package com.matyrobbrt.lib.version;

import java.util.List;

import com.google.gson.annotations.Expose;

public class VersionList {

	@Expose
	public List<Version> latest_versions;

	public static final class Version {

		@Expose
		public String minecraftVersion;
		@Expose
		public String modVersion;
		@Expose
		public String downloadLink;
	}

}
