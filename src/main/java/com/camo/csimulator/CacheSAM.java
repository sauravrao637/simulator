package com.camo.csimulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CacheSAM {
    private final int _cacheLines;
    private final int _blockSize;
    private final int _sets;
    private int _hits = 0;
    private int _misses = 0;
    private int _reads = 0;
    private int _writes = 0;
    private final int w;// block size = 2**w
    private final int p;
    Queue<Block>[] _blocks;

    CacheSAM(int cacheLines, int blockSize, int sets) {
        this._blockSize = blockSize;
        this._cacheLines = cacheLines;
        this._sets = sets;
        this._blocks = new LinkedList[cacheLines / sets];
        w = HelperCS.whatPower(this._blockSize);
        p = HelperCS.whatPower(this._sets);
        for (int i = 0; i < this._cacheLines / sets; i++)
            this._blocks[i] = new LinkedList<Block>();
    }

    public void write(String toWrite) {
        int n = toWrite.length();

        Block blocksQ[] = new Block[this._blocks[Integer.parseInt(toWrite.substring(n - w - p, n - w), 2)].size()];
        this._blocks[Integer.parseInt(toWrite.substring(n - w - p, n - w), 2)].toArray(blocksQ);
        Block block;
        for (int i = 0; i < blocksQ.length; i++) {
            block = blocksQ[i];
            if (block == null)
                continue;
            if (block.valid == 1 && block.tag.contentEquals(toWrite.substring(0, n - w))) {
                this._writes++;
                block.dirty = 1;
                this._hits++;
                return;
            }
        }
        this._misses++;
        this._reads++;
        this._writes++;
        Block e = new Block();
        e.dirty = 1;
        e.tag = toWrite.substring(0, n - w);
        e.content = toWrite.substring(0, n - w);
        e.valid = 1;
        if (this._blocks[Integer.parseInt(toWrite.substring(n - w - p, n - w), 2)]
                .size() < (this._cacheLines / this._sets)) {
            this._blocks[Integer.parseInt(toWrite.substring(n - w - p, n - w), 2)].add(e);
        } else {
            this._blocks[Integer.parseInt(toWrite.substring(n - w - p, n - w), 2)].remove();
            this._blocks[Integer.parseInt(toWrite.substring(n - w - p, n - w), 2)].add(e);
        }

    }

    void read(String toRead) {
    }

    public Output getOutput() throws Exception {
        ArrayList<SAM> sam = new ArrayList<>();
        for (int i = 0; i < this._blocks.length; i++) {
            while (!this._blocks[i].isEmpty()) {
                Block block = this._blocks[i].poll();
                try {
                    if (block != null) {
                        sam.add(new SAM(i, block.tag, HelperCS.btoh(block.content)));
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return new Output(sam, this._hits, this._misses, this._writes, this._reads);
    }

    public static class Output {
        private ArrayList<SAM> sam;
        private final int hits;
        private final int misses;
        private final int writes;
        private final int reads;

        public Output(ArrayList<SAM> sam, int hits, int misses, int writes, int reads) {
            this.sam = sam;
            this.hits = hits;
            this.misses = misses;
            this.writes = writes;
            this.reads = reads;
        }

        public int getHits() {
            return hits;
        }

        public int getWrites() {
            return this.writes;
        }

        public int getMisses() {
            return this.misses;
        }

        public ArrayList<SAM> getSAM() {
            return this.sam;
        }

        public int getReads() {
            return this.reads;

        }
    }

    public static class SAM {
        private final int setNumber;
        private final String tag;
        private final String content;

        public SAM(int setNumber, String tag, String content) {
            this.setNumber = setNumber;
            this.content = content;
            this.tag = tag;
        }

        public int getSetNumber() {
            return this.setNumber;
        }

        public String getTag() {
            return this.tag;
        }

        public String getContent() {
            return this.content;
        }
    }
}
