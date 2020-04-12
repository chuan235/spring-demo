package top.gmfcj.util;

import java.io.IOException;

public interface Record {

    public void serialize(OutputArchive archive, String tag) throws IOException;

    public void deserialize(InputArchive archive, String tag) throws IOException;
}
