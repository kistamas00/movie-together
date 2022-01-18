package org.kis.movietogether.controller.media;

import java.util.ArrayList;
import java.util.List;

public final class MediaController {

    private static MediaController mediaController;
    private final List<MediaInfoConsumer> consumers;

    private MediaController() {
        this.consumers = new ArrayList<>();
    }

    public static MediaController getInstance() {
        if (mediaController == null) {
            mediaController = new MediaController();
        }
        return mediaController;
    }

    public void subscribe(final MediaInfoConsumer mediaInfoConsumer) {
        consumers.add(mediaInfoConsumer);
    }
}
