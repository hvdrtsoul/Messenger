package com.ragelar.messenger;

public class Constants {
    public static final String USERS_TABLE = "users";
    public static final String USERS_USER = "user";
    public static final String USERS_PUBLIC_KEY = "publicKey";
    public static final String USERS_SESSION = "session";
    public static final String USERS_LOGGED_UNTIL = "logged_until";

    public static final String CONNECTIONS_TABLE = "connections";
    public static final String CONNECTIONS_ADRESS = "adress";
    public static final String CONNECTIONS_SHARED_KEY = "sharedKey";
    public static final String CONNECTIONS_ALIVE_UNTIL = "aliveUntil";
    public static final String CONNECTION_NOT_FOUND_MESSAGE = "not_found";


    public static final String CREATING_SESSIONS_TABLE = "creatingsessions";
    public static final String CREATING_SESSIONS_USER = "user";
    public static final String CREATING_SESSIONS_SECRET = "secret";
    public static final String CREATING_SESSIONS_ALIVE_UNTIL = "aliveuntil";


    public static final String NICKNAMES_TABLE = "nicknames";
    public static final String NICKNAMES_NICKNAME = "nickname";
    public static final String NICKNAMES_USER = "user";
    public static final String NICKNAME_NOT_FOUND = "not_found";

    public static final String GET_USERNAME_USERNAME_HEADER = "username";

    public static final String MESSAGES_TABLE = "messages";
    public static final String MESSAGES_ID = "id";
    public static final String MESSAGES_TO = "to";
    public static final String MESSAGES_FROM = "from";
    public static final String MESSAGES_TYPE = "type";
    public static final String MESSAGES_TIMESTAMP = "timestamp";
    public static final String MESSAGES_DATA = "data";

    public static final int ADDITIONAL_UPDATE_TIME = 300;
    public static final int ADDITIONAL_LOGGED_IN_TIME = 600;
    public static final long CLEAN_CONNECTIONS_TIME = 600000L;
    public static final long CLEAN_INACTIVE_USER_TIME = 2592000L; // 30 days
    public static final long CONSIDER_INACTIVE_TIME = 5184000L; // 60 days

    public static final String RESPONSE_HEADER_NAME = "result";
    public static final String RESPONSE_HEADER_OKAY = "OK";
    public static final String RESPONSE_HEADER_ERROR = "ERR";
    public static final String RESPONSE_HEADER_DATA = "data";

    public static final String SERVER_HOST = "45.142.36.14";
    public static final String LOCAL_HOST = "localhost";

    public static final String RESPONSE_PUBLIC_DFH_KEY_HEADER = "publicKey";

    public static final int USERNAME_LENGTH = 15;
    public static final int SESSION_LENGTH = 30;
    public static final int NICKNAME_MIN_LENGTH = 3;
    public static final int NICKNAME_MAX_LENGTH = 15;
    public static final int SECRET_LENGTH = 25;

    public static final String JOIN_US_ERROR = "ERR";
    public static final String JOIN_US_INCORRECT_NAME = "incorrect_name";
    public static final String JOIN_US_USER_EXISTS = "user_exists";

    public static final String HYPNOTIZE_INCORRECT_NICKNAME = "incorrect_nickname";
    public static final String HYPNOTIZE_NICKNAME_TAKEN = "nickname_taken";
    public static final String HYPNOTIZE_USER_ALREADY_TAKEN_NICKNAME = "too_many_nicknames";
    public static final String HYPNOTIZE_USER_DOES_NOT_EXIST = "no_such_user";

    public static final String ADDITIONAL_INFO_HEADER = "info";
    public static final String SOMETHING_WENT_WRONG_MESSAGE = "something_wrong";
    public static final String UNKNOWN_HOST_MESSAGE = "unknown_host";
    public static final String INCORRECT_SESSION = "not_authorized";
    public static final String SESSION_HEADER = "session";
    public static final String NOT_CONNECTED_MESSAGE = "not_connected";

    public static final String BAD_REQUEST_MESSAGE = "bad_request";

    public static final String AUTH_USER_DOES_NOT_EXIST = "no_such_user";
    public static final String AUTH_PUBLIC_KEY_NOT_FOUND = "key_not_found";
    public static final String AUTH_CHALLENGE_HEADER = "challenge";

    public static final String LAST_ACTIVE_TABLE = "last_active";
    public static final String LAST_ACTIVE_USERNAME = "username";
    public static final String LAST_ACTIVE_LAST_ACTIVE = "last_active";

    public static final String TWISTED_USER_DOES_NOT_EXIST = "no_such_user";
    public static final String TWISTED_SECRET_NOT_FOUND = "auth_not_found";
    public static final String TWISTED_NEW_SESSION_HEADER = "new_session";
    public static final String TWISTED_WRONG_SOLUTION = "wrong_solution";

    public static final String SEND_RECIPIENT_DOES_NOT_EXIST = "no_recipient";
    public static final String[] SEND_MESSAGE_TYPES = {"text", "image", "request", "answer"};
    public static final String SEND_UNKNOWN_MESSAGE_TYPE = "bad_type";
    public static final String SEND_TIMESTAMP_HEADER = "timestamp";

    public static final char CHECK_MAIL_SEPARATOR = ';';
    public static final String CHECK_MAIL_NO_MESSAGES = "no_mail";
    public static final String CHECK_MAIL_MESSAGES_HEADER_NAME = "messages";

    public static final String GET_MESSAGE_FAIL = "no_such_message";
    public static final String GET_MESSAGE_FROM_HEADER = "from";
    public static final String GET_MESSAGE_TYPE_HEADER = "type";
    public static final String GET_MESSAGE_TIMESTAMP_HEADER = "timestamp";
    public static final String GET_MESSAGE_DATA_HEADER = "data";

    public static final String REQUEST_TYPE_HEADER = "request-type";
    public static final String MEET_REQUEST_HEADER = "meet";
    public static final String REQUEST_DATA_HEADER = "data";
    public static final String KEEP_ALIVE_REQUEST_HEADER = "keep_alive";
    public static final String AUTH_REQUEST_HEADER = "auth";
    public static final String TWISTED_REQUEST_HEADER = "twisted";
    public static final String JOIN_US_REQUEST_HEADER = "join_us";
    public static final String HYPNOTIZE_REQUEST_HEADER = "hypnotize";
    public static final String SEND_REQUEST_HEADER = "send";
    public static final String CHECK_MAIL_REQUEST_HEADER = "check_mail";
    public static final String GET_USERNAME_REQUEST_HEADER = "get_username";
    public static final String GET_MESSAGE_REQUEST_HEADER = "get_message";

    public static final String LOCAL_DATABASE_NAME = "communicator";

}
