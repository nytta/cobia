package lam.cobia.core.model;

import lam.cobia.core.util.GsonUtil;

import java.util.Objects;

/**
 * @description: HostAndPort
 * @author: linanmiao
 * @date: 2018/7/21 23:44
 * @version: 1.0
 */
public class HostAndPort {

    private String host;

    private int port;

    public String getHost() {
        return host;
    }

    public HostAndPort setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public HostAndPort setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostAndPort that = (HostAndPort) o;
        return port == that.port &&
                Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {

        return Objects.hash(host, port);
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
