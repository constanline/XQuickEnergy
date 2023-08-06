package pansong291.xposed.quickenergy.core.common;

import static android.content.ContentValues.TAG;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;


/**
 * JSON工具类
 *
 * @author CLS
 * @since 2023/08/03
 */
public class JsonUtil {

    /**
     * 获取配置文件
     *
     * @return json
     */
    public static JSONObject getConfig() {
        try {
            String confJson = FileUtils.readFromFile(FileUtils.getConfigFile(FriendIdMap.currentUid));
            return new JSONObject(confJson);
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            return null;
        }
    }

    /**
     * 从输入流中获取指定节点的json数组
     *
     * @param is   输入流
     * @param node 获取的节点
     */
    public static JSONArray fileJsonArray(InputStream is, String node) {
        try {
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            reader.close();
            is.close();
            JSONObject object = new JSONObject(stringBuilder.toString());
            return new JSONArray(object.get(node).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
