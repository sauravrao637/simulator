package com.camo.csimulator;

public class CS {
    private static final int TYPE_HEX = 0;
    private static final int TYPE_BINARY = 1;

    private String[] _input;
    private int _dataSize;
    private int _cacheLines;
    private int _blockSize;

    /**
     * 
     * @param dataSize
     * @param cacheLines
     * @param blockSize
     * @param cache
     * @param type
     * @throws Invalidinput
     * @throws HtoBException
     */
    public CS(int dataSize, int cacheLines, int blockSize, String[] input, int type) throws IllegalArgumentException {

        this._blockSize = blockSize;
        this._cacheLines = cacheLines;
        this._dataSize = dataSize;

        if (type == TYPE_HEX) {
            try {
                this._input = HelperCS.getBinaryCacheFromHex(input, dataSize);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not parse input");
            }
        } else if (type == TYPE_BINARY) {
            if (HelperCS.isInputValid(this._input, this._dataSize))
                this._input = input;
            else {
                throw new IllegalArgumentException("Could not parse input");
            }
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    public CacheDM.Output getDirectMappingOutput() {
        CacheDM cacheDM = new CacheDM(this._cacheLines, this._blockSize);
        for (String lfCPU : this._input) {
            if (lfCPU.length() != 0 && lfCPU.charAt(0) != '#') {
                String[] lineArray = lfCPU.split(" ");
                if (lineArray.length > 2) {
                    throw new IllegalArgumentException("invalid input :- " + lfCPU);
                }
                if (lineArray[0].contentEquals("R")) {
                    cacheDM.read(lineArray[1]);
                }
                if (lineArray[0].contentEquals("W")) {
                    cacheDM.write(lineArray[1]);
                }
            }
        }
        try {
            return cacheDM.getOutput();
        } catch (Exception e) {
            return null;
        }
    }

    public CacheAM.Output getAssociativeMappingOutput() {
        CacheAM cacheAM = new CacheAM(this._cacheLines, this._blockSize);
        for (String lfCPU : this._input) {
            if (lfCPU.length() != 0 && lfCPU.charAt(0) != '#') {
                String[] lineArray = lfCPU.split(" ");
                if (lineArray.length > 2) {
                    throw new IllegalArgumentException("invalid input :- " + lfCPU);
                }
                if (lineArray[0].contentEquals("R")) {

                    cacheAM.read(lineArray[1]);
                }
                if (lineArray[0].contentEquals("W")) {

                    cacheAM.write(lineArray[1]);
                }
            }
        }
        try {
            return cacheAM.getOutput();
        } catch (Exception e) {
            return null;
        }
    }

    public CacheSAM.Output getSetAssociativeMappingOutput(int sets) {
        if (sets <= 0) {
            return null;
        }
        CacheSAM cacheSAM = new CacheSAM(this._cacheLines, this._blockSize, sets);
        for (String lfCPU : this._input) {
            if (lfCPU.length() != 0 && lfCPU.charAt(0) != '#') {
                String[] lineArray = lfCPU.split(" ");

                if (lineArray[0].contentEquals("R")) {
                    cacheSAM.read(lineArray[1]);
                }
                if (lineArray[0].contentEquals("W")) {
                    cacheSAM.write(lineArray[1]);
                }
            }
        }
        try {
            return cacheSAM.getOutput();
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getInput() {
        return this._input;
    }

    public CacheML.Output getMLOutput() {
        CacheML cacheML = new CacheML(this._cacheLines, this._blockSize);
        for (String lfCPU : this._input) {
            if (lfCPU.length() != 0 && lfCPU.charAt(0) != '#') {
                String[] lineArray = lfCPU.split(" ");

                if (lineArray[0].contentEquals("R")) {
                    cacheML.read(lineArray[1]);
                }
                if (lineArray[0].contentEquals("W")) {
                    cacheML.read(lineArray[1]);
                }
            }
        }
        try {
            return cacheML.getOutput();
        } catch (Exception e) {
            return null;
        }
    }

}
