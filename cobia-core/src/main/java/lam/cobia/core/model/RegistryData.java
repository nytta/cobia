package lam.cobia.core.model;

import lam.cobia.core.util.ParamConstant;

/**
 * @description: RegistryData
 * @author: linanmiao
 * @date: 2018/9/17 17:11
 * @version: 1.0
 */
public class RegistryData extends HostAndPort{

    private Integer weight = ParamConstant.WEIGHT_DEFAULT;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
