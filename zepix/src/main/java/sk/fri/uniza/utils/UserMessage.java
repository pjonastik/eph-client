package sk.fri.uniza.utils;

public final class UserMessage {

    public static final String FILE_NOT_FOUND_OR_UNKOWN_MIME_TYPE = "Súbor nebol nájdený alebo systém nevie detegovať typ súboru!";
    public static final String NOT_SUPPORTED_MIME_TYPE_OF_FILE = "Nepodporovaný typ súboru!";
    public static final String IO_ERROR_DURING_READING_POSTING_SHEET = "Problém pri načítaní ePH súboru!";
    public static final String IO_ERROR_DURING_READING_VALIDATION_INFO = "Problém pri načítaní ePH validačných dát!";
    public static final String INVALID_POSTING_SHEET = "ePH súbor nie je validný!";
    public static final String KEYSTORE_FILE_NOT_FOUND = "Súbor p12 nebol nájdený";
    public static final String AUTHENTICATION_KEYSTORE_FAILED = "Autentifikácia bezpečného úložiska neúspešná!";
    public static final String FILE_EXISTS = "Súbor už existuje!";
    public static final String DIR_NOT_FOUND = "Zlá súborová cesta!";
    public static final String SIGNED_POSTING_SHEET_PATH_WAS_NOT_SET = "Súborová cesta nebola nastavená pre podpísaný ePH";

    public static final String SUBJECT_WAS_NOT_SET = "Subjekt nebol vyplnený!";
    public static final String USER_NAME_WAS_NOT_SET = "Používateľské meno nebolo vyplnené!";
    public static final String USER_PASSWORD_WAS_NOT_SET = "Používateľské heslo nebolo vyplnené!";
    public static final String FILING_ROOM_URL_WAS_NOT_SET = "URL elektronickej podateľne nebolo vyplnené!";
    public static final String FILING_ROOM_URL_IS_MALFORMED = "Chybná URL podateľne!";


    private UserMessage(){
    }
}
