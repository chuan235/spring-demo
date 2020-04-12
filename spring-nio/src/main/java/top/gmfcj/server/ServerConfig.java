package top.gmfcj.server;

import top.gmfcj.constant.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class ServerConfig {

    protected InetSocketAddress clientPortAddress;
    protected InetSocketAddress serverPortAddress;
    protected int tickTime = Constants.DEFAULT_TICK_TIME;
    protected int maxClientCnxns = 60;
    protected int minSessionTimeout = -1;
    protected int maxSessionTimeout = -1;
    protected long serverId;
    protected int initLimit;

    public void parse(String[] args) {
        if (args.length < 2 || args.length > 4) {
            throw new IllegalArgumentException("Invalid number of arguments:" + Arrays.toString(args));
        }
        clientPortAddress = new InetSocketAddress(Integer.parseInt(args[0]));
        if (args.length >= 3) {
            tickTime = Integer.parseInt(args[2]);
        }
        if (args.length == 4) {
            maxClientCnxns = Integer.parseInt(args[3]);
        }
    }

    public void parse(String path) throws Exception {
        // 使用文件来读取配置信息
        File configFile = new File(path);
        if (!configFile.exists()) {
            throw new IllegalArgumentException(configFile.toString() + " 文件不存在");
        }
        // 通过流将文件load到内存中
        Properties cfg = new Properties();
        FileInputStream in = new FileInputStream(configFile);
        try {
            // 将文件中的信息封装到一个Properties对象中
            cfg.load(in);
        } finally {
            in.close();
        }
        // 将properties中的信息和配置类关联起来
        parseProperties(cfg);
    }

    public void parseProperties(Properties zkProp) throws Exception {
        for (Map.Entry<Object, Object> entry : zkProp.entrySet()) {
            String key = entry.getKey().toString().trim();
            String value = entry.getValue().toString().trim();
            if (key.equals("tickTime")) {
                tickTime = Integer.parseInt(value);
            }else if (key.equals("maxClientCnxns")) {
                maxClientCnxns = Integer.parseInt(value);
            } else if (key.equals("minSessionTimeout")) {
                minSessionTimeout = Integer.parseInt(value);
            } else if (key.equals("maxSessionTimeout")) {
                maxSessionTimeout = Integer.parseInt(value);
            } else if (key.equals("initLimit")) {
                initLimit = Integer.parseInt(value);
            }else if (key.startsWith("server") || key.startsWith("client")){
                String parts[] = value.split(":");
                String hostname = parts[0];
                Integer port = Integer.parseInt(parts[1]);
                // 192.168.0.144 8081
                if(key.startsWith("server")){
                    serverPortAddress = new InetSocketAddress(hostname, port);
                }else{
                    clientPortAddress = new InetSocketAddress(hostname, port);
                }
            } else if (key.equals("serverId")) {
                serverId = Integer.parseInt(value);
            }
        }
    }

    public InetSocketAddress getClientPortAddress() {
        return clientPortAddress;
    }

    public int getTickTime() {
        return tickTime;
    }

    public int getMaxClientCnxns() {
        return maxClientCnxns;
    }

    public int getMinSessionTimeout() {
        return minSessionTimeout;
    }

    public int getMaxSessionTimeout() {
        return maxSessionTimeout;
    }
    public InetSocketAddress getServerPortAddress() {
        return serverPortAddress;
    }
}
