/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import constants.Constants;

/**
 *
 * @author Onur
 */
public class MessagePacket {

    private String code;
    private String fromAddress;
    private String content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return code + Constants.SPLIT_CHARACTER_LEVEL_1
                + fromAddress + Constants.SPLIT_CHARACTER_LEVEL_1
                + content;
    }

    public boolean toClass(String s) {
        try {
            String[] variables = s.split(Constants.SPLIT_CHARACTER_LEVEL_1);
            this.code = variables[0];
            this.fromAddress = variables[1];
            this.content = variables[2];
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
