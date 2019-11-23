package iron2014.bansachonline.ApiRetrofit.InTerFace;


import java.util.List;

import iron2014.bansachonline.model.DatMua;
import iron2014.bansachonline.model.Notification;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInTerFaceNotif {
    @GET("thongbao/get_notif_km.php")
    Call<List<Notification>> get_notif_km();

    @GET("thongbao/get_notif_dh.php")
    Call<List<Notification>> get_notif_dh(@Query("mauser") String mauser);
}
