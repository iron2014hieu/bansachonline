package iron2014.bansachonline.ApiRetrofit.InTerFace;


import java.util.List;

import iron2014.bansachonline.model.BookFavorite;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInTerFaceFav {
    @GET("yeuthich/check_like.php")
    Call<List<BookFavorite>> check_like(@Query("masach") String masach, @Query("mauser") String mauser);

    @GET("yeuthich/get_favorite.php")
    Call<List<BookFavorite>> get_favorite(@Query("mauser") String mauser);
}
