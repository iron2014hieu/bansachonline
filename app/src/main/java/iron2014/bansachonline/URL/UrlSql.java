package iron2014.bansachonline.URL;

public class UrlSql {
    private static String baseURL = "https://bansachonline.xyz/";
    public static String URL_LOGIN = baseURL+"bansach/loginregister/login.php";
    public static String URL_LOGIN_PHONE = baseURL+"bansach/loginregister/login_with_phone.php/?phone=";

    //cthd
    public static String url_insert_cthd="https://bansachonline.xyz/bansach/hoadon/cthd/them_cthd.php";
    public  static String url_insert_hoadon ="https://bansachonline.xyz/bansach/hoadon/them_hoadon.php";
    public static String URL_THEMNHATXET ="https://bansachonline.xyz/bansach/hoadon/cthd/them_nhanxet.php";

    public static String URL_INSERT_NOTIF = baseURL+"bansach/thongbao/insert_notif.php";

    public  static String URL_INSERT_GIOHANG ="https://bansachonline.xyz/bansach/giohang/create_carts.php";
    public static String URL_UPDATE_CARTS =baseURL+"bansach/giohang/update_carts.php";
    public static String URL_DELETE_CARTS =baseURL+"bansach/giohang/delete_carts.php";

    //giam số lượng sách sau mua
    public static String URL_UPDATE_SOLUONG = "https://bansachonline.xyz/bansach/hoadon/cthd/update_soluong_sach.php";

    //yeu thich
    public static String URL_INSERT_FAVORITE = "https://bansachonline.xyz/bansach/yeuthich/insert_favorite.php";
    public static String URL_DELETE_FAVORITE ="https://bansachonline.xyz/bansach/yeuthich/delete_favorite.php";
}
