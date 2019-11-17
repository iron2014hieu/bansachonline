package iron2014.bansachonline.ApiRetrofit.InTerFace;


import java.util.List;

import iron2014.bansachonline.model.DatMua;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInTerFaceDatmua {
    @GET("giohang/query_carts.php")
    Call<List<DatMua>> getDatMua(@Query("mauser") String keyword);

    @GET("giohang/query_carts_bill.php")
    Call<List<DatMua>> getDatMuaThanhtoan(@Query("mauser") String keyword);
}
