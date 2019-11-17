package iron2014.bansachonline.ApiRetrofit.InTerFace;



import java.util.List;

import iron2014.bansachonline.model.Nhaxuatban;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInTerFaceNXB {
    @GET("nxb/getNXB.php")
    Call<List<Nhaxuatban>> getNXB();
}
