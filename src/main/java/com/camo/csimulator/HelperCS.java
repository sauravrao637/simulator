package com.camo.csimulator;

public final class HelperCS {
    // To find what power of 2 is a
    public static int whatPower(int a) {
        int result = (int) (Math.log(a) / Math.log(2));
        return result;
    }

    public static String htob(String str, int x) throws Exceptions.HtoBException {
        int dec;
        str = str.toLowerCase();
        try {
            dec = Integer.parseUnsignedInt(str, 16);
        } catch (Exception e) {
            throw new Exceptions.HtoBException();
        }
        String bin = Integer.toBinaryString(dec);
        ;
        String st = "";
        for (int i = bin.length(); i < x; i++)
            st += "0";

        return st + bin;
    }

    public static String btoh(String str) throws Exceptions.BtoHException {
        int dec;
        try {
            dec = Integer.parseInt(str, 2);
        } catch (Exception e) {
            throw new Exceptions.BtoHException();
        }
        return Integer.toHexString(dec);
    }

    public static String[] getBinaryCacheFromHex(String[] cache, int dataSize)
            throws Exceptions.Invalidinput, Exceptions.HtoBException {
        int x = HelperCS.whatPower(dataSize);
        String[] temp = new String[cache.length];
        for (int i = 0; i < cache.length; i++) {
            String t = cache[i];
            if (t.charAt(0) == '#')
                temp[i] = t;
            else {
                String[] lA = t.split(" ");
                if (lA.length != 2)
                    throw new Exceptions.Invalidinput();
                StringBuilder s = new StringBuilder();
                s.append(lA[0]);
                s.append(" ");
                try {
                    s.append(HelperCS.htob(lA[1], x));
                } catch (Exceptions.HtoBException e) {
                    throw e;
                }
                temp[i] = s.toString();
            }
        }
        return temp;
    }

    public static boolean isInputValid(String[] cache, int dataSize) {

        int x = HelperCS.whatPower(dataSize);
        for (int i = 0; i < cache.length; i++) {
            String t = cache[i];
            if (t.charAt(0) != '#')
            {
                String[] lA = t.split(" ");
                if (lA.length != 2)
                    return false;
                StringBuilder s = new StringBuilder();
                s.append(lA[0]);
                s.append(" ");
                try {
                    s.append(Integer.parseInt(lA[1], 2));
                    if(lA[1].length() != x){
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }

        return true;
    }
}
