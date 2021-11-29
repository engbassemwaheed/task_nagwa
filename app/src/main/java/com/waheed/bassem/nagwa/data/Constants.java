package com.waheed.bassem.nagwa.data;

public final class Constants {

    public static final String DATA_FILE_NAME = "file_list.json";

    public static final class DownloadState {
        public static final int NOT_DOWNLOADED = 1;
        public static final int PENDING_DOWNLOAD = 2;
        public static final int DOWNLOADING = 3;
        public static final int DOWNLOADED = 4;
        public static final int ERROR_DOWNLOAD = 5;
    }

    public static final class FileType {
        public static final String PDF = "PDF";
        public static final String VIDEO = "VIDEO";
    }

    public static final class JsonConstants {
        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String URL = "url";
        public static final String NAME = "name";

        public static final String DEFAULT_STRING = "-";
        public static final int DEFAULT_INT = -1;
    }

}
