/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DrowsinessApp;

import java.sql.Timestamp;
/**
 *
 * @author guygu
 */
class Point {
    int x;
    int y;
    Timestamp time;
    
    public Point(int x, int y, Timestamp time){
        this.x = x;
        this.y = y;
        this.time = time;
    }
    
    @Override
    public String toString(){
        return "x: " + this.x + ", y: " + this.y + " (" + time.toLocalDateTime() + ")";
    }
}
