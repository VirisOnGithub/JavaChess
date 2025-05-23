package javachess.translation;

/**
 * Enum representing various messages that can be sent to the user.
 * These messages are used by the LanguageService to provide messages in the user's language.
 */
public enum Message {
    CHECKMATE,
    CHECK,
    STALEMATE,
    DRAW,
    THREEFOLD_REPETITION,
    FIFTY_MOVES,
    PROMOTE,
    // Settings
    SETTINGS,
    LANGUAGE,
    ENGLISH,
    FRENCH,
    SPANISH,
    PIECE_SET,
    SOUND,
    ENABLE_SOUND,
    SAVE,
    SAVING_FAILED,
    ERROR,

    // Main Menu
    MAIN_MENU,
    PLAY_VS_PLAYER,
    PLAY_VS_COMPUTER,
    LOAD_FROM_PGN,
    OPEN_PGN_FILE,
    SELECT_DIFFICULTY,
    EASY,
    NORMAL,
    HARD,

    // Colors
    WHITE,
    BLACK,

    // GUIChessDisplay
    TO_PLAY, LOAD_FROM_FEN,
}
