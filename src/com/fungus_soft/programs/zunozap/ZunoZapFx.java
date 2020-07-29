package com.fungus_soft.programs.zunozap;

import com.fungus_soft.desktop.api.JFXProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.zunozap.impl.BrowserImpl;

import javafx.application.Platform;

@ProgramInfo(name = "ZunoZap Web Browser", version="0.9.1", authors="Fungus Software", width=800, height=460)
public class ZunoZapFx extends JFXProgram {

    public ZunoZapFx() {
        BrowserImpl zunozap = new ZunoZapImpl(this);
        try {
            Platform.runLater(() -> {
                try {
                    zunozap.start(null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.setTitle("Fungus ZunoZap");
        this.setVisible(true);
    }

}