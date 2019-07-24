package fr.ec.producthunt.data.repository.post.mapper;

import fr.ec.producthunt.data.api.model.PostResponse;
import fr.ec.producthunt.data.model.Post;
import java.util.ArrayList;
import java.util.List;

public class PostMapper {

  public List<Post> from(List<PostResponse> postResponses) {
    List<Post> posts = new ArrayList<>(postResponses.size());
    for (PostResponse postResponse : postResponses) {
      posts.add(from(postResponse));
    }

    return posts;
  }

  private Post from(PostResponse postResponse) {
    Post post = new Post();
    post.setId(postResponse.id);
    post.setImageUrl(postResponse.thumbnail.imageUrl);
    post.setSubTitle(postResponse.subTitle);
    post.setPostUrl(postResponse.postUrl);
    return post;
  }
}
