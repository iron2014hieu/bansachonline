package iron2014.bansachonline.ApiRetrofit.InTerFace;


import java.util.List;

import iron2014.bansachonline.model.TheLoai;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInTerFaceTheloai {
    @GET("theloai/getTheloai.php")
    Call<List<TheLoai>> getTheloai();
}
