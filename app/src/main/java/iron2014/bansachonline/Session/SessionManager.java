package iron2014.bansachonline.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.HashMap;

import iron2014.bansachonline.LoginRegister.LoginActivity;
import iron2014.bansachonline.LoginRegister.ProfileActivity;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE =0;

    public static final String PREF_NAMR = "LOGIN";
    public static final String LOGIN = "IS_LOGIN";
    public static final String EMAIL = "EMAIL";
    public static final String ADDRESS = "ADDRESS";
    public static final String NAME = "NAME";
    public static final String ID = "ID";
    public static final String QUYEN = "QUYEN";
    public static final String PHONE = "PHONE";
    
    // get matheloai --> get sach theo the loai

    public static final String ID_BILL = "ID_BILL";
    public static final String TEN_USER = "TEN_USER";

    public static final String TEN_THELOAI = "TENTHELOAI";
    //get book
    public static final String MASACH = "ID_BOOK";
    public static final String TENSACH = "TENSACH";
    public static final String MANXB = "MANXB";
    public  static final String MATHELOAI = "MATHELOAI";
    public static final String NGAYXB = "NGAYXB";
    public static final String NOIDUNG = "NOIDUNG";
    public static final String ANHBIA = "ANHBIA";
    public static final String GIA = "GIA";
    public static final String TENNXB = "TENNXB";
    public static final String SOLUONG = "SOLUONG";
    public static final String TACGIA = "TACGIA";
    public static final String TONGDIEM = "TONGDIEM";
    public static final String LANDANHGIA = "LANDANHGIA";

    public static final String DATHANHTOAN = "DATHANHTOAN";
    public static final String LINK_BOOK_READ = "LINK_BOOK_READ";
    public static final String AUDIO = "AUDIO";

    //bảng tấc giả
    public static final String MATACGIA = "MATACGIA";
    public static final String TENTACGIA = "TENTACGIA";
    //bảng thể loại
    public static final String TENTHELOAI = "TENTHELOAI";
    // hóa đơn
    public static final String TINHTRANG_HOADON = "TENTHELOAI";
    //suggest book
    public static String SUGGEST_BOOK ="SUGGEST_BOOK";
    public static String TOKEN ="TOKEN";

    public static String IDCTHD = "IDCTHD";
    public static String NOIDUNGCTHD = "NOIDUNGCTHD";
    public static String DIEMDANHGIACTHD ="DIEMDANHGIACTHD";

    public static String TONGTIEN = "TONGTIEN";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAMR, PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }
    public void createBottomSheetBook(String tensach,String linkbook,String giaban,String soluong){
        editor.putString(TENSACH, tensach);
        editor.putString(ANHBIA, linkbook);
        editor.putString(GIA, giaban);
        editor.putString(SOLUONG, soluong);
        editor.apply();
    }
    public void createSession(String id,String email,String address,String phone, String name, String quyen){
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.putString(EMAIL, email);
        editor.putString(ADDRESS, address);
        editor.putString(PHONE, phone);
        editor.putString(NAME, name);
        editor.putString(QUYEN, quyen);
        editor.apply();
    }
    public void createTongtien(String tongtien){
        editor.putString(TONGTIEN, tongtien);
        editor.apply();
    }
    public void createSessionGuimatheloai(String matheloai,String tentheloai){
        editor.putString(MATHELOAI, matheloai);
        editor.putString(TEN_THELOAI, tentheloai);
        editor.apply();
    }
    public void createSessionGuimatacgia(String matacgia,String tentacgia){
        editor.putString(MATACGIA, matacgia);
        editor.putString(TENTACGIA, tentacgia);
        editor.apply();
    }
    public void createSessionGuimaNXB(String manxb,String tennxb){
        editor.putString(MANXB, manxb);
        editor.putString(TENNXB, tennxb);
        editor.apply();
    }

    public void createSessionSendInfomationBook(String masach,String tensach,
                                                String manxb,String matheloai,String ngayxb,
                                                String noidung,String anhbia,String gia,
                                                String tennxb,String soluong,String tacgia,String matacgia,
                                                String tongdiem, String landanhgia){
        editor.putString(MASACH, masach);
        editor.putString(TENSACH, tensach);
        editor.putString(MANXB, manxb);
        editor.putString(MATHELOAI, matheloai);
        editor.putString(NGAYXB, ngayxb);
        editor.putString(NOIDUNG, noidung);
        editor.putString(ANHBIA, anhbia);
        editor.putString(GIA, gia);
        editor.putString(TENNXB, tennxb);
        editor.putString(SOLUONG, soluong);
        editor.putString(TACGIA, tacgia);
        editor.putString(MATACGIA, matacgia);
        editor.putString(TONGDIEM, tongdiem);
        editor.putString(LANDANHGIA, landanhgia);
        editor.apply();
    }
    public void createCTHD_ID(String idcthd,String noidungcthd,String diem){
        editor.putString(IDCTHD, idcthd);
        editor.putString(NOIDUNGCTHD, noidungcthd);
        editor.putString(DIEMDANHGIACTHD, diem);
        editor.apply();
    }
    public void createCart(String idSach, String iduser,String tensach, String giaban, String dathnahtoan){
        editor.putString(MASACH, idSach);
        editor.putString(ID, iduser);
        editor.putString(TENSACH, tensach);
        editor.putString(GIA, giaban);
        editor.putString(DATHANHTOAN, dathnahtoan);
        editor.apply();
    }
    public void createBill(String idBill, String idBook, String nameUser){
        editor.putString(ID_BILL, idBill);
        editor.putString(MASACH, idBook);
        editor.putString(TEN_USER, nameUser);
        editor.apply();
    }
    public void createGuiLinkBook(String tensach,String tacgia, String linkbook,String audio){
        editor.putString(TENSACH, tensach);
        editor.putString(LINK_BOOK_READ, linkbook);
        editor.putString(AUDIO, audio);
        editor.putString(TACGIA, tacgia);
        editor.apply();
    }
    public void createHoadon(String tinhtrang){
        editor.putString(TINHTRANG_HOADON, tinhtrang);
        editor.apply();
    }
    public void createSuggestBook(String suggest){
        editor.putString(SUGGEST_BOOK, suggest);
        editor.apply();
    }
    // kiểm tra login
    public boolean isLOggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }
    public void Checklogin(){
        if (!this.isLOggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((ProfileActivity)context).finish();
        }
    }
    public HashMap<String,String> getBottonsheetBook() {
        HashMap<String, String> token = new HashMap<>();
        token.put(TENSACH, sharedPreferences.getString(TENSACH, null));
        token.put(ANHBIA, sharedPreferences.getString(ANHBIA, null));
        token.put(GIA, sharedPreferences.getString(GIA, null));
        token.put(SOLUONG, sharedPreferences.getString(SOLUONG, null));
        return token;
    }
    public HashMap<String,String> getTongtien() {
        HashMap<String, String> token = new HashMap<>();
        token.put(TONGTIEN, sharedPreferences.getString(TONGTIEN, null));
        return token;
    }
    public HashMap<String,String> getCTHD_ID() {
        HashMap<String, String> token = new HashMap<>();
        token.put(IDCTHD, sharedPreferences.getString(IDCTHD, null));
        token.put(DIEMDANHGIACTHD, sharedPreferences.getString(DIEMDANHGIACTHD, null));
        token.put(NOIDUNGCTHD, sharedPreferences.getString(NOIDUNGCTHD, null));
        return token;
    }
    // các hàm lấy dữ liệu
    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ADDRESS, sharedPreferences.getString(ADDRESS, null));
        user.put(PHONE, sharedPreferences.getString(PHONE, null));
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(QUYEN, sharedPreferences.getString(QUYEN, null));
        return user;
    }
    public HashMap<String,String> getSuggest(){
        HashMap<String, String> suggest = new HashMap<>();
        suggest.put(SUGGEST_BOOK, sharedPreferences.getString(SUGGEST_BOOK, ""));
        return suggest;
    }
    public HashMap<String, String> getMAtheloai(){
        HashMap<String, String> theloai = new HashMap<>();
        theloai.put(MATHELOAI, sharedPreferences.getString(MATHELOAI, null));
        theloai.put(TEN_THELOAI, sharedPreferences.getString(TEN_THELOAI, null));
        return theloai;
    }
    public HashMap<String, String> getMAtacgia(){
        HashMap<String, String> tacgia = new HashMap<>();
        tacgia.put(MATACGIA, sharedPreferences.getString(MATACGIA, null));
        tacgia.put(TENTACGIA, sharedPreferences.getString(TENTACGIA, null));
        return tacgia;
    }
    public HashMap<String, String> getMaNXB(){
        HashMap<String, String> nxb = new HashMap<>();
        nxb.put(MANXB, sharedPreferences.getString(MANXB, null));
        nxb.put(TENNXB, sharedPreferences.getString(TENNXB, null));
        return nxb;
    }
    public HashMap<String, String> getCart(){
        HashMap<String, String> cart = new HashMap<>();
        cart.put(MASACH, sharedPreferences.getString(MASACH, null));
        cart.put(ID, sharedPreferences.getString(ID, null));
        cart.put(TENSACH, sharedPreferences.getString(TENSACH, null));
        cart.put(GIA, sharedPreferences.getString(GIA, null));
        cart.put(DATHANHTOAN, sharedPreferences.getString(DATHANHTOAN, null));
        return cart;
    }
    public HashMap<String, String> getBookDetail(){
        HashMap<String, String> book = new HashMap<>();
        book.put(MASACH, sharedPreferences.getString(MASACH,null));
        book.put(TENSACH, sharedPreferences.getString(TENSACH,null));
        book.put(MANXB, sharedPreferences.getString(MANXB,null));
        book.put(MATHELOAI, sharedPreferences.getString(MATHELOAI, null));
        book.put(NGAYXB, sharedPreferences.getString(NGAYXB,null));
        book.put(NOIDUNG, sharedPreferences.getString(NOIDUNG,null));
        book.put(ANHBIA, sharedPreferences.getString(ANHBIA,null));
        book.put(GIA, sharedPreferences.getString(GIA,null));
        book.put(TENNXB, sharedPreferences.getString(TENNXB,null));
        book.put(SOLUONG, sharedPreferences.getString(SOLUONG,null));
        book.put(TACGIA, sharedPreferences.getString(TACGIA,null));
        book.put(MATACGIA, sharedPreferences.getString(MATACGIA,null));
        book.put(TONGDIEM, sharedPreferences.getString(TONGDIEM,null));
        book.put(LANDANHGIA, sharedPreferences.getString(LANDANHGIA,null));
        return book;
    }
    public HashMap<String, String> getDetailBill(){
        HashMap<String, String> bill = new HashMap<>();

        bill.put(ID_BILL, sharedPreferences.getString(ID_BILL, null));
        bill.put(MASACH, sharedPreferences.getString(MASACH, null));
        bill.put(TEN_USER, sharedPreferences.getString(TEN_USER, null));
        return bill;
    }
    public HashMap<String, String> getLinkbook(){
        HashMap<String, String> book = new HashMap<>();

        book.put(TENSACH, sharedPreferences.getString(TENSACH, null));
        book.put(LINK_BOOK_READ, sharedPreferences.getString(LINK_BOOK_READ, null));
        book.put(AUDIO, sharedPreferences.getString(AUDIO, null));
        book.put(TACGIA, sharedPreferences.getString(TACGIA, null));
        return book;
    }
    public HashMap<String, String> getHoadon(){
        HashMap<String, String> hoadon = new HashMap<>();

        hoadon.put(TINHTRANG_HOADON, sharedPreferences.getString(TINHTRANG_HOADON, null));
        return hoadon;
    }
    public void Logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((ProfileActivity)context).finish();
    }
}
