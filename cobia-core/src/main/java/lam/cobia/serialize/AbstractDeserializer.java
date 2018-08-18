package lam.cobia.serialize;

import lam.cobia.serialize.support.CobiaDeserializer;

/**
 * @description: AbstractDeserializer
 * @author: linanmiao
 * @date: 2018/8/18 15:36
 * @version: 1.0
 */
public abstract class AbstractDeserializer implements CobiaDeserializer {

    protected final String name;

    protected AbstractDeserializer(String name) {
        this.name = name;
    }
}
