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
    private int x;
    private int y;
    private Timestamp timePoint;
    
    public Point(int x, int y, Timestamp time){
        this.x = x;
        this.y = y;
        this.timePoint = time;
    }
    
    @Override
    public String toString(){
        return "x: " + this.x + ", y: " + this.y + " (" + timePoint + ")";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Timestamp getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Timestamp timePoint) {
        this.timePoint = timePoint;
    }
    
    public String fileName(){
        System.out.println(timePoint);
        String tmp = timePoint.toString().replaceAll("\\s+", "");
        System.out.println(tmp);
        return tmp.substring(0, 10) + 
                "__" + tmp.substring(10, 12) +
                "." + tmp.substring(13, 15) +
                "." + tmp.substring(16);
    }
}
