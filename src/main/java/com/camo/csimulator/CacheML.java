package com.camo.csimulator;

import java.util.ArrayList;

public class CacheML {

	private final int _cacheLines1;
	private final int _blockSize1;
	private Block[] _blocks1;
	private int _hits1 = 0;
	private int _misses1 = 0;
	private int _reads1 = 0;
	private int _writes1 = 0;

	private final int _cacheLines2;
	private final int _blockSize2;
	private Block[] _blocks2;
	private int _hits2 = 0;
	private int _misses2 = 0;
	private int _reads2 = 0;
	private int _writes2 = 0;

	CacheML(int cacheLines, int blockSize) {
		this._blocks1 = new Block[cacheLines];
		this._blocks2 = new Block[cacheLines];
		this._cacheLines1 = cacheLines;
		this._cacheLines2 = cacheLines / 2;
		this._blockSize1 = blockSize;
		this._blockSize2 = blockSize;
		int i;
		for (i = 0; i < this._cacheLines1; i++) {
			this._blocks1[i] = new Block();
		}
		for (i = 0; i < this._cacheLines2; i++) {
			this._blocks2[i] = new Block();
		}
	}

	public Output getOutput() throws Exception{
		int i;
		String tag;
		ArrayList<ML> ml = new ArrayList<>();
		for (i = 0; i < this._cacheLines1; i++) {
			tag = null;
			if (this._blocks1[i].tag != null) {
				tag = this._blocks1[i].tag;
			}
			
			if (tag == null)tag = "EMPTY";
			if (this._blocks1[i].content != null)
				try {
					ml.add(new ML(2,i,tag,HelperCS.btoh(this._blocks1[i].content)));
				} catch (Exception e) {
					throw e;
				}
		}

		for (i = 0; i < this._cacheLines2; i++) {
			tag = null;
			if (this._blocks2[i].tag != null) {
				tag = this._blocks2[i].tag;
			}if (tag == null)tag = "EMPTY";
			if (this._blocks2[i].content != null)
				try {
					ml.add(new ML(1,i,tag,HelperCS.btoh(this._blocks2[i].content)));
				} catch (Exception e) {
					throw e;
				}
		}
		return new Output(ml,this._hits1,this._misses1,this._writes1,this._reads1,this._hits2,this._misses2,this._writes2,this._reads2);

	}

	void writeL1(String toWrite) {
		int n = toWrite.length();
		int w = HelperCS.whatPower(this._blockSize1); // block size = 2**w
		int p1 = HelperCS.whatPower(this._cacheLines1); // number of bits to identify cache line
		Block block = this._blocks1[Integer.parseInt(toWrite.substring(n - w - p1, n - w), 2)];
		if (block.valid == 1 && block.tag.contentEquals(toWrite.substring(0, n - w - p1))) {
			// System.out.println("address already exist with tag: "+st.substring(0,
			// n-w-p)+" cache line : "+Integer.parseInt(st.substring(n-w-p,n-w),2));
			this._writes1++;
			block.dirty = 1;
			this._hits1++;
		} else {
			this._misses1++;
			this._reads1++;
			this._writes1++;
			block.dirty = 1;
			block.valid = 1;
			block.tag = toWrite.substring(0, n - w - p1);
			block.content = toWrite.substring(0, n - w);
		}

	}

	void writeL2(String toWrite) {
		int n = toWrite.length();
		int w = HelperCS.whatPower(this._blockSize2); // block size = 2**w
		int p2 = HelperCS.whatPower(this._cacheLines2); // number of bits to identify cache line
		Block block = this._blocks2[Integer.parseInt(toWrite.substring(n - w - p2, n - w), 2)];
		if (block.valid == 1 && block.tag.contentEquals(toWrite.substring(0, n - w - p2))) {
			// System.out.println("address already exist with tag: "+st.substring(0,
			// n-w-p)+" cache line : "+Integer.parseInt(st.substring(n-w-p,n-w),2));
			this._writes2++;
			block.dirty = 1;
			this._hits2++;
		} else {
			this._misses2++;
			this._reads2++;
			this._writes2++;
			block.dirty = 1;
			block.valid = 1;
			block.tag = toWrite.substring(0, n - w - p2);
			block.content = toWrite.substring(0, n - w);
		}

	}

	void read(String toRead) {
		Block block;
		int n = toRead.length();
		int w = HelperCS.whatPower(this._blockSize1); // block size = 2**w
		int p1 = HelperCS.whatPower(this._cacheLines1); // number of bits to identify cache line
		int p2 = p1 - 1;
		block = this._blocks2[Integer.parseInt(toRead.substring(n - w - p2, n - w), 2)];

		if (block.valid == 1 && block.tag.contentEquals(toRead.substring(0, n - w - p2))) {
			// System.out.println("address found in cache
			// line:"+Integer.parseInt(st.substring(n-w-p,n-w),2)+" with tag:
			// "+st.substring(0, n-w-p));
			this._hits2++;
		} else {
			this._misses2++;
			this._reads2++;

			block = this._blocks1[Integer.parseInt(toRead.substring(n - w - p1, n - w), 2)];

			if (block.valid == 1 && block.tag.contentEquals(toRead.substring(0, n - w - p1))) {
				this._hits1++;
				this._blocks1[Integer.parseInt(toRead.substring(n - w - p1, n - w), 2)] = new Block();
				this.writeL2(toRead);

			} else {
				this._misses1++;
				this._reads1++;
				this.writeL1(toRead);

			}

		}

		return;

	}

	public static class Output{
		private ArrayList<ML> ml;
		private final int hits1;
		private final int misses1;
		private final int writes1;
		private final int reads1;
		private final int hits2;
		private final int misses2;
		private final int writes2;
		private final int reads2;
		public Output(ArrayList<ML> ml,int hits1,int misses1,int writes1,int reads1,int hits2,int misses2,int writes2,int reads2){
			this.ml = ml;
			this.hits1 = hits1;
			this.misses1 = misses1;
			this.writes1 = writes1;
			this.reads1 = reads1;
			this.hits2 = hits2;
			this.misses2 = misses2;
			this.writes2 = writes2;
			this.reads2 = reads2;
		}
		public int getReadsL1(){
			return this.reads1;

		}

		public int getHitsL1(){
			return hits1;
		}

		public int getWritesL1(){
			return this.writes1;
		}

		public int getMissesL1(){
			return this.misses1;
		}

		public ArrayList<ML> getML(){
			return this.ml;
		}

		public int getReadsL2(){
			return this.reads2;

		}
		public int getHitsL2(){
			return hits2;
		}

		public int getWritesL2(){
			return this.writes2;
		}

		public int getMissesL2(){
			return this.misses2;
		}

		public ArrayList<ML> getDM(){
			return this.ml;
		}
	}

	public static class ML{
		private final int _level;
		private final int _cacheLine;
		private final String _tag;
		private final String _content;

		public ML(int level,int cacheLine,String tag, String content){
			this._level = level;
			this._cacheLine = cacheLine;
			this._tag = tag;
			this._content = content;
		}

		public int getLevel(){
			return this._level;
		}

		public int getCacheLine(){
			return this._cacheLine;
		}

		public String getTag(){
			return this._tag;
		}

		public String getContent(){
			return this._content;
		}

	}
}
