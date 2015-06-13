/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpio;

/**
 *
 * @author Onur
 */
public interface IPINController {

    //<editor-fold desc="methods">
    public void right();

    public void left();

    public void stop();

    public void go();

    public void back();
    
    public void setSpeed(int speed);
    //</editor-fold>
}
