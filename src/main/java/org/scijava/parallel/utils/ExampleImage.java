package org.scijava.parallel.utils;

import static cz.it4i.common.FileRoutines.getSuffix;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import cz.it4i.parallel.SciJavaParallelRuntimeException;
import lombok.extern.slf4j.Slf4j;

/**
 * Helper class for testing image downloading
 * 
 * @author koz01
 */
@Slf4j
public final class ExampleImage
{

	private ExampleImage() {
		// it is utility class where are used only static member
	}

	public static Path lenaAsTempFile()
	{
		return downloadToTmpFile( "https://upload.wikimedia.org/wikipedia/en/7/7d/Lenna_%28test_image%29.png" );
	}

	private static Path downloadToTmpFile(String url) {
		try (InputStream is = new URL(url).openStream()) {
			final File tempFile = File.createTempFile( "tmp", getSuffix(url) );
			tempFile.deleteOnExit();
			Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			return tempFile.toPath();
		}
		catch (IOException exc) {
			log.error("download image", exc);
			throw new SciJavaParallelRuntimeException(exc);
		}
	}
}
