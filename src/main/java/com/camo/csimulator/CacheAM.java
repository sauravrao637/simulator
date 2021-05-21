package com.camo.csimulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CacheAM {
    private int _hits = 0;
    private int _misses = 0;
    private int _reads = 0;
    private int _writes = 0;
    private int _cacheLines;
    private int _blockSize;
    private Queue<Block> _blocks;
    private int w; // block size = 2**w

    public CacheAM(int cacheLines, int blockSize) {
        this._blocks = new LinkedList<>();
        this._cacheLines = cacheLines;
        this._blockSize = blockSize;
        w = HelperCS.whatPower(this._blockSize);
    }

    void read(String st) {
        Block block;
        int n = st.length();
        Block blocks[] = new Block[this._blocks.size()];
        this._blocks.toArray(blocks);
        for (int i = 0; i < blocks.length; i++) {
            block = blocks[i];
            if (block.valid == 1 && block.tag.contentEquals(st.substring(0, n - w))) {
                this._hits++;
                return;
            }
        }
        this._misses++;
        this._reads++;
        write(st);
        return;

    }

    void write(String st) {

        int n = st.length();
        
        Block blocks[] = new Block[this._blocks.size()];
        this._blocks.toArray(blocks);
        Block block;
        for (int i = 0; i < blocks.length; i++) {
            block = blocks[i];
            if (block == null)
                continue;
            if (block.valid == 1 && block.tag.contentEquals(st.substring(0, n - w))) {
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
        e.tag = st.substring(0, n - w);
        e.content = st.substring(0, n - w);
        e.valid = 1;

        if (this._blocks.size() < this._cacheLines) {
            this._blocks.add(e);
        } else {
            this._blocks.remove();
            this._blocks.add(e);
        }
    }

    public Output getOutput() throws Exception{
        ArrayList<AM> am = new ArrayList<>();
        while (!this._blocks.isEmpty()) {
            Block block = this._blocks.poll();
            if (block != null) {
                
                try{
                    am.add(new AM(block.tag,HelperCS.btoh(block.content)));
                }catch(Exception e){
                    throw e;
                }
            }
        }
        return new Output(this._hits, this._misses, this._reads,this._writes, am);
    }
    
    public static class Output{
        private final int _hits ;
        private final int _misses ;
        private final int _reads ;
        private final int _writes ;
        private final ArrayList<AM> _am;

        public Output(int hits, int misses,int reads,int writes,ArrayList<AM> am){
            this._am = am;
            this._hits = hits;
            this._misses = misses;
            this._reads = reads;
            this._writes = writes;
        }

        public int getHits(){
            return this._hits;
        }

        public int getMisses(){
            return this._misses;
        }

        public int getWrites(){
            return this._writes;
        }

        public int getReads(){
            return this._reads;
        }

        public ArrayList<AM> getAM(){
            return this._am;
        }
    }

    public static class AM{
        private String _tag;
        private String _address_of_block;

        public AM(String tag,String address_of_block){
            this._address_of_block = address_of_block;
            this._tag = tag;
        }

        public String getTag(){
            return this._tag;
        }

        public String getAddressOfBlock(){
            return this._address_of_block;
        }
    }

}
