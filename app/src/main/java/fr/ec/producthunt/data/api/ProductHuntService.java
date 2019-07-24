package fr.ec.producthunt.data.api;

import fr.ec.producthunt.data.api.model.PostResponse;
import fr.ec.producthunt.data.api.model.PostResponseList;
import fr.ec.producthunt.data.model.Post;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductHuntService {
  @GET("posts?access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb")
  Call<PostResponseList> getPosts();
}
