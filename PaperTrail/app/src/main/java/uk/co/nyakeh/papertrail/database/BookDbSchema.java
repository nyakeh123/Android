package uk.co.nyakeh.papertrail.database;

public class BookDbSchema {
    public static final class BookTable {
        public static final String NAME = "books";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String AUTHOR = "author";
            public static final String LENGTH = "length";
            public static final String BLURB = "blurb";
            public static final String DATE_STARTED = "dateStarted";
            public static final String DATE_FINISHED = "dateFinished";
            public static final String ISBN = "isbn";
            public static final String IMAGE_URL = "imageUrl";
        }
    }
}