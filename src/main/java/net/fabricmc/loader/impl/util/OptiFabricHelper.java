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

package net.fabricmc.loader.impl.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

import net.fabricmc.loader.impl.launch.FabricLauncherBase;

public class OptiFabricHelper {
	public static boolean copyJarToDest(Path dest) {
		try {
			Enumeration<URL> mods = FabricLauncherBase.getLauncher().getTargetClassLoader().getResources("optifabric.jar");

			if (mods.hasMoreElements()) {
				URL url = mods.nextElement();

				if (Files.exists(dest) && md5OfFile(Files.newInputStream(dest)).equals(md5OfFile(url.openStream()))) {
					return true;
				}

				Files.deleteIfExists(dest);
				Files.copy(url.openStream(), dest, StandardCopyOption.REPLACE_EXISTING);
				return true;
			}

			return false;
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String md5OfFile(InputStream is) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		BufferedInputStream bs = new BufferedInputStream(is);
		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = bs.read(buffer, 0, buffer.length)) != -1) {
			md.update(buffer, 0, bytesRead);
		}

		bs.close();
		byte[] digest = md.digest();

		StringBuilder sb = new StringBuilder();

		for (byte bite : digest) {
			sb.append(String.format("%02x", bite & 0xff));
		}

		return sb.toString();
	}
}
