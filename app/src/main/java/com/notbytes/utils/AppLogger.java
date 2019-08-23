package com.notbytes.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class AppLogger {

    /**
     * Name of properties file.
     */
    private static final String LOG_SETTINGS_FILE_NAME = "sparsh_logging.properties";

    /**
     * Flags to load various log level status.
     */
    private static final String LOG_STATUS = "LOG_STATUS";
    private static final String VERBOSE_LOG_STATUS = "VERBOSE_LOG_STATUS";
    private static final String DEBUG_LOG_STATUS = "DEBUG_LOG_STATUS";
    private static final String INFO_LOG_STATUS = "INFO_LOG_STATUS";
    private static final String WARN_LOG_STATUS = "WARN_LOG_STATUS";
    private static final String ERROR_LOG_STATUS = "ERROR_LOG_STATUS";

    /**
     * Enabled log status value.
     */
    private static final String STATUS_ON = "ON";

    /**
     * Flag to hold log enable status.
     */
    private static boolean sIsLogEnabled;

    /**
     * Flag to hold verbose log enable status.
     */
    private static boolean sIsVerboseLogEnabled;
    /**
     * Flag to hold debug log enable status.
     */
    private static boolean sIsDebugLogEnabled;
    /**
     * Flag to hold info log enable status.
     */
    private static boolean sIsInfoLogEnabled;
    /**
     * Flag to hold warn log enable status.
     */
    private static boolean sIsWarnLogEnabled;
    /**
     * Flag to hold error log enable status.
     */
    private static boolean sIsErrorLogEnabled;

    private static AppLogger mAppLogger = new AppLogger();

    private AppLogger() {

        if (AppUtil.isSDCardAvailable()) {
            try {
                File settingsFile = new File(Environment.getExternalStorageDirectory(), LOG_SETTINGS_FILE_NAME);
                if (settingsFile.exists()) {
                    Properties properties = new Properties();
                    FileInputStream fileInputStream = new FileInputStream(settingsFile);
                    properties.load(fileInputStream);

                    sIsLogEnabled = STATUS_ON.equalsIgnoreCase(properties.getProperty(LOG_STATUS));
                    if (sIsLogEnabled) {
                        sIsVerboseLogEnabled = STATUS_ON.equalsIgnoreCase(properties.getProperty(VERBOSE_LOG_STATUS));
                        sIsDebugLogEnabled = STATUS_ON.equalsIgnoreCase(properties.getProperty(DEBUG_LOG_STATUS));
                        sIsInfoLogEnabled = STATUS_ON.equalsIgnoreCase(properties.getProperty(INFO_LOG_STATUS));
                        sIsWarnLogEnabled = STATUS_ON.equalsIgnoreCase(properties.getProperty(WARN_LOG_STATUS));
                        sIsErrorLogEnabled = STATUS_ON.equalsIgnoreCase(properties.getProperty(ERROR_LOG_STATUS));
                    }
                    fileInputStream.close();
                }
            } catch (IOException e) {
                //Do not print this error
            } catch (Exception e) {
                //Do not print this error
            }
        }
    }

    public static void verbose(String tag, String message) {
        if (sIsVerboseLogEnabled) {
            Log.v(tag, message);
        }
    }

    public static void debug(String tag, String message) {
        if (sIsDebugLogEnabled) {
            Log.d(tag, message);
        }
    }

    public static void info(String tag, String message) {
        if (sIsInfoLogEnabled) {
            Log.i(tag, message);
        }
    }

    public static void warn(String tag, String message) {
        if (sIsWarnLogEnabled) {
            Log.w(tag, message);
        }
    }

    public static void error(String tag, String message) {
        if (sIsErrorLogEnabled) {
            Log.e(tag, message);
        }
    }

    public static void error(String tag, Exception exception) {
        if (sIsErrorLogEnabled) {
            String message = Log.getStackTraceString(exception);
            Log.e(tag, message);
        }
    }
}
