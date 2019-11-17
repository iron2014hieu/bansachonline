package iron2014.bansachonline.ApiRetrofit.InTerFace;
import java.util.List;

import iron2014.bansachonline.model.Books;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInTerFace {
    @GET("sach/getBooks.php")
    Call<List<Books>> getBooks(@Query("key") String keyword);

    @GET("sach/getBooksTensachRandom.php")
    Call<List<Books>> getBookRandom(@Query("key") String key);

    @GET("sach/getBooksTensach.php")
    Call<List<Books>> getBook_tensach(@Query("key") String keyword);

    @GET("sach/getBooksDESC.php")
    Call<List<Books>> getBookDESC();

    //lấy sách theo tác giả, nxb, thể loại
    @GET("sach/getBookbyMa.php")
    Call<List<Books>> getBookbyMatheloai(@Query("matheloai") String matheloai);
    @GET("sach/getBookbyMa.php")
    Call<List<Books>> getBookbyMatacgia(@Query("matacgia") String matacgia);
    @GET("sach/getBookbyMa.php")
    Call<List<Books>> getBookbyManxb(@Query("manxb") String matheloai);

    // sachs chi tiet

    @GET("sach/getBookDetail.php")
    Call<List<Books>> getBookDetail(@Query("masach") String masach);
}
