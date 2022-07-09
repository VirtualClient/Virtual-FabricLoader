/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.loader.impl.discovery;

import java.nio.file.Files;
import java.nio.file.Path;

public class SingleFileModCandidateFinder implements ModCandidateFinder {
	private final boolean requiresRemap;
	private final Path path;

	public SingleFileModCandidateFinder(Path path, boolean requiresRemap) {
		this.path = path;
		this.requiresRemap = requiresRemap;
	}

	@Override
	public void findCandidates(ModCandidateConsumer out) {
		if (!Files.exists(path)) {
			return;
		}

		if (Files.isDirectory(path)) {
			throw new RuntimeException(path + " is not a file!");
		}

		if (DirectoryModCandidateFinder.isValidFile(path)) {
			out.accept(path, requiresRemap);
		}
	}
}
