package com.cfets.ts.s.task;

import java.io.IOException;
import java.security.SecureRandom;

import java.util.Random;

public class DefaultTaskIdGenerator implements TaskIdGenerator {

	@Override
	public String createId() {
		return getBase64UUID();
	}

	/**
	 * 在安全性比较高的情况下使用SecureRandom
	 */
	public static final SecureRandom RANDOM_INSTANCE = new SecureRandom();

	/**
	 * Returns a Base64 encoded version of a Version 4.0 compatible UUID as
	 * defined here: http://www.ietf.org/rfc/rfc4122.txt
	 */
	public String getBase64UUID() {
		return getBase64UUID(RANDOM_INSTANCE);
	}

	/**
	 * Returns a Base64 encoded version of a Version 4.0 compatible UUID
	 * randomly initialized by the given {@link java.util.Random} instance as
	 * defined here: http://www.ietf.org/rfc/rfc4122.txt
	 */
	public String getBase64UUID(Random random) {
		final byte[] randomBytes = new byte[16];
		random.nextBytes(randomBytes);
		/*
		 * Set the version to version 4 (see
		 * http://www.ietf.org/rfc/rfc4122.txt) The randomly or pseudo-randomly
		 * generated version. The version number is in the most significant 4
		 * bits of the time stamp (bits 4 through 7 of the time_hi_and_version
		 * field).
		 */
		randomBytes[6] &= 0x0f; /*
								 * clear the 4 most significant bits for the
								 * version
								 */
		randomBytes[6] |= 0x40; /* set the version to 0100 / 0x40 */

		/*
		 * Set the variant: The high field of th clock sequence multiplexed with
		 * the variant. We set only the MSB of the variant
		 */
		randomBytes[8] &= 0x3f; /* clear the 2 most significant bits */
		randomBytes[8] |= 0x80; /* set the variant (MSB is set) */
		try {
			byte[] encoded = Base64.encodeBytesToBytes(randomBytes, 0, randomBytes.length, Base64.URL_SAFE);
			// we know the bytes are 16, and not a multi of 3, so remove the 2
			// padding chars that are added
			assert encoded[encoded.length - 1] == '=';
			assert encoded[encoded.length - 2] == '=';
			return new String(encoded, 0, encoded.length - 2, Base64.PREFERRED_ENCODING);
		} catch (IOException e) {
			throw new IllegalStateException("should not be thrown");
		}
	}

}
