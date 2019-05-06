/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DrowsinessApp;

import processing.core.PApplet;
import processing.core.PSurface;

public class MainCore extends PApplet {
    public boolean isPress;
    public char lastKey;
    
    public MainCore(){
        isPress = false;
    }
    
    public void setup(){
        size(570,480);
        smooth();
    }
    
    public void draw(){
        if(key == '4'){
            isPress = true;
        }
        lastKey = key;
    }
    
    public PSurface getInitSurface(){
        return initSurface();
    }
    
    public int getMouseX(){
        return mouseX;
    }
    
    public int getMouseY(){
        return mouseY;
    }
}
