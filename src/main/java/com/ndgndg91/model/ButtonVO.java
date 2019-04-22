package com.ndgndg91.model;

import lombok.Data;

@Data
public class ButtonVO {
    private String type;
    private String[] buttons;

    public ButtonVO(String[] buttons){
        this.type = "buttons";
        this.buttons = buttons;
    }
}
