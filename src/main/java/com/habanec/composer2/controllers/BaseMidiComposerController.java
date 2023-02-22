package com.habanec.composer2.controllers;

import com.habanec.composer2.services.ComposerService;

public abstract class BaseMidiComposerController {

    protected ComposerService composerService;

    public BaseMidiComposerController(ComposerService composerService) {
        this.composerService = composerService;
    }

}
