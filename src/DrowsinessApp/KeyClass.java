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
class KeyClass {
    private char key;
    private String timePoint;

    public KeyClass(char key, String timePoint) {
        this.key = key;
        this.timePoint = timePoint;
    }

    public char getKey() {
        return key;
    }

    public void setKey(char key) {
        this.key = key;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }
}
