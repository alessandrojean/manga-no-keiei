package api.mcd;

import okhttp3.ResponseBody;
import api.mcd.model.Keyword;
import api.mcd.model.Search;
import api.mcd.model.Serie;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface MCDService
{
	@POST("search/")
	Call<Search> search(@Body Keyword keyword);

	@GET("series/{id}/")
	Call<Serie> getSerie(@Path("id") int id);

	@GET
	Call<ResponseBody> downloadImage(@Url String imageUrl);
}
