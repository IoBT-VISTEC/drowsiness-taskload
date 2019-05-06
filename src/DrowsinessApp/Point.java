/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DrowsinessApp;
/**
 *
 * @author guygu
 */
class Point {
    private int x;
    private int y;
    private String timePoint;
    
    public Point(int x, int y, String time){
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

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }
    
}
