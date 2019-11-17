package iron2014.bansachonline.ApiRetrofit.InTerFace;



import java.util.List;

import iron2014.bansachonline.model.Tacgia;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInTerFaceTacgia {
    @GET("tacgia/getTacgia.php")
    Call<List<Tacgia>> getTacgia();

}
