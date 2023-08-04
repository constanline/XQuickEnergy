package pansong291.xposed.quickenergy.util;

import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    private static final String TAG = FileUtils.class.getCanonicalName();
    private static File mainDirectory;
    private static File configDirectory;
    private static final Map<String,File> configFileMap = new HashMap<>();
    private static File friendIdMapFile;
    private static File cooperationIdMapFile;
    private static File reserveIdMapFile;
    private static File cityCodeMapFile;
    private static File statisticsFile;
    private static File exportedStatisticsFile;
    private static File forestLogFile;
    private static File farmLogFile;
    private static File otherLogFile;
    private static File simpleLogFile;
    private static File runtimeLogFile;

    @SuppressWarnings("deprecation")
    public static File getMainDirectoryFile() {
        if(mainDirectory == null) {
            mainDirectory = new File(Environment.getExternalStorageDirectory(), "xqe");
            if(mainDirectory.exists()) {
                if(mainDirectory.isFile())
                {
                    mainDirectory.delete();
                    mainDirectory.mkdirs();
                }
            } else {
                mainDirectory.mkdirs();
            }
        }
        return mainDirectory;
    }
    
    @SuppressWarnings("deprecation")
    public static File getConfigDirectoryFile() {
        if (configDirectory == null) {
            configDirectory = new File(Environment.getExternalStorageDirectory(), "xqe/config");
            if (configDirectory.exists()) {
                if (configDirectory.isFile()) {
                    configDirectory.delete();
                    configDirectory.mkdirs();
                }
            } else {
                configDirectory.mkdirs();
            }
        }
        return configDirectory;
    }

    public static File getConfigFile() {
        return getConfigFile(null);
    }

    public static File getConfigFile(String userId) {
        if (!configFileMap.containsKey("Default")) {
            File configFile = new File(getMainDirectoryFile(), "config.json");
            configFileMap.put("Default", configFile);
        }
        if (!StringUtil.isEmpty(userId)) {
            if (!configFileMap.containsKey(userId)) {
                File configFile = new File(getConfigDirectoryFile(), "config-" + userId + ".json");
                if (configFile.exists()) {
                    configFileMap.put(userId, configFile);
                    return configFile;
                }
            } else {
                return configFileMap.get(userId);
            }
        }
        return configFileMap.get("Default");
    }

    public static File getFriendIdMapFile() {
        if (friendIdMapFile == null) {
            friendIdMapFile = new File(getMainDirectoryFile(), "friendId.list");
            if (friendIdMapFile.exists() && friendIdMapFile.isDirectory())
                friendIdMapFile.delete();
        }
        return friendIdMapFile;
    }

    public static File getCooperationIdMapFile() {
        if (cooperationIdMapFile == null) {
            cooperationIdMapFile = new File(getMainDirectoryFile(), "cooperationId.list");
            if (cooperationIdMapFile.exists() && cooperationIdMapFile.isDirectory())
                cooperationIdMapFile.delete();
        }
        return cooperationIdMapFile;
    }

    public static File getReserveIdMapFile() {
        if (reserveIdMapFile == null) {
            reserveIdMapFile = new File(getMainDirectoryFile(), "reserveId.list");
            if (reserveIdMapFile.exists() && reserveIdMapFile.isDirectory())
                reserveIdMapFile.delete();
        }
        return reserveIdMapFile;
    }

    public static File getCityCodeMapFile() {
        if (cityCodeMapFile == null) {
            cityCodeMapFile = new File(getMainDirectoryFile(), "cityCode.list");
            if (cityCodeMapFile.exists() && cityCodeMapFile.isDirectory())
                cityCodeMapFile.delete();
        }
        return cityCodeMapFile;
      }

    public static File getStatisticsFile() {
        if (statisticsFile == null) {
            statisticsFile = new File(getMainDirectoryFile(), "statistics.json");
            if (statisticsFile.exists() && statisticsFile.isDirectory())
                statisticsFile.delete();
        }
        return statisticsFile;
    }

    public static File getExportedStatisticsFile() {
        if (exportedStatisticsFile == null) {
            exportedStatisticsFile = new File(getMainDirectoryFile(), "statistics.json");
            if (exportedStatisticsFile.exists() && exportedStatisticsFile.isDirectory())
                exportedStatisticsFile.delete();
        }
        return exportedStatisticsFile;
    }

    public static File getForestLogFile() {
        if (forestLogFile == null) {
            forestLogFile = new File(getMainDirectoryFile(), "forest.log");
            if (forestLogFile.exists() && forestLogFile.isDirectory())
                forestLogFile.delete();
            if (!forestLogFile.exists())
                try {
                    forestLogFile.createNewFile();
                } catch (Throwable ignored) {
                }
        }
        return forestLogFile;
    }

    public static File getFarmLogFile() {
        if (farmLogFile == null) {
            farmLogFile = new File(getMainDirectoryFile(), "farm.log");
            if (farmLogFile.exists() && farmLogFile.isDirectory())
                farmLogFile.delete();
            if (!farmLogFile.exists())
                try {
                    farmLogFile.createNewFile();
                } catch (Throwable ignored) {
                }
        }
        return farmLogFile;
    }

    public static File getOtherLogFile() {
        if (otherLogFile == null) {
            otherLogFile = new File(getMainDirectoryFile(), "other.log");
            if (otherLogFile.exists() && otherLogFile.isDirectory())
                otherLogFile.delete();
            if (!otherLogFile.exists())
                try {
                    otherLogFile.createNewFile();
                } catch (Throwable ignored) {
                }
        }
        return otherLogFile;
    }

    public static File getSimpleLogFile() {
        if (simpleLogFile == null) {
            simpleLogFile = new File(getMainDirectoryFile(), "simple.log");
            if (simpleLogFile.exists() && simpleLogFile.isDirectory())
                simpleLogFile.delete();
        }
        return simpleLogFile;
    }

    public static File getRuntimeLogFile() {
        if (runtimeLogFile == null) {
            runtimeLogFile = new File(getMainDirectoryFile(), "runtime.log");
            if (runtimeLogFile.exists() && runtimeLogFile.isDirectory())
                runtimeLogFile.delete();
        }
        return runtimeLogFile;
    }

    public static File getBackupFile(File f) {
        return new File(f.getAbsolutePath() + ".bak");
    }

    public static String readFromFile(File f) {
        StringBuilder result = new StringBuilder();
        FileReader fr = null;
        try {
            fr = new FileReader(f);
            char[] chs = new char[1024];
            int len = 0;
            while ((len = fr.read(chs)) >= 0) {
                result.append(chs, 0, len);
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        close(fr, f);
        return result.toString();
    }

    public static boolean append2SimpleLogFile(String s) {
        if (getSimpleLogFile().length() > 31_457_280) // 30MB
            getSimpleLogFile().delete();
        return append2File(Log.getFormatDateTime() + "  " + s + "\n", getSimpleLogFile());
    }

    public static void append2RuntimeLogFile(String s) {
        if (getRuntimeLogFile().length() > 31_457_280) // 30MB
            getRuntimeLogFile().delete();
        append2File(Log.getFormatDateTime() + "  " + s + "\n", getRuntimeLogFile());
    }

    public static boolean write2File(String s, File f) {
        boolean success = false;
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            fw.write(s);
            fw.flush();
            success = true;
        } catch (Throwable t) {
            if (!f.equals(getRuntimeLogFile()))
                Log.printStackTrace(TAG, t);
        }
        close(fw, f);
        return success;
    }

    public static boolean append2File(String s, File f) {
        boolean success = false;
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
            fw.append(s);
            fw.flush();
            success = true;
        } catch (Throwable t) {
            if (!f.equals(getRuntimeLogFile()))
                Log.printStackTrace(TAG, t);
        }
        close(fw, f);
        return success;
    }

    public static boolean copyTo(File f1, File f2) {
        return write2File(readFromFile(f1), f2);
    }

    public static void close(Closeable c, File f) {
        try {
            if (c != null)
                c.close();
        } catch (Throwable t) {
            if (!f.equals(getRuntimeLogFile()))
                Log.printStackTrace(TAG, t);
        }
    }

}
