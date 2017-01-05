package api.mal;

import okhttp3.ResponseBody;
import api.mal.model.Search;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MALService
{
	@GET("search/prefix.json")
	Call<Search> search(@Query("type") String type, @Query("keyword") String keyword, @Query("v") int v);

	@GET("manga/{id}")
	Call<ResponseBody> getMangaInformation(@Path("id") int id);
	
	@GET
	Call<ResponseBody> downloadImage(@Url String imageUrl);  
}
