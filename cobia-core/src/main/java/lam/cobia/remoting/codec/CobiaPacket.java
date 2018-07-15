package lam.cobia.remoting.codec;

import com.google.gson.Gson;

import lam.cobia.core.exception.CobiaException;

/**
* <p>
* define the data former of tcp:<br/>
* +------------+-----+-----+ <br/>
* |数据块的头部开始标记|数据长度 |数据内容 | <br/>
* +------------+-----+-----+ <br/>
* </p>
* @author linanmiao
* @date 2018年4月27日
* @version 1.0
*/
public class CobiaPacket implements Packet{
	/**
	 * 魔数
	 */
	private static final int COBIA_MARK = 0X76;//0Xcafebabe;
	
	/**
	 * 数据块的头部的开始标记
	 */
	private final int mark;
	/**
	 * 数据的长度
	 */
	private final int length;
	/**
	 * 数据的内容
	 */
	private final byte[] data;
	
	private final static byte[] EMPTY_DATA = new byte[0];
	
	private final static CobiaPacket EMPTY = new CobiaPacket(EMPTY_DATA);
	
	private static Gson gson = new Gson();
	
	private CobiaPacket(int mark, byte[] data) {
		if (data == null) {
			throw new NullPointerException("data is null");
		}
		this.mark = mark;
		this.length = data.length;
		this.data = data;
	}
	
	private CobiaPacket(byte[] data) {
		this(getDefaultMark(), data);
	}
	
	@Override
	public int getMark() {
		return mark;
	}
	
	@Override
	public int getLength() {
		return length;
	}
	
	@Override
	public byte[] getData() {
		return data;
	}
	
	public static CobiaPacket newPacket(byte[] data) {
		if (data == null) {
			throw new NullPointerException("data is null");
		}
		if (data.length == 0) {
			return EMPTY;
		}
		return new CobiaPacket(data);
	}
	
	public static byte[] newBytes(int length) {
		if (length < 0) {
			throw new CobiaException("length can not be negitive:" + length);
		}
		if (length == 0) {
			return EMPTY_DATA;
		}
		return new byte[length];
	}
	
	public static int getDefaultMark() {
		return COBIA_MARK;
	}
	
	@Override
	public String toString() {
		return gson.toJson(this);
	}

}
