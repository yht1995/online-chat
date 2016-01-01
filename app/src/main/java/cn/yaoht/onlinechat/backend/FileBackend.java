package cn.yaoht.onlinechat.backend;

import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yaoht on 2016/1/1.
 * Project: OnlineChat
 */
public class FileBackend {
    /**
     * 将文件Base64编码
     *
     * @param file 编码文件
     * @return 文件的Base64编码
     * @throws IOException
     */
    public static String FileEncodeBase64(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        stream.read(bytes);
        stream.close();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 文件Base64解码并保存
     *
     * @param base64   Base64编码
     * @param filename 文件名
     * @return 文件绝对路径
     * @throws IOException
     */
    public static String FileDecodeBase64(String base64, String filename) throws IOException {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), filename);

        FileOutputStream stream = new FileOutputStream(file);
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        stream.write(bytes);
        stream.flush();
        stream.close();
        return file.getAbsolutePath();
    }
}
