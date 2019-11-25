package iron2014.bansachonline.ApiRetrofit.InTerFace;


import java.util.List;

import iron2014.bansachonline.model.CTHD;
import iron2014.bansachonline.model.Hoadon;
import iron2014.bansachonline.model.KhuyenMai;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInTerFaceHoadon {
    @GET("hoadon/get_choxacnhan.php")
    Call<List<Hoadon>> get_choxacnhan(@Query("mauser") String keyword);
    @GET("hoadon/get_cholayhang.php")
    Call<List<Hoadon>> get_cholayhang(@Query("mauser") String keyword);
    @GET("hoadon/get_danggiao.php")
    Call<List<Hoadon>> get_danggiao(@Query("mauser") String keyword);
    @GET("hoadon/get_danhgia.php")
    Call<List<Hoadon>> get_danhgia(@Query("mauser") String keyword);
    //cthd
    @GET("hoadon/cthd/get_cthd_bymahd.php")
    Call<List<CTHD>> get_cthd_bymahd(@Query("mahd") String keyword);
    @GET("hoadon/cthd/get_library_user.php")
    Call<List<CTHD>> get_library_user(@Query("mauser") String keyword);
    // laays heets nhaan xet theo sach
    @GET("hoadon/cthd/get_all_cthd.php")
    Call<List<CTHD>> get_all_cthd(@Query("masach") String keyword);
    @GET("hoadon/cthd/get_5_cthd.php")
    Call<List<CTHD>> get_5_cthd(@Query("masach") String keyword);

    @GET("hoadon/get_all_donhang.php")
    Call<List<Hoadon>> get_all_donhang();

    @GET("hoadon/get_all_danggiao.php")
    Call<List<Hoadon>> get_all_danggiao();

    @GET("hoadon/get_all_danhgia.php")
    Call<List<Hoadon>> get_all_danhgia();


    @GET("khuyenmai/get_all_khuyenmai.php")
    Call<List<KhuyenMai>> get_all_khuyenmai();
    //lay cthd theo id
    @GET("hoadon/get_cthd_byid.php")
    Call<List<CTHD>> get_cthd_byid(@Query("id") String id);
}
