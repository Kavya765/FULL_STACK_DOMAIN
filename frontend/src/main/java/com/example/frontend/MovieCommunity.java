package com.example.frontend;

import com.example.frontend.models.Movie;
import com.example.frontend.models.Post;
import com.example.frontend.models.Reply;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class MovieCommunity {
    private VBox root;
    private Movies movies;
    private Movie movie;  // The movie for this forum

    public MovieCommunity(Movies movies, Movie movie) {
        this.movies = movies;
        this.movie = movie;
        root = new VBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Forum for " + movie.getTitle());
        Label desc = new Label("Start a conversation and share your opinion....");
        desc.getStyleClass().add("description");
        title.getStyleClass().add("movie-forum-title");

        VBox titleBox = new VBox(title,desc);
        titleBox.setAlignment(Pos.CENTER); // Center it horizontally

        TextField postTextField = new TextField();
        postTextField.setPromptText("Post a new message...");
        postTextField.getStyleClass().add("post-textfield");

        Button postButton = new Button("Post");
        postButton.getStyleClass().add("post-button");
        postButton.setOnAction(e -> {
            postNewMessage(postTextField.getText());
            postTextField.clear();
        });
        HBox posttextbtn = new HBox(postTextField,postButton);
        posttextbtn.setAlignment(Pos.CENTER);

        // Retrieve the posts from the database for the current movie
        List<Post> posts = CommunityClient.getPostsByMovieId(movie.getId());
        posts = posts.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()))  // Most liked first
                .collect(Collectors.toList());

        VBox postsVBox = new VBox(10); // <-- spacing between posts
        postsVBox.setAlignment(Pos.CENTER);
        postsVBox.getStyleClass().add("posts-container");
        for (Post post : posts) {
            VBox postBox = createPostBox(post);
            postsVBox.getChildren().add(postBox);
        }

        root.getChildren().addAll(titleBox, posttextbtn, postsVBox);
    }

    private VBox createPostBox(Post post) {
        VBox postBox = new VBox();
        postBox.setAlignment(Pos.CENTER);
        postBox.getStyleClass().add("posts-container");

        Label postAuthor = new Label(MoviesClient.getUserById(post.getUserId()).getUsername());
        postAuthor.getStyleClass().add("post-author");

        Label postText = new Label(post.getPostText());
        postText.getStyleClass().add("post-text");
        postText.setWrapText(true);

        Label createdAt = new Label(post.getCreatedAt().toString().substring(0, 10));
        createdAt.getStyleClass().add("post-created-at");
        createdAt.setTranslateY(-10);

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        HBox authorBox = new HBox(10, postAuthor, spacer1, createdAt);
        authorBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer0 = new Region();
        HBox.setHgrow(spacer0, Priority.ALWAYS);

        HBox textBox = new HBox(postText,spacer0);

        // Like button + like count
        Label likesLabel = new Label(String.valueOf(CommunityClient.getPostLikesCount(post.getPostId())));
        likesLabel.getStyleClass().add("post-likes");
        likesLabel.setTranslateY(-30);

        String icon = CommunityClient.hasUserLikedPost(UserSession.getInstance().getUserId(), post.getPostId())
                ? "/images/liked.png"
                : "/images/like.png";
        ImageView likeIcon = new ImageView(new Image(getClass().getResource(icon).toExternalForm()));
        likeIcon.setFitHeight(30);
        likeIcon.setFitWidth(30);
        likeIcon.setPreserveRatio(true);
        likeIcon.setTranslateY(-30);

        Button likePostButton = new Button();
        likePostButton.setGraphic(likeIcon);
        likePostButton.getStyleClass().add("like-button");
        likePostButton.setOnAction(event -> likePost(post.getPostId(), likesLabel, likePostButton));
        likePostButton.setTranslateX(-30);
        likePostButton.setTranslateX(10);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        HBox likeBox = new HBox(0, spacer2, likePostButton, likesLabel);
        likeBox.setAlignment(Pos.CENTER_LEFT);

        // Replies section
        VBox repliesVBox = new VBox(5);
        repliesVBox.getStyleClass().add("replies-box");
        repliesVBox.setSpacing(5);

        List<Reply> replies = CommunityClient.getRepliesByPostId(post.getPostId());

        // Sort replies by likes (descending order)
        replies.sort((r1, r2) -> Integer.compare(r2.getLikes(), r1.getLikes()));


        // Reply icon and input box (initially hidden)
        Image normalReplyImage = new Image(getClass().getResource("/images/replyicon2.png").toExternalForm());
        Image hoverReplyImage = new Image(getClass().getResource("/images/replyicon2hover.png").toExternalForm());

        ImageView replyIcon = new ImageView(normalReplyImage);
        replyIcon.setFitWidth(30);
        replyIcon.setFitHeight(30);
        replyIcon.setCursor(Cursor.HAND);
        Tooltip replyTooltip = new Tooltip("Reply");
        replyTooltip.setShowDelay(Duration.ZERO);
        replyTooltip.setHideDelay(Duration.seconds(0.1));
        Tooltip.install(replyIcon, replyTooltip);

        replyIcon.setOnMouseEntered(e -> replyIcon.setImage(hoverReplyImage));
        replyIcon.setOnMouseExited(e -> replyIcon.setImage(normalReplyImage));

        // Reply input box (initially hidden)
        TextField replyTextField = new TextField();
        replyTextField.setPromptText("Write a reply...");
        replyTextField.setMaxWidth(300);

        Button replyButton = new Button("Reply");
        replyButton.getStyleClass().add("reply-button");

        HBox replyInputBox = new HBox(10, replyTextField, replyButton);
        replyInputBox.setAlignment(Pos.CENTER_LEFT);
        replyInputBox.setMaxWidth(Double.MAX_VALUE);
        replyInputBox.setVisible(false); // Start hidden

        HBox.setHgrow(replyTextField, Priority.ALWAYS);

        // Toggle visibility on icon click
        replyIcon.setOnMouseClicked(e -> {
            replyInputBox.setVisible(!replyInputBox.isVisible());
        });

        // Post the reply
        replyButton.setOnAction(e -> {
            String replyText = replyTextField.getText();
            if (!replyText.isEmpty()) {
                postReply(post.getPostId(), replyText);
                replyTextField.clear();
                replyInputBox.setVisible(false);
            }
        });

        // Wrap icon + input in VBox
        VBox replyControlBox = new VBox(5, replyIcon, replyInputBox);
        replyControlBox.setAlignment(Pos.CENTER_LEFT);
        replyControlBox.setPadding(new Insets(10, 0, 0, 0)); // optional spacing

        postBox.getChildren().addAll(authorBox,textBox, likeBox, repliesVBox, replyControlBox);

        // Function to update the replies display based on the current state
        BiConsumer<List<Reply>, Boolean> updateRepliesDisplay = (replyList, showAll) -> {
            repliesVBox.getChildren().clear(); // Clear existing replies
            int limit = showAll ? replyList.size() : Math.min(replyList.size(), 2);
            for (int i = 0; i < limit; i++) {
                Reply reply = replyList.get(i);
                VBox replyBox = new VBox(3);
                replyBox.getStyleClass().add("reply-box");

                Label replyUser = new Label(MoviesClient.getUserById(reply.getUserId()).getUsername());
                replyUser.getStyleClass().add("reply-user");

                Label replyDate = new Label(reply.getCreated_at().toString().substring(0, 10));
                replyDate.getStyleClass().add("reply-date");

                Region replySpacer = new Region();
                HBox.setHgrow(replySpacer, Priority.ALWAYS);

                HBox userDateBox = new HBox(10, replyUser, replySpacer, replyDate);
                userDateBox.setAlignment(Pos.CENTER_LEFT);

                Label replyText = new Label(reply.getReplyText());
                replyText.getStyleClass().add("reply-text");
                replyText.setWrapText(true);

                Label replyLikesLabel = new Label(String.valueOf(CommunityClient.getReplyLikesCount(reply.getReplyId())));
                replyLikesLabel.setTranslateX(-10);
                replyLikesLabel.getStyleClass().add("reply-likes");

                String replyIconString = CommunityClient.hasUserLikedReply(UserSession.getInstance().getUserId(), reply.getReplyId()) ? "/images/liked.png" : "/images/like.png";
                ImageView likeReplyIcon = new ImageView(new Image(getClass().getResource(replyIconString).toExternalForm()));
                likeReplyIcon.setFitHeight(20);
                likeReplyIcon.setFitWidth(20);
                likeReplyIcon.setPreserveRatio(true);

                Button likeReplyButton = new Button();
                likeReplyButton.setGraphic(likeReplyIcon);
                likeReplyButton.getStyleClass().add("like-button");
                likeReplyButton.setOnAction(event -> {
                    likeReply(reply.getReplyId(), replyLikesLabel, likeReplyButton);
                });

                HBox likesBox = new HBox(5, likeReplyButton, replyLikesLabel); // small spacing
                likesBox.setAlignment(Pos.CENTER_LEFT);
                likesBox.setMaxWidth(Region.USE_PREF_SIZE); // avoid stretching

                replyBox.getChildren().addAll(userDateBox, replyText, likesBox);
                repliesVBox.getChildren().add(replyBox);
            }
        };

        // Initially display only the first two replies
        updateRepliesDisplay.accept(replies, false);

        // Show "Show More" button only if there are more than 2 replies
        if (replies.size() > 2) {
            // ToggleButton to show more or less replies
            ToggleButton showMoreButton = new ToggleButton("Show More ↓");
            showMoreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-width: 0; -fx-padding: 0;");
            showMoreButton.setSelected(false); // Initially not selected

            // Set the action for the ToggleButton
            showMoreButton.setOnAction(event -> {
                boolean showAll = showMoreButton.isSelected();
                showMoreButton.setText(showAll ? "Show Less ↑" : "Show More ↑");
                updateRepliesDisplay.accept(replies, showAll);
            });

            // Add the ToggleButton below the replies (at the end of the post)
            HBox showMoreBox = new HBox();
            showMoreBox.setAlignment(Pos.CENTER);  // Center the button inside the HBox
            showMoreBox.getChildren().add(showMoreButton);  // Add the button to the HBox

            postBox.getChildren().add(showMoreBox);
        }

        return postBox;
    }

    private void postNewMessage(String postText) {
        CommunityClient.addPost(UserSession.getInstance().getUserId(), movie.getId(), postText);

        List<Post> posts = CommunityClient.getPostsByMovieId(movie.getId());
        posts = posts.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()))  // Most liked first
                .collect(Collectors.toList());

        VBox postsVBox = new VBox();
        postsVBox.getStyleClass().add("posts-container");
        for (Post post : posts) {
            VBox postBox = createPostBox(post);
            postsVBox.getChildren().add(postBox);
        }

        reloadPosts();
    }

    private void postReply(int postId, String replyText) {
        CommunityClient.addReply(postId, UserSession.getInstance().getUserId(), replyText);

        reloadPosts();
    }

    private void likePost(int postId, Label likesLabel, Button likePostButton) {
        if(CommunityClient.hasUserLikedPost(UserSession.getInstance().getUserId(), postId)) {
            CommunityClient.removeLikeFromPost(UserSession.getInstance().getUserId(), postId);
        } else {
            CommunityClient.updatePostLike(UserSession.getInstance().getUserId(), postId);
        }

        int updatedLikeCount = CommunityClient.getPostLikesCount(postId);

        String icon = CommunityClient.hasUserLikedPost(UserSession.getInstance().getUserId(), postId) ? "/images/liked.png" : "/images/like.png";
        ImageView likeIcon = new ImageView(new Image(getClass().getResource(icon).toExternalForm()));
        likeIcon.setFitHeight(30);
        likeIcon.setFitWidth(30);
        likeIcon.setPreserveRatio(true);
        likeIcon.setTranslateY(-30);

        likesLabel.setText(String.valueOf(updatedLikeCount));

        likePostButton.setGraphic(likeIcon);
    }

    private void likeReply(int replyId, Label replyLikesLabel, Button likeReplyButton) {
        if(CommunityClient.hasUserLikedReply(UserSession.getInstance().getUserId(), replyId)) {
            CommunityClient.removeLikeFromReply(UserSession.getInstance().getUserId(), replyId);
        } else {
            CommunityClient.updateReplyLike(UserSession.getInstance().getUserId(), replyId);
        }

        int updatedLikeCount = CommunityClient.getReplyLikesCount(replyId);

        String icon = CommunityClient.hasUserLikedReply(UserSession.getInstance().getUserId(), replyId) ? "/images/liked.png" : "/images/like.png";
        ImageView likeIcon = new ImageView(new Image(getClass().getResource(icon).toExternalForm()));
        likeIcon.setFitHeight(20);
        likeIcon.setFitWidth(20);
        likeIcon.setPreserveRatio(true);

        likeReplyButton.setGraphic(likeIcon);

        replyLikesLabel.setText(String.valueOf(updatedLikeCount));
    }

    private void reloadPosts() {
        List<Post> posts = CommunityClient.getPostsByMovieId(movie.getId());

        // Sort posts based on the number of replies
        posts = posts.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()))  // Most liked first
                .collect(Collectors.toList());


        VBox newPostsVBox = new VBox();
        newPostsVBox.getStyleClass().add("posts-container");
        for (Post post : posts) {
            VBox postBox = createPostBox(post);
            newPostsVBox.getChildren().add(postBox);
        }

        root.getChildren().removeIf(node -> node.getStyleClass().contains("posts-container")); // Remove old posts section
        root.getChildren().add(newPostsVBox); // Add the updated posts
    }


    public VBox getRoot() {
        return root;
    }
}

