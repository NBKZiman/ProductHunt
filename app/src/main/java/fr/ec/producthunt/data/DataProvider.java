package fr.ec.producthunt.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import fr.ec.producthunt.data.database.RoomProductHuntDb;
import fr.ec.producthunt.data.database.dao.PostDao;
import fr.ec.producthunt.data.model.Post;
import fr.ec.producthunt.data.repository.post.remote.PostRemoteRepository;
import fr.ec.producthunt.data.repository.post.remote.PostRemoteRepositoryException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DataProvider {

  public static final String POST_API_END_POINT =
      "https://api.producthunt.com/v1/posts?access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb";

  private JsonPostParser jsonPostParser = new JsonPostParser();

  private final PostDao postDao;
  private final PostRemoteRepository postRemoteRepository;

  private static DataProvider dataProvider;

  public static DataProvider getInstance(Application application) {

    if (dataProvider == null) {
      dataProvider = new DataProvider(application);
    }
    return dataProvider;
  }

  public DataProvider(Context context) {
    postDao = RoomProductHuntDb.getDatabase(context).postDeo();
    postRemoteRepository = new PostRemoteRepository();
  }

  public List<Post> getPostsFromDatabase() {
    return postDao.getPosts();
  }

  public Boolean syncPost() {

    int nb = 0;

    try {
      List<Post> postList = postRemoteRepository.getPosts();
      postDao.save(postList);
      nb = postList.size();
    } catch (PostRemoteRepositoryException e) {
      Log.e(TAG, "syncPost: Fails", e);
    }

    return nb > 0;
  }
}

