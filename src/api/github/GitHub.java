package api.github;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import utils.RequestUtils;
import api.github.model.LatestRelease;

public class GitHub
{
	private static final String LATEST_RELEASE_URL = "https://api.github.com/repos/alessandrojean/Java-MangaReadersAPI/releases";

	public static LatestRelease getLatestRelease() throws IOException
	{
		String json = RequestUtils.get(LATEST_RELEASE_URL);
		List<LatestRelease> releases = new Gson().fromJson(json, new TypeToken<List<LatestRelease>>() {
		}.getType());

		return releases.get(0);
	}

}
