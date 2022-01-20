package org.kis.movietogether.controller;

import org.kis.movietogether.controller.media.MediaInfoConsumer;

import java.util.ArrayList;
import java.util.List;

public final class ApplicationController {

    private static ApplicationController applicationController;
    private final List<MediaInfoConsumer> consumers;

    private ApplicationController() {
        this.consumers = new ArrayList<>();
    }

    public static synchronized ApplicationController getInstance() {
        if (applicationController == null) {
            applicationController = new ApplicationController();
        }
        return applicationController;
    }

    public void subscribe(final MediaInfoConsumer mediaInfoConsumer) {
        consumers.add(mediaInfoConsumer);
    }
}
