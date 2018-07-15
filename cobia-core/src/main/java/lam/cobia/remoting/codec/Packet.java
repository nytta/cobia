package lam.cobia.remoting.codec;
/**
* <p>
* tcp data stream packet
* </p>
* @author linanmiao
* @date 2018年4月27日
* @version 1.0
*/
public interface Packet {
	/**
	 * 魔数
	 */
	public int getMark();
    
	/**
	 * 数据长度
	 */
	public int getLength();
	
	/**
	 * 数据
	 */
	public byte[] getData();

}
