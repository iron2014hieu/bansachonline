package iron2014.bansachonline.URL;

public class EndPoints {
    private  static String baseUrl = "https://bansachonline.xyz/bansach/notification/";
    public static final String URL_REGISTER_DEVICE = baseUrl+"RegisterDevice.php";
    public static final String URL_SEND_SINGLE_PUSH = baseUrl+"sendSinglePush.php";
    public static final String URL_SEND_MULTIPLE_PUSH = baseUrl+"sendMultiplePush.php";
    public static final String URL_FETCH_DEVICES = baseUrl+"GetRegisteredDevices.php";

    public static final String URL_UPDATE_DEVICES_ISLOGIN = baseUrl+"update_islogin.php";
}

