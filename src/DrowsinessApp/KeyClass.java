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
    private String key;
    private String timePoint;

    public KeyClass(String key, String timePoint) {
        this.key = key;
        this.timePoint = timePoint;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }
}
