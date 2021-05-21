package com.camo.csimulator;

import java.util.ArrayList;

public class CacheDM {
	private int cacheLines;
	private int blockSize;
	private Block[] blocks;
	private int hits = 0;
	private int misses = 0;
	private int reads = 0;
	private int writes = 0;
	private int w; // block size = 2**w
	private int p; // number of bits to identify this line

	CacheDM(int cacheLines, int blockSize) {
		int i;
		this.blockSize = blockSize;
		this.cacheLines = cacheLines;
		this.w = HelperCS.whatPower(this.blockSize);
		this.p = HelperCS.whatPower(this.cacheLines);
		
		this.cacheLines = cacheLines;
		this.blockSize = blockSize;
		this.blocks = new Block[this.cacheLines];
		for (i = 0; i < cacheLines; i++) {
			this.blocks[i] = new Block();
		}
	}

	public void read(String toRead) {
		int n = toRead.length();
		Block block = this.blocks[Integer.parseInt(toRead.substring(n - w - p, n - w), 2)];

		if (block.valid == 1 && block.tag.contentEquals(toRead.substring(0, n - w - p))) {
			this.hits++;
		} else {
			this.misses++;
			this.reads++;
			if (block.dirty == 1) {
				block.dirty = 0;
			}
			block.valid = 1;
			block.tag = toRead.substring(0, n - w - p);
			block.content = toRead.substring(0, n - w);
		}

		return;
	}

	public void write(String toWrite) {
		int n = toWrite.length();
		Block block = this.blocks[Integer.parseInt(toWrite.substring(n - w - p, n - w), 2)];
		if (block.valid == 1 && block.tag.contentEquals(toWrite.substring(0, n - w - p))) {
			this.writes++;
			block.dirty = 1;
			this.hits++;
		} else {
			this.misses++;
			this.reads++;
			this.writes++;
			block.dirty = 1;
			block.valid = 1;
			block.tag = toWrite.substring(0, n - w - p);
			block.content = toWrite.substring(0, n - w);
		}
		return;
	}

	public Output getOutput() throws Exception{

		int i;
		String tag;
		ArrayList<DM> dm = new ArrayList<>(); 
		
			for(i = 0; i < this.cacheLines; i++)
			{
				tag =null;
				if(this.blocks[i].tag != null)
				{
					tag = this.blocks[i].tag;
				}
				
				try{
					dm.add(new DM(i,tag,HelperCS.btoh(this.blocks[i].content)));
				}catch(Exception e){
					throw e;
				}
			}
			return new Output(dm,hits,misses,writes,reads);
		
	}

	public static class Output{
		private ArrayList<DM> dm;
		private final int hits;
		private final int misses;
		private final int writes;
		private final int reads;
		public Output(ArrayList<DM> dm,int hits,int misses,int writes,int reads){
			this.dm = dm;
			this.hits = hits;
			this.misses = misses;
			this.writes = writes;
			this.reads = reads;
		}

		public int getHits(){
			return hits;
		}

		public int getWrites(){
			return this.writes;
		}

		public int getMisses(){
			return this.misses;
		}

		public ArrayList<DM> getDM(){
			return this.dm;
		}

		public int getReads(){
			return this.reads;

		}
	}

	public static class DM{
		private final int cacheLine;
		private final String tag;
		private final String content;
		public DM(int cacheLine,String tag,String content){
			this.cacheLine = cacheLine;
			this.content = content;
			this.tag = tag;
		}

		public int getCacheLine(){
			return this.cacheLine;
		}

		public String getTag(){
			return this.tag;
		}

		public String getContent(){
			return this.content;
		}
	}

}
