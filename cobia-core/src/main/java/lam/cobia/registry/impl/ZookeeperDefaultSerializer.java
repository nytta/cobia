package lam.cobia.registry.impl;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @description: ZookeeperDefaultSerializer
 * @author: linanmiao
 * @date: 2018/11/26 0:01
 * @version: 1.0
 */
public class ZookeeperDefaultSerializer implements ZkSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDefaultSerializer.class);

    private static final String CHARSET_NAME = "utf-8";
    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        try {
            return ((String)data).getBytes(CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("[buildZkSerializer] unsupport encoding " + CHARSET_NAME, e);
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("[buildZkSerializer] unsupport encoding " + CHARSET_NAME, e);
            return null;
        }
    }
}
