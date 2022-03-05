package org.kis.movietogether.controller.ui.player;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


public class PlayerController {
    public static final String FXML = "player-view.fxml";


    @FXML
    protected BorderPane root;

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;
    private ImageView video;

    @FXML
    protected void initialize() {
        setup();
    }

    public void setup() {
        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        video = new ImageView();
        mediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(video));
    }

    public void exit() {
        mediaPlayer.release();
        factory.release();
    }
}
