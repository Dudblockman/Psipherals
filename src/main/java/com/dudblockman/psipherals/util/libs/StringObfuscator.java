/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Oct 30, 2016, 9:53:03 PM (GMT)]
 */
package com.dudblockman.psipherals.util.libs;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class StringObfuscator {

    public static boolean matchesHash(String str, String hash) {
        return getHash(str).equals(hash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String getHash(String str) {
        if (str != null)
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                //System.out.println(bytesToHex(md.digest(dontRainbowTableMeOrMySonEverAgain(str).getBytes(StandardCharsets.UTF_8))));
                return bytesToHex(md.digest(dontRainbowTableMeOrMySonEverAgain(str).getBytes()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        return "";
    }

    private static String dontRainbowTableMeOrMySonEverAgain(String str) throws NoSuchAlgorithmException {
        str += reverseString(str);
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
        rand.setSeed(str.getBytes(StandardCharsets.UTF_8));
        int l = str.length();
        int steps = rand.nextInt(l);
        char[] chrs = str.toCharArray();
        for (int i = 0; i < steps; i++) {
            int indA = rand.nextInt(l);
            int indB;
            do {
                indB = rand.nextInt(l);
            } while (indB == indA);
            char c = (char) (chrs[indA] ^ chrs[indB]);
            chrs[indA] = c;
        }

        return String.copyValueOf(chrs);
    }

    private static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

}